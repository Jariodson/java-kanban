import Managers.Files.FileBackedTasksManager;
import Managers.TaskManager.TaskManager;
import Tasks.Enums.Status;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeTest {
    private final static File nameOfFile = new File("resources/testTasks.csv");
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new FileBackedTasksManager(nameOfFile);
    }

    @Test
    void testShouldReturnNewTasksWithPriorByStartTimeFromPrioritizedList() {
        int task1Id = taskManager.createTask(new Task("Task1", "Описание таски",
                Status.NEW, 20, "23.01.2024;16:04"));

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class,
                () -> taskManager.createTask(new Task("Task2", "Описание таски",
                        Status.NEW, 20, "23.01.2024;16:14"))
        );
        Assertions.assertEquals("Нельзя делать задачи одновременно!", ex.getMessage());

        int task3Id = taskManager.createTask(new Task("Task3", "Описание таски",
                Status.NEW));

        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.getStartTime() + " " + task);
        }
    }

    @Test
    void testShouldReturnEpicEndTimeIfListOfEpicSubtasksIsEmpty() { //createEpicWithoutSubtasks
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Status.NEW));
        System.out.println(taskManager.getEpicById(epic1Id).getEndTime());
    }

    @Test
    void testShouldReturnEpicEndTimeIfEpicHaveSubtasks() {
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Status.NEW));
        int subtask1Id = taskManager.createSubtask(
                new Subtask("Subtask1", "Описание сабтаски", Status.NEW, epic1Id,
                        10, "23.01.2024;16:04")
        );
        int subtask2Id = taskManager.createSubtask(
                new Subtask("Subtask2", "Описание сабтаски", Status.IN_PROGRESS, epic1Id,
                        20, "23.01.2024;17:04")
        );
        System.out.println(taskManager.getEpicById(epic1Id).getEndTime());
    }

    @Test
    void testShouldReturnPrioritizedTasksFromFile() {
        int task1Id = taskManager.createTask(new Task("Task1", "Описание таски",
                Status.NEW, 20, "23.01.2024;16:04"));
        int task3Id = taskManager.createTask(new Task("Task3", "Описание таски",
                Status.NEW));
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Status.NEW));
        int subtask1Id = taskManager.createSubtask(
                new Subtask("Subtask1", "Описание сабтаски", Status.NEW, epic1Id,
                        10, "23.01.2024;16:25")
        );
        int subtask2Id = taskManager.createSubtask(
                new Subtask("Subtask2", "Описание сабтаски", Status.IN_PROGRESS, epic1Id,
                        20, "23.01.2024;17:04")
        );
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }

    @Test
    void getPrioritizeFromFiledTest() {
        assertTrue(nameOfFile.delete());
        int task1Id = taskManager.createTask(new Task("Task1", "Описание таски",
                Status.NEW, 20, "23.01.2024;16:04"));
        int task3Id = taskManager.createTask(new Task("Task3", "Описание таски",
                Status.NEW));
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Status.NEW));
        int subtask1Id = taskManager.createSubtask(
                new Subtask("Subtask1", "Описание сабтаски", Status.NEW, epic1Id,
                        10, "23.01.2024;16:25")
        );
        int subtask2Id = taskManager.createSubtask(
                new Subtask("Subtask2", "Описание сабтаски", Status.IN_PROGRESS, epic1Id,
                        20, "23.01.2024;17:04")
        );
        taskManager.getEpicById(epic1Id);
        taskManager.getSubtaskById(subtask1Id);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(nameOfFile);
        for (Task task : fileBackedTasksManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}
