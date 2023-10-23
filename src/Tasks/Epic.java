package Tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subTasksIds;

    public Epic(String name, String description, String status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTasksIds(int id) {
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
