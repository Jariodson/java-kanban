package Managers.Files;

import Managers.HistoryManager.HistoryManager;
import Tasks.*;
import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CSVFormat {
    private static final File file = new File("resources/tasks.csv");

    public static File getFILE() {
        return file;
    }

    public String toString(Task task) {
        if (task.getClassType().equals(TaskTypes.SUBTASK)) {
            return String.join(",", task.getId().toString(), task.getClassType().toString(),
                    task.getName(), task.getStatus().toString(),
                    task.getDescription(), ((Subtask) task).getEpicId().toString());
        } else {
            return String.join(",", task.getId().toString(), task.getClassType().toString(),
                    task.getName(), task.getStatus().toString(),
                    task.getDescription());
        }
    }

    static Task fromString(String value) {
        String[] task;
        if (value.isEmpty() | value.isBlank()) {
            return null;
        } else {
            task = value.split(",");
        }
        TaskTypes type = TaskTypes.valueOf(task[1]);
        if (type == TaskTypes.SUBTASK) {
            return new Subtask(Integer.parseInt(task[0]), task[2], Statuses.valueOf(task[3]), task[4], Integer.parseInt(task[5]));
        } else if (type == TaskTypes.EPIC) {
            return new Epic(Integer.parseInt(task[0]), task[2], Statuses.valueOf(task[3]), task[4]);
        }
        return new Task(Integer.parseInt(task[0]), task[2], Statuses.valueOf(task[3]), task[4]);
    }

    protected static String historyToString(HistoryManager manager) {
        List<String> taskIds = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            taskIds.add(task.getId().toString());
        }
        return String.join(",", taskIds);
    }

    protected static List<Integer> historyFromString(String value) {
        String[] ids = value.split(",");
        List<Integer> idsList = new ArrayList<>();
        for (String id : ids) {
            idsList.add(Integer.parseInt(id));
        }
        return idsList;
    }

    protected String defaultFormat() {
        return "id,type,name,status,description,epic\n";
    }
}
