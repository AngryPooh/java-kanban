package manager;

import expention.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static java.lang.String.format;


public class FileBackedTaskManager extends InMemoryTaskManager {
    protected File file;
    protected final String tabular = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);

        List<String> lines;

        int countId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            reader.readLine();

            lines = Files.readAllLines(file.toPath());

            for (int i = 1; i < lines.size(); i++) {
                Task task = fromString(lines.get(i));
                int taskId = task.getId();
                countId = Math.max(countId, taskId);

                TaskType taskType = task.getTaskType();

                switch (taskType) {
                    case TASK -> fbtm.tasks.put(task.getId(), task);
                    case EPIC -> fbtm.epics.put(task.getId(), (Epic) task);
                    default -> {
                        Subtask subtask = (Subtask) task;
                        fbtm.subtasks.put(task.getId(), subtask);
                        Epic epic = fbtm.epics.get(subtask.getEpicId());
                        epic.addSubtaskId(subtask.getId());
                    }
                }
            }
            fbtm.setGeneratorId(countId);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при выгрузке из файла: ", e);
        }
        return fbtm;
    }

    public static String taskToString(Task task) {

        String taskString;
        if (task.getTaskType().equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            taskString = format("%d,%s,%s,%s,%s,%d%n", subtask.getId(), subtask.getTaskType(), subtask.getTitle(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
        } else {
            taskString = format("%d,%s,%s,%s,%s%n", task.getId(), task.getTaskType(), task.getTitle(), task.getStatus(), task.getDescription());
        }
        return taskString;
    }

    public static Task fromString(String taskString) {
        String[] divide = taskString.split(",");

        int id = Integer.parseInt(divide[0]);
        TaskType taskType = TaskType.valueOf(divide[1]);
        String title = divide[2];
        Status status = Status.valueOf(divide[3]);
        String description = divide[4];

        switch (taskType) {
            case TASK -> {
                Task task = new Task(title, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(title, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            }
            default -> {
                int epicId = Integer.parseInt(divide[5]);
                Subtask subtask = new Subtask(title, description, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            }
        }
    }

    public void save() {

        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(tabular);
            for (Task task : tasks.values()) {
                String taskFile = taskToString(task);
                writer.write(taskFile);
            }
            for (Epic epic : epics.values()) {
                String epicFile = taskToString(epic);
                writer.write(epicFile);
            }

            for (Subtask subtask : subtasks.values()) {
                String subtaskFile = taskToString(subtask);
                writer.write(subtaskFile);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл ", e);
        }
    }

    @Override
    public Task createTask(Task newTask) {
        super.createTask(newTask);
        save();
        return newTask;
    }

    @Override
    public Subtask createSubtask(Subtask newSubtask) {
        super.createSubtask(newSubtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        super.createEpic(newEpic);
        save();
        return newEpic;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTaskById(int taskIdForDelete) {
        super.deleteTaskById(taskIdForDelete);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskIdForDelete) {
        super.deleteSubtaskById(subtaskIdForDelete);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpicById(int epicIdForDelete) {
        super.deleteEpicById(epicIdForDelete);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        save();
        return super.getTaskById(taskId);
    }

    @Override
    public Subtask getSubtaskById(int subTaskId) {
        save();
        return super.getSubtaskById(subTaskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        save();
        return super.getEpicById(epicId);
    }

    @Override
    public List<Subtask> getListOfSubtasksByEpicId(int epicId) {
        return super.getListOfSubtasksByEpicId(epicId);
    }

}
