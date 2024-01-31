import Managers.TaskManager.InMemoryTaskManager;
import Tasks.Enums.Status;
import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private Epic epic;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        initTasks();
    }

    @Test
    void getEpicStatusShouldReturnEpicStatusNewIfListOfSubtasksEmptyTest() {
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Status.NEW));
        assertNotNull(taskManager.getEpics());
        assertEquals(0, epic.getSubTasksId().size());
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void getEpicStatusShouldReturnEpicStatusNewIfListOfSubtasksWithStatusesNewTest() {
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Status.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Status.NEW, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Status.NEW, epic.getId()));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void getEpicStatusShouldReturnEpicStatusDoneIfListOfSubtasksWithStatusesDoneTest() {
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Status.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Status.DONE, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Status.DONE, epic.getId()));
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void getEpicStatusShouldReturnEpicStatusInProgressIfListOfSubtasksWithStatusesNewAndDone() {
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Status.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Status.NEW, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Status.DONE, epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void getEpicStatusShouldReturnEpicStatusInProgressIfListOfSubtasksStatusesEqualsToEpicStatusInProgressTest() {
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Status.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Status.IN_PROGRESS, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Status.IN_PROGRESS, epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}