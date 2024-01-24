package Managers;

import Managers.Files.CSVFormat;
import Managers.Files.FileBackedTasksManager;
import Managers.HistoryManager.HistoryManager;
import Managers.HistoryManager.InMemoryHistoryManager;
import Managers.TaskManager.TaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("resources/tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
