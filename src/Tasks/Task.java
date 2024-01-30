package Tasks;

import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Task {
    protected String name;
    private final static TaskTypes type = TaskTypes.TASK;
    protected String description;
    protected int id;
    protected Statuses status;
    protected int duration;
    protected final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");
    protected final static ZoneId zoneId = ZoneId.of("Europe/Moscow");

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

    protected ZonedDateTime startTime;

    public Task(String name, String description, Statuses status, int duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime, formatter).atZone(zoneId);
    }

    public Task(Integer id, String name, Statuses status, String description, String startTime, int duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        if (startTime.equals("null")) {
            this.startTime = null;
        }
        else {
            this.startTime = LocalDateTime.parse(startTime, formatter).atZone(zoneId);
        }
        this.duration = duration;
    }

    public Task(String name, String description, Statuses status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public TaskTypes getClassType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
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
