package model;

import java.util.Objects;

public class Task {

    protected String title;
    protected String description;
    protected int id;
    protected Status status = Status.NEW;

    public Task(String title, String description, int taskId, Status status) {
        this.title = title;
        this.description = description;
        this.id = taskId;
        this.status = status;
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, int taskId) {
        this.title = title;
        this.description = description;
        this.id = taskId;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(int taskId) {
        this.id = taskId;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                Objects.equals(status, task.status) &&
                Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    @Override
    public String toString() {
        return "Задача{" +
                "название='" + title + '\'' +
                ", описание='" + description + '\'' +
                ", id='" + id + '\'' +
                ", статус='" + status;
    }
}