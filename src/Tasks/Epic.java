package Tasks;

import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

import java.time.*;
import java.util.*;

public class Epic extends Task {
    private final List<Integer> subTasksIds;
    private static final TaskTypes type = TaskTypes.EPIC;
    private ZonedDateTime endTime;

    public Epic(String name, String description, Statuses status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public Epic(Integer id, String name, Statuses status, String description, String startTime, int duration) {
        super(id, name, status, description, startTime, duration);
        subTasksIds = new ArrayList<>();
    }

    @Override
    public TaskTypes getClassType() {
        return type;
    }

    public List<Integer> getSubTasksId() {
        return subTasksIds;
    }

    public void addSubTasksDuration(int duration) {
        this.duration += duration;
    }

    public void addSubTasksId(int id) {
        subTasksIds.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subTasksIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        if (endTime == null)
            return null;
        return endTime.toLocalDateTime();
    }

    public void setEndTime(LocalDateTime endTime) {
        if (endTime == null)
            return;
        this.endTime = endTime.atZone(zoneId);
    }

    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null)
            return;
        this.startTime = startTime.atZone(zoneId);
    }
}
