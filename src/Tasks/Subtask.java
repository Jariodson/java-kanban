package Tasks;

import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

public class Subtask extends Task {
    private final int epicId;
    private final static TaskTypes type = TaskTypes.SUBTASK;

    public Subtask(String name, String description, Statuses status, int epicId, int duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, Statuses status, String description, String startTime, int duration, int epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Statuses status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                "epicId=" + epicId +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public TaskTypes getClassType() {
        return type;
    }

}
