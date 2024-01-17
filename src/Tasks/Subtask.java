package Tasks;

import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

public class Subtask extends Task {
    private final int epicId;
    //protected String classType = Subtask.class.getSimpleName().toUpperCase();
    private final static TaskTypes type = TaskTypes.SUBTASK;

    public Subtask(String name, String description, Statuses status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, Statuses status, String description, int epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskTypes getClassType() {
        return type;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
