package Tasks;

import Tasks.Enums.Status;
import Tasks.Enums.TaskType;

public class Subtask extends Task {
    private final static TaskType type = TaskType.SUBTASK;
    private final int epicId;

    public Subtask(String name, String description, Status status, int epicId, int duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, Status status, String description, String startTime, int duration, int epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId) {
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
    public TaskType getClassType() {
        return type;
    }

}
