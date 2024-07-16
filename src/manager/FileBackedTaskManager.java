package manager;

import expention.ManagerSaveException;
import model.*;

import java.io.*;
import java.util.List;

import static java.lang.String.format;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    File file;
    String tabular = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    //метод сохраняет текущее состояние менеджера в указанный файл

    public static FileBackedTaskManager loadFromFile(File file) {

        try {
            FileBackedTaskManager fbtm = new FileBackedTaskManager(file);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            reader.readLine();

            while (reader.ready()) {
                String taskString = reader.readLine();
                Task task = fromString(taskString);
                if (task.getTaskType().equals(TaskType.TASK)) {
                    fbtm.tasks.put(task.getId(), task);
                } else if (task.getTaskType().equals(TaskType.EPIC)) {
                    fbtm.epics.put(task.getId(), (Epic) task);
                } else {
                    Subtask subtask = (Subtask) task;
                    fbtm.subtasks.put(task.getId(), subtask);
                    Epic epic = fbtm.epics.get(subtask.getEpicId());
                    epic.addSubtaskId(subtask.getId());
                }
            }
            reader.close();
            return fbtm;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при выгрузке из файла: " + e.getMessage());
        }
    }

    //метод восстанавливает данные менеджера из файла при запуске программы.

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

    //метод сохранения задач в строку

    public static Task fromString(String taskString) throws IOException {
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

    //метод создания задачи из строки

    public static void main(String[] args) {

        File file = new File("tasks.cvs");
        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);

        // Создание 2-х задач
        Task task1 = new Task("Задача1", "Создаем задачу1");
        Task task2 = new Task("Задача2", "Создаем задачу2");
        fbtm.createTask(task1);//1
        fbtm.createTask(task2);//2

        // Создание эпиков
        Epic epic1 = new Epic("Эпик1", "Создаем эпик1");
        Epic epic2 = new Epic("Эпик2", "Создаем эпик2");
        fbtm.createEpic(epic1);//3
        fbtm.createEpic(epic2);//4

        // Создание подзадач
        Subtask subtask1p1 = new Subtask("Подзадача1", "Создаем подзадачу1", epic2.getId());
        Subtask subtask1p2 = new Subtask("Подзадача2", "Создаем подзадачу2", epic2.getId());
        Subtask subtask1p3 = new Subtask("Подзадача3", "Создаем подзадачу3", epic2.getId());
        fbtm.createSubtask(subtask1p1);//5
        fbtm.createSubtask(subtask1p2);//6
        fbtm.createSubtask(subtask1p3);//7


        FileBackedTaskManager file1 = FileBackedTaskManager.loadFromFile(file);
        System.out.println(file1.getListOfTasks());
        System.out.println(file1.getListOfEpics());
        System.out.println(file1.getListOfSubtasks());

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
            throw new ManagerSaveException("Ошибка записи в файл " + e.getMessage());
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
