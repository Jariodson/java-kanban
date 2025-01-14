import Managers.Exceptions.ManagerSaveException;
import Managers.Files.FileBackedTasksManager;
import Tasks.Enums.Status;
import Tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final static File nameOfFile = new File("resources/testTasks.csv");

    @BeforeEach
    void beforeEach() {
        taskManager = new FileBackedTasksManager(nameOfFile);
        initTasks();
    }

    @Test
    void loadFromFileShouldThrowExceptionWhenFileIsEmptyTest() {
        Assertions.assertTrue(nameOfFile.delete());
        taskManager = new FileBackedTasksManager(nameOfFile);
        ManagerSaveException ex = Assertions.assertThrows(ManagerSaveException.class,
                () -> FileBackedTasksManager.loadFromFile(nameOfFile));
        assertEquals("Ошибка чтения файла", ex.getMessage());
    }

    @Test
    void loadFromFileShouldReturnEpicSubtasksIdsFromFileWhenListOfEpicsIsWithoutSubtasksTest() {
        Assertions.assertTrue(nameOfFile.delete());
        taskManager = new FileBackedTasksManager(nameOfFile);
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Status.NEW));
        int epic2Id = taskManager.createEpic(new Epic("Epic2", "Описание эпика", Status.NEW));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(nameOfFile);
        assertEquals(0, taskManager.getEpicById(epic1Id).getSubTasksId().size());
        assertEquals(0, taskManager.getEpicById(epic2Id).getSubTasksId().size());
    }

    @Test
    void loadFromFileShouldReturnEmptyHistoryFromFileWhenListOfHistoryIsEmptyTest() {
        Assertions.assertTrue(nameOfFile.delete());
        taskManager = new FileBackedTasksManager(nameOfFile);
        int epic1Id = taskManager.createEpic(new Epic("Epic1", "Описание эпика", Status.NEW));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(nameOfFile);
        assertEquals(0, fileBackedTasksManager.getHistory().size());
    }
}
