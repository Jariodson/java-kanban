package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager implements ManagerInterface {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int genId = 0;

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
            epic.getSubTasksIds().clear();
            epic.setStatus("NEW");
        }
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
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
        epics.get(subtask.getEpicId()).addSubTasksIds(id);
        updateEpicStatus(subtask.getEpicId());
        return id;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        ArrayList<Integer> subsIds = epics.get(id).getSubTasksIds();
        for (int subId : subsIds){
            subtasks.remove(subId);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubTasksIds().remove(id);
        subtasks.remove(id);
        updateEpicStatus(epic.getId());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasksById(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubTasksIds()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }
        return epicSubtasks;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<String> subStatuses = new ArrayList<>();
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus("NEW");
        }
        for (int id : epic.getSubTasksIds()) {
            Subtask subtask = subtasks.get(id);
            subStatuses.add(subtask.getStatus());
        }
        for (String status : subStatuses) {
            if (status.equals(subStatuses.get(0)) && status.equals("DONE")) {
                epic.setStatus(status);
            } else if (status.equals(subStatuses.get(0)) && status.equals("NEW")) {
                epic.setStatus(status);
            } else {
                epic.setStatus("IN_PROGRESS");
                break;
            }
        }
    }
}
