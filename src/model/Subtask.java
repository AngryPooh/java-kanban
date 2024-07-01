package model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int subtaskId, Status status, int epicId) {
        super(title, description, subtaskId, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int subtaskId, int epicId) {
        super(title, description, subtaskId);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int subtaskId, Status status) {
        super(title, description, subtaskId, status);
    }

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }


    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(title, subtask.title) &&
                Objects.equals(description, subtask.description) &&
                Objects.equals(id, subtask.id) &&
                Objects.equals(status, subtask.status) &&
                (epicId == subtask.epicId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "название='" + title + '\'' +
                ", описание='" + description + '\'' +
                ", id='" + id + '\'' +
                ", статус='" + status + '\'' +
                ", id эпика='" + epicId + '}' + '\'';
    }

}
