package Manager.HistoryManager;

import Manager.Managers;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> lastTenTasks = new ArrayList<>();
    @Override
    public void add(Task task) {
        lastTenTasks.add(task);
        if (lastTenTasks.size() > 10){
            lastTenTasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return lastTenTasks;
    }
}
