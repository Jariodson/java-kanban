import Managers.Files.FileBackedTasksManager;
import Tasks.Epic;
import Tasks.Enums.Statuses;
import Tasks.Subtask;
import Tasks.Task;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        //TaskManager taskManager = Managers.getDefault();
        File file = new File("resources/tasks.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        final int task1Id = fileBackedTasksManager.createTask(
                new Task("Task1-1", "Первая таска", Statuses.NEW));
        final int epic1Id = fileBackedTasksManager.createEpic(
                new Epic("Epic1-1", "Первый эпик", Statuses.NEW));
        final int subtask1Id = fileBackedTasksManager.createSubtask(
                new Subtask("Подзадача1-1-1", "Первая подзадача первого эпика", Statuses.NEW, epic1Id));

        fileBackedTasksManager.getSubtaskById(subtask1Id);
        fileBackedTasksManager.getEpicById(epic1Id);

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(file);
        System.out.println("Из файла");
        System.out.println(fileBackedTasksManager1.getHistory());
        System.out.println();

        final int epic2Id = fileBackedTasksManager1.createEpic(
                new Epic("Epic1-2", "Второй эпик", Statuses.NEW));

        System.out.println(fileBackedTasksManager1.getTasks());
        System.out.println(fileBackedTasksManager1.getEpics());
        System.out.println(fileBackedTasksManager1.getSubtasks());
    }
}
