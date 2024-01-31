package Managers.Http;

import Managers.Files.FileBackedTasksManager;
import Managers.Managers;
import Tasks.Enums.TaskType;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port) {
        this(port, false);
    }

    public HttpTaskManager(int port, boolean load) {
        super(null);
        gson = Managers.getGson();
        client = new KVTaskClient(port);
        if (load) {
            load();
        }
    }

    public void load() {
        try {
            Type collectionType = new TypeToken<Collection<Task>>() {
            }.getType();
            Collection<Task> taskList = gson.fromJson(client.load("tasks"), collectionType);
            addTasks(taskList);

            collectionType = new TypeToken<Collection<Epic>>() {
            }.getType();
            Collection<Epic> epicList = gson.fromJson(client.load("epics"), collectionType);
            addTasks(epicList);

            collectionType = new TypeToken<Collection<Epic>>() {
            }.getType();
            Collection<Subtask> subtaskList = gson.fromJson(client.load("subtasks"), collectionType);
            addTasks(subtaskList);

            collectionType = new TypeToken<Collection<Epic>>() {
            }.getType();
            Collection<Task> historyList = gson.fromJson(client.load("history"), collectionType);
            for (Task task : historyList) {
                inMemoryHistoryManager.add(task);
            }
        } catch (Exception e) {
            System.out.println("Ошибка!" + e.getMessage());
        }
    }

    private void addTasks(Collection<? extends Task> tasks) {
        int maxId = 0;
        for (Task task : tasks) {
            if (maxId < task.getId()) {
                maxId = task.getId();
            }
            TaskType taskType = task.getClassType();
            switch (taskType) {
                case EPIC -> this.epics.put(task.getId(), (Epic) task);
                case SUBTASK -> {
                    this.subtasks.put(task.getId(), (Subtask) task);
                    this.epics.get(((Subtask) task).getEpicId()).addSubTasksId(task.getId());
                    this.prioritizedTasks.add(task);
                }
                case TASK -> {
                    this.tasks.put(task.getId(), task);
                    this.prioritizedTasks.add(task);
                }
            }
            genId = maxId;
        }
    }

    @Override
    protected void save() {
        String tasksInJson = gson.toJson(tasks.values());
        client.put("tasks", tasksInJson);
        String epicsInJson = gson.toJson(epics.values());
        client.put("epics", epicsInJson);
        String subtasksInJson = gson.toJson(subtasks.values());
        client.put("subtasks", subtasksInJson);
        String historyInJson = gson.toJson(getHistory());
        client.put("history", historyInJson);
    }
}
