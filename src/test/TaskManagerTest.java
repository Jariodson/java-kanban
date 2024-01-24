import Managers.HistoryManager.HistoryManager;
import Managers.Managers;
import Managers.TaskManager.TaskManager;
import Tasks.Enums.Statuses;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task1;
    protected Epic epic1;
    protected Subtask subtask1;

    private int task1Id;
    private int task2Id;
    private int epic1Id;
    int epic2Id;
    private int subtask1Id;
    private int subtask2Id;

    protected void initTasks() {
        task1Id = taskManager.createTask(task1 = new Task("Task1", "Описание таски", Statuses.NEW));
        task2Id = taskManager.createTask(new Task("Task2", "Описание таски", Statuses.NEW));

        epic1Id = taskManager.createEpic(epic1 = new Epic("Epic1", "Описание эпика", Statuses.NEW));
        epic2Id = taskManager.createEpic(new Epic("Epic2", "Описание эпика", Statuses.NEW));

        subtask1Id = taskManager.createSubtask(subtask1 = new Subtask("Subtask1", "Описание сабтаски", Statuses.NEW, epic1Id));
        subtask2Id = taskManager.createSubtask(new Subtask("Subtask2", "Описание сабтаски", Statuses.NEW, epic1Id));
    }

    @Test
    void getHistory() {
        final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        assertEquals(java.util.Collections.emptyList(), inMemoryHistoryManager.getHistory());
    }

    @Test
    void getTasks() {
        assertNotNull(taskManager.getTasks(), "Список задач пуст.");
        assertEquals(List.of(task1Id, task2Id), taskManager.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList())
        );
    }

    @Test
    void getEpics() {
        assertNotNull(taskManager.getEpics(), "Список эпиков пуст.");
        assertEquals(List.of(epic1Id, epic2Id), taskManager.getEpics().stream()
                .map(Epic::getId)
                .collect(Collectors.toList())
        );
    }

    @Test
    void getSubtasks() {
        assertNotNull(taskManager.getTasks(), "Список сабтасков пуст.");
        assertEquals(List.of(subtask1Id, subtask2Id), taskManager.getSubtasks().stream()
                .map(Subtask::getId)
                .collect(Collectors.toList())
        );
    }

    @Test
    void deleteTasks() {
        taskManager.deleteTasks();
        assertEquals(java.util.Collections.emptyList(), taskManager.getTasks());
    }

    @Test
    void deleteEpics() {
        taskManager.deleteEpics();
        assertEquals(java.util.Collections.emptyList(), taskManager.getEpics());
    }

    @Test
    void deleteSubtasks() {
        taskManager.deleteSubtasks();
        assertEquals(java.util.Collections.emptyList(), taskManager.getSubtasks());
    }

    @Test
    void getTaskById() {
        assertNotNull(taskManager.getTaskById(task1Id));
        assertEquals(task1, taskManager.getTaskById(task1Id));
    }

    @Test
    void getEpicById() {
        assertNotNull(taskManager.getEpicById(epic1Id));
        assertEquals(epic1, taskManager.getEpicById(epic1Id));
    }

    @Test
    void getSubtaskById() {
        assertNotNull(taskManager.getSubtaskById(subtask1Id));
        assertNotNull(taskManager.getEpicById(taskManager.getSubtaskById(subtask1Id).getEpicId()));
        assertEquals(subtask1, taskManager.getSubtaskById(subtask1Id));
    }

    @Test
    void createTask() {
        Task newTask = new Task("NewTask", "Discription", Statuses.NEW);
        final int taskId = taskManager.createTask(newTask);
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(newTask, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(newTask, tasks.get(2), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic newEpic = new Epic("NewEpic", "Discription", Statuses.NEW);
        final int epicId = taskManager.createEpic(newEpic);
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(newEpic, savedEpic, "Эпики не совпадают");

        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
        assertEquals(newEpic, epics.get(2));
    }

    @Test
    void createSubtask() {
        Subtask newSubtask = new Subtask("NewSubtask", "Discriprion", Statuses.NEW, epic1Id);
        final int subtaskId = taskManager.createSubtask(newSubtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Сабтаск не найден.");
        assertTrue(taskManager.getEpics().stream()
                .map(Epic::getId)
                .collect(Collectors.toList())
                .contains(epic1Id)
        );
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Сабтаски не возвращаются.");
        assertEquals(newSubtask, subtasks.get(2));
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void deleteTaskById() {
        taskManager.deleteTaskById(task1Id);
        Assertions.assertFalse(taskManager.getTasks().contains(task1));
    }

    @Test
    void deleteEpicById() {
        taskManager.deleteEpicById(epic1Id);
        Assertions.assertFalse(taskManager.getEpics().contains(epic1));
    }

    @Test
    void deleteSubtaskById() {
        taskManager.deleteSubtaskById(subtask1Id);
        Assertions.assertFalse(taskManager.getSubtasks().contains(subtask1));
    }

    @Test
    void getEpicSubtasksById() {
    }
}