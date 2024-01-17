package Managers;

import Managers.Files.CSVFormat;
import Managers.Files.FileBackedTasksManager;
import Managers.HistoryManager.HistoryManager;
import Managers.HistoryManager.InMemoryHistoryManager;
import Managers.TaskManager.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager(CSVFormat.getFILE());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
