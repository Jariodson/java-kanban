package Managers.Files;

import Managers.HistoryManager.HistoryManager;
import Tasks.Enums.Status;
import Tasks.Enums.TaskType;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVFormat {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");

    static Task fromString(String value) {
        String[] task;
        if (value.isEmpty() | value.isBlank()) {
            return null;
        } else {
            task = value.split(",");
        }
        TaskType type = TaskType.valueOf(task[1]);
        switch (type) {
            case SUBTASK:
                return new Subtask(
                        Integer.parseInt(task[0]),
                        task[2],
                        Status.valueOf(task[3]),
                        task[4],
                        task[5],
                        Integer.parseInt(task[6]),
                        Integer.parseInt(task[8])
                );
            case EPIC:
                return new Epic(
                        Integer.parseInt(task[0]),
                        task[2],
                        Status.valueOf(task[3]),
                        task[4],
                        task[5],
                        Integer.parseInt(task[6])
                );
            default:
                return new Task(
                        Integer.parseInt(task[0]),
                        task[2],
                        Status.valueOf(task[3]),
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

    public String toString(Task task) {
        if (task.getClassType() == TaskType.SUBTASK) {
            return String.join(",", task.getId().toString(),
                    task.getClassType().toString(),
                    task.getName(),
                    task.getStatus().toString(),
                    task.getDescription(),
                    timeToString(task.getStartTime()),
                    task.getDuration().toString(),
                    timeToString(task.getEndTime()),
                    ((Subtask) task).getEpicId().toString()
            );
        } else {
            return String.join(",", task.getId().toString(),
                    task.getClassType().toString(),
                    task.getName(),
                    task.getStatus().toString(),
                    task.getDescription(),
                    timeToString(task.getStartTime()),
                    task.getDuration().toString(),
                    timeToString(task.getEndTime())
            );
        }
    }

    private String timeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "null";
        } else {
            return localDateTime.format(formatter);
        }
    }

    protected String defaultFormat() {
        return "id,type,name,status,description,startTime,duration,endTime,epicId\n";
    }
}
