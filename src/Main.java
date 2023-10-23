import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Task1-1", "Первая задача", "NEW");
        final int task1Id = manager.createTask(task1);
        ArrayList<Task> tasks = manager.getTasks();
        System.out.println(tasks);
        System.out.println();

        Epic epic1 = new Epic("Epic1-1", "Первый эпик", "NEW");
        Epic epic2 = new Epic("Epic1-2", "Второй эпик", "NEW");
        final int epic1Id = manager.createEpic(epic1);
        final int epic2Id = manager.createEpic(epic2);
        ArrayList<Epic> epics = manager.getEpics();

        System.out.println(epics);

        Subtask subtask1 = new Subtask("Подзадача1-1-1", "Первая подзадача первого эпика", "NEW", epic1Id);
        Subtask subtask2 = new Subtask("Подзадача1-1-2", "Вторая подзадача первого эпика", "NEW", epic1Id);
        Subtask subtask3 = new Subtask("Подзадача1-2-1", "Третья подзадача второго эпика", "NEW", epic2Id);
        final int subtask1Id = manager.createSubtask(subtask1);
        final int subtask2Id = manager.createSubtask(subtask2);
        final int subtask3Id = manager.createSubtask(subtask3);
        ArrayList<Subtask> subtasks = manager.getSubtasks();

        System.out.println(subtasks);
        System.out.println();

        Subtask subtask31 = new Subtask("Новая Подзадача1-2-1", "Третья подзадача второго эпика", "IN_PROGRESS", epic2Id);
        Subtask subtask21 = new Subtask("Новая Подзадача1-1-2", "Вторая подзадача первого эпика", "DONE", epic1Id);
        manager.updateSubtask(subtask31);
        manager.updateSubtask(subtask21);
        subtasks = manager.getSubtasks();

        System.out.println(epics);
        System.out.println(subtasks);
        System.out.println();

        manager.deleteTaskById(task1Id);
        tasks = manager.getTasks();
        manager.deleteEpicById(epic2Id);
        epics = manager.getEpics();

        manager.deleteSubtasks();
        subtasks = manager.getSubtasks();

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks);
    }
}
