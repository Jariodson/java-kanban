package Tasks;

import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

public class Task {
    protected String name;
    private final static TaskTypes type = TaskTypes.TASK;
    protected String description;
    protected int id;
    protected Statuses status;

    public Task(String name, String description, Statuses status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, Statuses status, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
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
}
