package Tasks;

import Tasks.Enums.Statuses;
import Tasks.Enums.TaskTypes;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasksIds;
    private static final TaskTypes type = TaskTypes.EPIC;

    public Epic(String name, String description, Statuses status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public Epic(Integer id, String name, Statuses status, String description) {
        super(id, name, status, description);
        subTasksIds = new ArrayList<>();
    }

    @Override
    public TaskTypes getClassType() {
        return type;
    }

    public List<Integer> getSubTasksId() {
        return subTasksIds;
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
}
