package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();


    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(String title, String description, int epicId) {
        super(title, description, epicId);
    }
    public Epic(String title, String description, int epicId, Status status) {
        super(title, description, epicId, status);
    }

    public Epic(String title, String description, int epicId, Status status, List<Integer> subtaskIds) {
        super(title, description, epicId, status);
        this.subtaskIds = subtaskIds;
    }

    public Epic(List<Integer> subtaskIds) {
        super();
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }
    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(title, epic.title) &&
                Objects.equals(description, epic.description) &&
                Objects.equals(id, epic.id) &&
                Objects.equals(status, epic.status) &&
                Objects.equals(subtaskIds, epic.subtaskIds);

    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status, subtaskIds);
    }

    @Override
    public String toString() {
        if (subtaskIds.isEmpty()) {
            return "Эпик{" +
                    "название='" + title + '\'' +
                    ", описание='" + description + '\'' +
                    ", id='" + id + '\'' +
                    ", статус='" + status + '}' + '\'';
        } else {
            return "Эпик{" +
                    "название='" + title + '\'' +
                    ", описание='" + description + '\'' +
                    ", id='" + id + '\'' +
                    ", статус='" + status + '\'' +
                    ", id подзадач(и)='" + subtaskIds + '}' + '\'';
        }
    }


}
