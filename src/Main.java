import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {

    static TaskManager taskManager = new InMemoryTaskManager();
    private static final Task task = new Task("Задача", "Купить сапоги");
    private static final Task task1 = new Task("Задача", "Продать пальто");
    private static final Task task2 = new Task("Задача", "Продать кроссовки");
    private static final Task task3 = new Task("Задача", "Купить кальсоны");
    private static final Epic epic = new Epic("Эпик", "Переезд", 10);
    private static final Subtask subtask = new Subtask("Подзадача", "собрать вещи", 10);
    private static final Subtask subtask1 = new Subtask("Подзадача", "собрать посуду", 10);

    public static void initTasks() {
        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);
    }

    public static void deleteTasks() {
        taskManager.deleteTaskById(task.getId());
        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteTaskById(task2.getId());
        taskManager.deleteTaskById(task3.getId());
    }

    public static void printAllTasks() {
        System.out.println("Задачи:");
        taskManager.getListOfTasks().forEach(System.out::println);
        System.out.println("Эпики:");
        taskManager.getListOfEpics().forEach(System.out::println);
        System.out.println("Подзадачи:");
        taskManager.getListOfSubtasks().forEach(System.out::println);
    }

    public static void main(String[] args) {
        initTasks();
        printAllTasks();
        deleteTasks();
        System.out.println("После команды удаления");
        printAllTasks();
    }
}