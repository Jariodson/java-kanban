package Tasks;

import Tasks.Enums.Status;
import Tasks.Enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");
    protected final static ZoneId zoneId = ZoneId.of("Europe/Moscow");
    private final static TaskType type = TaskType.TASK;
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected int duration;
    protected ZonedDateTime startTime;

    public Task(String name, String description, Status status, int duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime, formatter).atZone(zoneId);
    }

    public Task(Integer id, String name, Status status, String description, String startTime, int duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        if (startTime.equals("null")) {
            this.startTime = null;
        } else {
            this.startTime = LocalDateTime.parse(startTime, formatter).atZone(zoneId);
        }
        this.duration = duration;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public TaskType getClassType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null)
            return null;
        return startTime.toLocalDateTime();
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(Duration.ofMinutes(duration)).toLocalDateTime();
    }
}
