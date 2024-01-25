package Managers.Files;

import Managers.HistoryManager.HistoryManager;
import Tasks.*;
import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CSVFormat {

    public String toString(Task task) {
        if (task.getClassType() == TaskTypes.SUBTASK) {
            return String.join(",", task.getId().toString(),
                    task.getClassType().toString(),
                    task.getName(),
                    task.getStatus().toString(),
                    task.getDescription(),
                    Objects.requireNonNullElse(task.getStartTime(), "null").toString(),
                    task.getDuration().toString(),
                    Objects.requireNonNullElse(task.getEndTime(), "null").toString(),
                    ((Subtask) task).getEpicId().toString()
            );
        } else {
            return String.join(",", task.getId().toString(),
                    task.getClassType().toString(),
                    task.getName(),
                    task.getStatus().toString(),
                    task.getDescription(),
                    Objects.requireNonNullElse(task.getStartTime(), "null").toString(),
                    task.getDuration().toString(),
                    Objects.requireNonNullElse(task.getEndTime(), "null").toString()
            );
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
        switch (type) {
            case SUBTASK:
                return new Subtask(
                        Integer.parseInt(task[0]),
                        task[2],
                        Statuses.valueOf(task[3]),
                        task[4],
                        task[5],
                        Integer.parseInt(task[6]),
                        Integer.parseInt(task[8])
                );
            case EPIC:
                return new Epic(
                        Integer.parseInt(task[0]),
                        task[2],
                        Statuses.valueOf(task[3]),
                        task[4],
                        task[5],
                        Integer.parseInt(task[6])
                );
            default:
                return new Task(
                        Integer.parseInt(task[0]),
                        task[2],
                        Statuses.valueOf(task[3]),
                        task[4],
                        task[5],
                        Integer.parseInt(task[6])
                );
        }
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
        return "id,type,name,status,description,startTime,duration,endTime,epicId\n";
    }
}
