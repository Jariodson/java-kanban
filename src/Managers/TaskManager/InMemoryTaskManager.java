package Managers.TaskManager;

import Managers.HistoryManager.HistoryManager;
import Managers.Managers;
import Tasks.Enums.TaskTypes;
import Tasks.Epic;
import Tasks.Enums.Statuses;
import Tasks.Subtask;
import Tasks.Task;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    protected int genId = 0;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            inMemoryHistoryManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            inMemoryHistoryManager.remove(epic.getId());
            tasks.remove(epic.getId());
            for (Integer subtaskId : epic.getSubTasksId()) {
                inMemoryHistoryManager.remove(subtaskId);
            }
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
        }
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
        addToPrioritizedTasks(task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = ++genId;
        epic.setId(id);
        epics.put(id, epic);
        addToPrioritizedTasks(epic);
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = ++genId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).addSubTasksId(id);
        epics.get(subtask.getEpicId()).addSubTasksDuration(subtask.getDuration());
        updateEpicStatus(subtask.getEpicId());
        setEpicTime(epics.get(subtask.getEpicId()));
        addToPrioritizedTasks(subtask);
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
        if (subtasks.containsKey(subtask.getId()) || epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            inMemoryHistoryManager.remove(taskId);
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            List<Integer> subsIds = epics.get(epicId).getSubTasksId();
            for (int subId : subsIds) {
                subtasks.remove(subId);
                inMemoryHistoryManager.remove(subId);
            }
            inMemoryHistoryManager.remove(epicId);
            epics.remove(epicId);
        }
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
            for (int i = 0; i < epic.getSubTasksId().size(); i++) {
                if (epic.getSubTasksId().get(i).equals(subtaskId)) {
                    epic.getSubTasksId().remove(i);
                    break;
                }
            }
            subtasks.remove(subtaskId);
            updateEpicStatus(epic.getId());
            inMemoryHistoryManager.remove(subtaskId);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasksById(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubTasksId()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }
        return epicSubtasks;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void setEpicTime(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Integer id : epic.getSubTasksId()) {
            subtaskList.add(subtasks.get(id));
        }
        subtaskList.sort((s1, s2) -> s2.getEndTime().compareTo(s1.getEndTime()));
        epic.setEndTime(subtaskList.get(0).getEndTime());
        epic.setStartTime(subtaskList.get(subtaskList.size() - 1).getStartTime());
    }

    private void addToPrioritizedTasks(Task task) {
        if (task.getStartTime().equals("null") || prioritizedTasks.isEmpty()) {
            prioritizedTasks.add(task);
            return;
        }
        LocalDateTime localDateTimeTaskStartTime = LocalDateTime.parse(task.getStartTime(), formatter);
        LocalDateTime localDateTimeTaskEndTime = LocalDateTime.parse(task.getEndTime(), formatter);
        for (Task task1 : prioritizedTasks) {
            if (task1.getClassType().equals(TaskTypes.EPIC)) {
                prioritizedTasks.add(task);
                return;
            }
            LocalDateTime localDateTimeStartTimeInTasks = LocalDateTime.parse(task1.getStartTime(), formatter);
            LocalDateTime localDateTimeTaskEndTimeInTasks = LocalDateTime.parse(task1.getEndTime(), formatter);
            if (localDateTimeTaskEndTime.isBefore(localDateTimeStartTimeInTasks)) { //если задача закончена до начала другой
                prioritizedTasks.add(task);
                return;
            } else if (localDateTimeTaskStartTime.isAfter(localDateTimeTaskEndTimeInTasks)) { //если задача начата после окончания другой
                prioritizedTasks.add(task);
                return;
            } else {
                throw new IllegalStateException("Нельзя делать задачи одновременно!");
            }
        }
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
