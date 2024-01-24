import Managers.Files.FileBackedTasksManager;
import Managers.TaskManager.TaskManager;
import Tasks.Enums.Statuses;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class TimeTest {
    private TaskManager taskManager;
    private final static File nameOfFile = new File("resources/testTasks.csv");
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");

    @BeforeEach
    void beforeEach() {
        taskManager = new FileBackedTasksManager(nameOfFile);
    }

    @Test
    void createNewTasks() {
        int task1Id = taskManager.createTask(new Task("Task1", "Описание таски",
                Statuses.NEW, 20, "23.01.2024;16:04"));

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class,
                () -> taskManager.createTask(new Task("Task2", "Описание таски",
                        Statuses.NEW, 20, "23.01.2024;16:14"))
        );
        Assertions.assertEquals("Нельзя делать задачи одновременно!", ex.getMessage());

        int task3Id = taskManager.createTask(new Task("Task3", "Описание таски",
                Statuses.NEW));

        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.getStartTime() + " " + task);
        }
    }

    @Test
    void createEpicWithoutSubtasks() {
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Statuses.NEW));
        System.out.println(taskManager.getEpicById(epic1Id).getEndTime());
    }

    @Test
    void createEpicWithSubtasks() {
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Statuses.NEW));
        int subtask1Id = taskManager.createSubtask(
                new Subtask("Subtask1", "Описание сабтаски", Statuses.NEW, epic1Id,
                        10, "23.01.2024;16:04")
        );
        int subtask2Id = taskManager.createSubtask(
                new Subtask("Subtask2", "Описание сабтаски", Statuses.IN_PROGRESS, epic1Id,
                        20, "23.01.2024;17:04")
        );
        System.out.println(taskManager.getEpicById(epic1Id).getEndTime());
    }

    @Test
    void getPrioritizedTasks(){
        int task1Id = taskManager.createTask(new Task("Task1", "Описание таски",
                Statuses.NEW, 20, "23.01.2024;16:04"));
        int task3Id = taskManager.createTask(new Task("Task3", "Описание таски",
                Statuses.NEW));
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Statuses.NEW));
        int subtask1Id = taskManager.createSubtask(
                new Subtask("Subtask1", "Описание сабтаски", Statuses.NEW, epic1Id,
                        10, "23.01.2024;16:25")
        );
        int subtask2Id = taskManager.createSubtask(
                new Subtask("Subtask2", "Описание сабтаски", Statuses.IN_PROGRESS, epic1Id,
                        20, "23.01.2024;17:04")
        );
    }
}
