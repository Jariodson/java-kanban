package Managers.Files;

import Managers.Exceptions.ManagerSaveException;
import Managers.TaskManager.InMemoryTaskManager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.Enums.TaskTypes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Managers.Files.CSVFormat.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected final File fileName;
    private final CSVFormat csvFormat = new CSVFormat();

    public FileBackedTasksManager(File fileName) {
        this.fileName = fileName;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public List<Subtask> getEpicSubtasksById(int epicId) {
        List<Subtask> subtasks = super.getEpicSubtasksById(epicId);
        save();
        return subtasks;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(csvFormat.defaultFormat());
            for (Task task : getTasks()) {
                bufferedWriter.write(csvFormat.toString(task) + '\n');
            }
            for (Epic epic : getEpics()) {
                bufferedWriter.write(csvFormat.toString(epic) + '\n');
            }
            for (Subtask subtask : getSubtasks()) {
                bufferedWriter.write(csvFormat.toString(subtask) + '\n');
            }
            bufferedWriter.write('\n');
            bufferedWriter.write(historyToString(inMemoryHistoryManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        Map<Integer, Task> tasks = new HashMap<>();
        int maxId = 0;
        for (int i = 1; i < lines.size() - 2; i++) {
            if (lines.get(i).isEmpty()) {
                break;
            }
            final Task task = fromString(lines.get(i));
            tasks.put(task.getId(), task);
            if (maxId < task.getId()) {
                maxId = task.getId();
            }

            TaskTypes types = task.getClassType();
            switch (types) {
                case SUBTASK:
                    fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                    fileBackedTasksManager.epics.get(((Subtask) task).getEpicId()).addSubTasksId(task.getId());
                    fileBackedTasksManager.prioritizedTasks.add(task);
                    break;
                case EPIC:
                    fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                    break;
                default:
                    fileBackedTasksManager.tasks.put(task.getId(), task);
                    fileBackedTasksManager.prioritizedTasks.add(task);
                    break;
            }
        }
        fileBackedTasksManager.genId = maxId;
        List<Integer> history = new ArrayList<>();
        if (!lines.get((lines.size()) - 1).isEmpty()) {
            history = historyFromString(lines.get((lines.size()) - 1));
        }
        for (Integer taskId : history) {
            if (tasks.containsKey(taskId)) {
                fileBackedTasksManager.inMemoryHistoryManager.add(tasks.get(taskId));
            }
        }
        return fileBackedTasksManager;
    }
}
