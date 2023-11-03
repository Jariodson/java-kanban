package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTasksIds;

    public Epic(String name, String description, String status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksId() {
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
