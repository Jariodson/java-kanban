import Managers.TaskManager.InMemoryTaskManager;
import Tasks.Enums.Statuses;
import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private Epic epic;
    @BeforeEach
    void beforeEach(){
        taskManager = new InMemoryTaskManager();
        initTasks();
    }

    @Test
    void getEpicStatusIfListOfSubtasksEmpty() {
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Statuses.NEW));
        assertNotNull(taskManager.getEpics());
        assertEquals(0, epic.getSubTasksId().size());
        assertEquals(Statuses.NEW, epic.getStatus());
    }

    @Test
    void getEpicStatusIfListOfSubtasksWithStatusesNEW() {
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Statuses.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Statuses.NEW, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Statuses.NEW, epic.getId()));
        assertEquals(Statuses.NEW, epic.getStatus());
    }

    @Test
    void getEpicStatusIfListOfSubtasksWithStatusesDone(){
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Statuses.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Statuses.DONE, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Statuses.DONE, epic.getId()));
        assertEquals(Statuses.DONE, epic.getStatus());
    }

    @Test
    void getEpicStatusIfListOfSubtasksWithStatusesNewAndDone(){
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Statuses.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Statuses.NEW, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Statuses.DONE, epic.getId()));
        assertEquals(Statuses.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void getEpicStatusIfListOfSubtasksWithStatusesInProgress(){
        taskManager.createEpic(epic = new Epic("Epic", "Описание Эпика", Statuses.NEW));
        taskManager.createSubtask(new Subtask("Subtask1", "Описание 1-го сабтаска", Statuses.IN_PROGRESS, epic.getId()));
        taskManager.createSubtask(new Subtask("Subtask2", "Описание 2-го сабтаска", Statuses.IN_PROGRESS, epic.getId()));
        assertEquals(Statuses.IN_PROGRESS, epic.getStatus());
    }
}