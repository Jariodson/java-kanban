import Managers.HistoryManager.HistoryManager;
import Managers.Managers;
import Tasks.Enums.Statuses;
import Tasks.Epic;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private HistoryManager historyManager;
    private Task task;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("Task1", "Discription", Statuses.NEW);
        task.setId(1);
    }

    @Test
    void addShouldAddTaskToHistoryListTest() {
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class,
                () -> historyManager.add(null));
        assertEquals("Пустая задача.", ex.getMessage());

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, historyManager.getHistory().size());

        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void removeShouldRemoveTaskFromHistoryTest() {
        Epic epic1 = new Epic("Epic1", "Описание эпика", Statuses.NEW);
        Epic epic2 = new Epic("Epic2", "Описание эпика", Statuses.NEW);
        epic1.setId(2);
        epic2.setId(3);

        historyManager.add(task);
        assertNotNull(historyManager.getHistory());
        historyManager.remove(task.getId());

        historyManager.add(task);
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertEquals(0, historyManager.getHistory().size());

        historyManager.add(task);
        historyManager.add(epic1);
        historyManager.add(epic2);
        assertNotNull(historyManager.getHistory());
        assertEquals(3, historyManager.getHistory().size());
        historyManager.remove(epic2.getId());
        assertEquals(2, historyManager.getHistory().size());

        historyManager.remove(task.getId());
        assertEquals(1, historyManager.getHistory().size());

    }

    @Test
    void getHistoryShouldReturnHistoryListTest() {
        historyManager.add(task);
        assertNotNull(historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());

    }
}