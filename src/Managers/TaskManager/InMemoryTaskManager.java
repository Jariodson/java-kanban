package Managers.TaskManager;

import Managers.HistoryManager.InMemoryHistoryManager;
import Managers.Managers;
import Tasks.Epic;
import Tasks.Statuses;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    private int genId = 0;

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksId().clear();
            epic.setStatus(Statuses.NEW);
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        inMemoryHistoryManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        inMemoryHistoryManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        inMemoryHistoryManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public int createTask(Task task) {
        int id = ++genId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = ++genId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = ++genId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).addSubTasksId(id);
        updateEpicStatus(subtask.getEpicId());
        return id;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId) {
        ArrayList<Integer> subsIds = epics.get(epicId).getSubTasksId();
        for (int subId : subsIds) {
            subtasks.remove(subId);
        }
        epics.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
        epic.getSubTasksId().remove(subtaskId);
        subtasks.remove(subtaskId);
        updateEpicStatus(epic.getId());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasksById(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubTasksId()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }
        return epicSubtasks;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Statuses> subStatuses = new ArrayList<>();
        if (epic.getSubTasksId().isEmpty()) {
            epic.setStatus(Statuses.NEW);
        }
        for (int id : epic.getSubTasksId()) {
            Subtask subtask = subtasks.get(id);
            subStatuses.add(subtask.getStatus());
        }
        for (Statuses status : subStatuses) {
            if (status.equals(subStatuses.get(0)) && status.equals(Statuses.DONE)) {
                epic.setStatus(status);
            } else if (status.equals(subStatuses.get(0)) && status.equals(Statuses.NEW)) {
                epic.setStatus(status);
            } else {
                epic.setStatus(Statuses.IN_PROGRESS);
                break;
            }
        }
    }
}
