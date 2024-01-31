import Managers.HistoryManager.HistoryManager;
import Managers.Managers;
import Managers.TaskManager.TaskManager;
import Tasks.Enums.Status;
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
    int epic2Id;
    private int task1Id;
    private int task2Id;
    private int epic1Id;
    private int subtask1Id;
    private int subtask2Id;

    protected void initTasks() {
        task1Id = taskManager.createTask(task1 = new Task("Task1", "Описание таски", Status.NEW));
        task2Id = taskManager.createTask(new Task("Task2", "Описание таски", Status.NEW));

        epic1Id = taskManager.createEpic(epic1 = new Epic("Epic1", "Описание эпика", Status.NEW));
        epic2Id = taskManager.createEpic(new Epic("Epic2", "Описание эпика", Status.NEW));

        subtask1Id = taskManager.createSubtask(subtask1 = new Subtask("Subtask1", "Описание сабтаски", Status.NEW, epic1Id));
        subtask2Id = taskManager.createSubtask(new Subtask("Subtask2", "Описание сабтаски", Status.NEW, epic1Id));
    }

    @Test
    void getHistoryShouldReturnHistoryTest() {
        final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        assertEquals(java.util.Collections.emptyList(), inMemoryHistoryManager.getHistory());
    }

    @Test
    void getTasksShouldReturnTasksTest() {
        assertNotNull(taskManager.getTasks(), "Список задач пуст.");
        assertEquals(List.of(task1Id, task2Id), taskManager.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList())
        );
    }

    @Test
    void getEpicsShouldReturnEpicsTest() {
        assertNotNull(taskManager.getEpics(), "Список эпиков пуст.");
        assertEquals(List.of(epic1Id, epic2Id), taskManager.getEpics().stream()
                .map(Epic::getId)
                .collect(Collectors.toList())
        );
    }

    @Test
    void getSubtasksShouldReturnSubtasksTest() {
        assertNotNull(taskManager.getTasks(), "Список сабтасков пуст.");
        assertEquals(List.of(subtask1Id, subtask2Id), taskManager.getSubtasks().stream()
                .map(Subtask::getId)
                .collect(Collectors.toList())
        );
    }

    @Test
    void deleteTasksShouldDeleteTasksTest() {
        taskManager.deleteTasks();
        assertEquals(java.util.Collections.emptyList(), taskManager.getTasks());
    }

    @Test
    void deleteEpicsShouldDeleteEpicsTest() {
        taskManager.deleteEpics();
        assertEquals(java.util.Collections.emptyList(), taskManager.getEpics());
    }

    @Test
    void deleteSubtasksShouldDeleteSubtasksTest() {
        taskManager.deleteSubtasks();
        assertEquals(java.util.Collections.emptyList(), taskManager.getSubtasks());
    }

    @Test
    void getTaskByIdShouldReturnTaskIfTaskIdIsExistTest() {
        assertNotNull(taskManager.getTaskById(task1Id));
        assertEquals(task1, taskManager.getTaskById(task1Id));
    }

    @Test
    void getEpicByIdShouldReturnEpicIfEpicIdIsExistTest() {
        assertNotNull(taskManager.getEpicById(epic1Id));
        assertEquals(epic1, taskManager.getEpicById(epic1Id));
    }

    @Test
    void getSubtaskByIdShouldReturnSubtaskIfSubtaskIdIsExistTest() {
        assertNotNull(taskManager.getSubtaskById(subtask1Id));
        assertNotNull(taskManager.getEpicById(taskManager.getSubtaskById(subtask1Id).getEpicId()));
        assertEquals(subtask1, taskManager.getSubtaskById(subtask1Id));
    }

    @Test
    void createTaskShouldReturnNewTaskTest() {
        Task newTask = new Task("NewTask", "Discription", Status.NEW);
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
    void createEpicShouldReturnNewEpicTest() {
        Epic newEpic = new Epic("NewEpic", "Discription", Status.NEW);
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
    void createSubtaskShouldReturnNewSubtaskTest() {
        Subtask newSubtask = new Subtask("NewSubtask", "Discriprion", Status.NEW, epic1Id);
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
    void updateTaskShouldReturnNewTaskAfterUpdateTest() {
        System.out.println(taskManager.getTasks());
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        System.out.println("Обновление задачи");
        System.out.println(taskManager.getTasks());
    }

    @Test
    void updateEpicShouldReturnNewEpicAfterUpdateTest() {
        System.out.println(taskManager.getEpics());
        epic1.setStatus(Status.DONE);
        taskManager.updateEpic(epic1);
        System.out.println("Обновление эпика");
        System.out.println(taskManager.getEpics());
    }

    @Test
    void updateSubtaskShouldReturnNewSubtaskAfterUpdateTest() {
    }

    @Test
    void deleteTaskByIdShouldReturnFalseIfTaskAfterDeleteIsExistTest() {
        taskManager.deleteTaskById(task1Id);
        Assertions.assertFalse(taskManager.getTasks().contains(task1));
    }

    @Test
    void deleteEpicByIdShouldReturnFalseIfEpicAfterDeleteIsExistTest() {
        taskManager.deleteEpicById(epic1Id);
        Assertions.assertFalse(taskManager.getEpics().contains(epic1));
    }

    @Test
    void deleteSubtaskByIdShouldReturnFalseIfSubtaskAfterDeleteIsExistTest() {
        taskManager.deleteSubtaskById(subtask1Id);
        Assertions.assertFalse(taskManager.getSubtasks().contains(subtask1));
    }

    @Test
    void getEpicSubtasksByIdShouldReturnAllIdsOfEpicsSubtasksTest() {
    }
}