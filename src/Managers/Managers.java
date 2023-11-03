package Manager;

import Manager.HistoryManager.HistoryManager;
import Manager.HistoryManager.InMemoryHistoryManager;
import Manager.TaskManager.InMemoryTaskManager;
import Manager.TaskManager.TaskManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager;
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager;
    }
}
