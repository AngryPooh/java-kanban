import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();

        // Создание 2-х задач
        Task task1 = new Task("Задача 1", "Создаем задачу 1");
        Task task2 = new Task("Задача 2", "Создаем задачу 2");
        taskManager.createTask(task1);//1
        taskManager.createTask(task2);//2

        // Создание эпиков
        Epic epic1 = new Epic("Эпик 1", "Создаем эпик 1");
        Epic epic2 = new Epic("Эпик 2", "Создаем эпик 2");
        taskManager.createEpic(epic1);//3
        taskManager.createEpic(epic2);//4

        // Создание подзадач
        Subtask subtask1p1 = new Subtask("Подзадача 1", "Создаем подзадачу 1", epic1.getId());
        Subtask subtask1p2 = new Subtask("Подзадача 2", "Создаем подзадачу 2", epic1.getId());
        Subtask subtask1p3 = new Subtask("Подзадача 3", "Создаем подзадачу 3", epic1.getId());
        taskManager.createSubtask(subtask1p1);//5
        taskManager.createSubtask(subtask1p2);//6
        taskManager.createSubtask(subtask1p3);//7

        // Печать списков задач, эпиков и подзадач
        System.out.println("Все задачи: " + taskManager.getListOfTasks());
        System.out.println("Все эпики: " + taskManager.getListOfEpics());
        System.out.println("Все подзадачи: " + taskManager.getListOfSubtasks());

        // Запрос созданных задач несколько раз в разном порядке.
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getSubtaskById(subtask1p1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1p2.getId());
        taskManager.getSubtaskById(subtask1p3.getId());
        taskManager.getTaskById(task1.getId());

        // Печатаем историю
        printHistory(taskManager.getHistory());

        //Обновляем Подзадачу
        Subtask updateSubtask = new Subtask("Подзадача 1", "Обновляем подзадачу 1", subtask1p1.getId(), Status.IN_PROGRESS, epic1.getId());
        taskManager.updateSubtask(updateSubtask);
        System.out.println("\nОбновление эпиков" + taskManager.getListOfEpics());
        System.out.println("Обновление подзадачи" + taskManager.getListOfSubtasks());


        Epic newEpic1 = new Epic("newEpic 1", "Доделать дела с авто");
        newEpic1.setId(3);
        taskManager.updateEpic(newEpic1);
        System.out.println(taskManager.getEpicById(3));
        System.out.println("\nОбновление эпиков" + taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubtasks());

        // Удалите задачу, которая есть в истории, и проверяем, что при печати она не будет выводиться.
        taskManager.deleteTaskById(task1.getId());
        printHistory(taskManager.getHistory());

        //удаляем подзадачу, которая есть в истории, и проверяем, что при печати она не будет выводиться.
        taskManager.deleteSubtaskById(subtask1p1.getId());
        printHistory(taskManager.getHistory());

        //Удаляем эпик с двумя подзадачами и проверяем, что из истории удалился как сам эпик, так и все его подзадачи.
        taskManager.deleteEpicById(epic1.getId());
        printHistory(taskManager.getHistory());

        // Повторная распечатка после удаления
        System.out.println("\nПовторная распечатка после удаления:");
        System.out.println("Все задачи: " + taskManager.getListOfTasks());
        System.out.println("Все эпики: " + taskManager.getListOfEpics());
        System.out.println("Все подзадачи: " + taskManager.getListOfSubtasks());
    }

    private static void printHistory(List<Task> history) {
        System.out.println("Смотрим историю:");
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println();
    }
}
