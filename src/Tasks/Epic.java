package Tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasksIds;

    public Epic(String name, String description, Statuses status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public
    List<Integer> getSubTasksId() {
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
