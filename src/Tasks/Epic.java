package Tasks;

import Tasks.Enums.Status;
import Tasks.Enums.TaskType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private static final TaskType type = TaskType.EPIC;
    private final List<Integer> subTasksIds;
    private ZonedDateTime endTime;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public Epic(Integer id, String name, Status status, String description, String startTime, int duration) {
        super(id, name, status, description, startTime, duration);
        subTasksIds = new ArrayList<>();
    }

    @Override
    public TaskType getClassType() {
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                "subTasksIds=" + subTasksIds +
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
