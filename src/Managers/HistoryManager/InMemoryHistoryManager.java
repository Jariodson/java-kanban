package Managers.HistoryManager;

import Tasks.Task;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> lastTenTasks = new ArrayList<>();
    private static final int COUNT_OF_TASKS = 10;
    private static final int FIRST_TASK = 0;

    @Override
    public void add(Task task) {
        if (task == null){
            return;
        }
        lastTenTasks.add(task);
        if (lastTenTasks.size() > COUNT_OF_TASKS) {
            lastTenTasks.remove(FIRST_TASK);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(lastTenTasks);
    }
}
