import manager.Managers;
import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.Test;


import java.util.List;

import static model.Status.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    protected TaskManager taskManager = Managers.getDefault();

    @Test
    public void createTask() {
        Task task = new Task("Задача", "Продать пальто", Status.NEW);
        final int taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getListOfTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    public void createEpic() {
        final Epic epic = new Epic("Эпик", "Переезд");
        final int epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertNotNull(taskManager.getListOfEpics(), "Задачи на возвращаются.");
        assertNotNull(epic.getSubtaskIds(), "Список подзадач не создан.");
        assertEquals(Status.NEW, epic.getStatus(), "Статус не NEW");
        assertEquals(epicId, epic.getId(), "Идентификаторы задач не совпадают");
    }

    @Test
    public void createSubtask() {
        final Epic epic = new Epic("Эпик", "Переезд");
        final int epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);

        final Subtask subtask = new Subtask("Подзадача", "собрать посуду", epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtaskTasks = taskManager.getListOfSubtasks();
        assertNotNull(taskManager.getListOfSubtasks(), "Задачи не возвращаются.");
        assertEquals(1, subtaskTasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtaskTasks.getFirst(), "Задачи не совпадают.");
        assertEquals(3, subtask.getId(), "Идентификаторы задач не совпадают");

        assertEquals(savedEpic, epic, "Эпик подзадачи неверный");
        assertEquals(Status.NEW, epic.getStatus(), "Статус не NEW");
        assertNotNull(epicId, "Эпик подзадачи не найден");
    }

    @Test
    public void getHistory() {

        final Task task = new Task("Задача", "Продать пальто", 1);
        final int taskId = taskManager.createTask(task);
        final Epic epic = new Epic("Эпик", "Переезд");
        final int epicId = taskManager.createEpic(epic);
        final Subtask subtask = new Subtask("Подзадача", "собрать посуду",  epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        final Subtask subtask1 = new Subtask("Подзадача", "заказать машину",  epicId);
        final int subtas1kId = taskManager.createSubtask(subtask1);

        taskManager.getSubtaskById(subtas1kId);
        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);

        assertEquals(List.of(subtask1, task, epic, subtask), taskManager.getHistory());
    }


    @Test
    public void checkEpicStatus() {
        final Epic epic = new Epic("Эпик", "Переезд");
        final int epicId = taskManager.createEpic(epic);
        final Subtask subtask = new Subtask("Подзадача", "собрать посуду", epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        final Subtask subtask1 = new Subtask("Подзадача", "заказать машину", epicId);
        final int subtaskId1 = taskManager.createSubtask(subtask1);

        Epic expectedEpicOfSubtask = taskManager.getEpicById(epicId);

        //проверить статус эпика IN_PROGRESS если Subtask NEW и DONE
        Subtask updateSubtask1 = new Subtask("Подзадача", "заказать машину", subtaskId1, Status.DONE, epicId);
        taskManager.updateSubtask(updateSubtask1);
        assertEquals(IN_PROGRESS, expectedEpicOfSubtask.getStatus(), "Статус не IN_PROGRESS");

        //проверить статус эпика IN_PROGRESS если Subtask IN_PROGRESS
        Subtask updateSubtask = new Subtask("Подзадача", "собрать посуду", subtaskId, Status.DONE, epicId);
        Subtask update2Subtask1 = new Subtask("Подзадача", "description4", subtaskId1, Status.DONE, epicId);
        taskManager.updateSubtask(updateSubtask);
        taskManager.updateSubtask(update2Subtask1);
        assertEquals(Status.DONE, expectedEpicOfSubtask.getStatus(), "Статус не DONE");

        //проверить статус эпика DONE если Subtask DONE
        Subtask update2Subtask = new Subtask("Подзадача", "собрать посуду", subtaskId, IN_PROGRESS,
                epicId);
        Subtask update3Subtask1 = new Subtask("Подзадача", "заказать машину", subtaskId1, IN_PROGRESS,
                epicId);
        taskManager.updateSubtask(update2Subtask);
        taskManager.updateSubtask(update3Subtask1);
        assertEquals(IN_PROGRESS, expectedEpicOfSubtask.getStatus(), "Статус не IN_PROGRESS");
    }

    @Test
    public void deleteTaskById() {
        final Task task = new Task("Задача", "Продать пальто");
        final int taskId = taskManager.createTask(task);
        final Task task1 = new Task("Задача", "Купить сапоги");
        taskManager.createTask(task1);
        final List<Task> tasks = taskManager.getListOfTasks();
        assertNotNull(tasks, "Список задач не заполнен");
        final int taskSize = tasks.size();
        assertEquals(2, taskSize, "Неверное количество задач.");
        taskManager.deleteTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId), "Задача не удалена");

    }

    @Test
    public void deleteSubtaskById() {

        final Epic epic = new Epic("Эпик", "Переезд");
        final int epicId = taskManager.createEpic(epic);

        final Subtask subtask = new Subtask("Подзадача", "собрать посуду", epicId);
        final int subtaskId = taskManager.createSubtask(subtask);

        final List<Subtask> subtaskTasks = taskManager.getListOfSubtasks();
        final int size = subtaskTasks.size();

        assertNotNull(taskManager.getListOfSubtasks(), "Список подзадач не заполнен");
        assertEquals(size, taskManager.getListOfSubtasks().size(), "Неверное количество задач.");
        taskManager.deleteSubtaskById(subtaskId);
        assertNull(taskManager.getSubtaskById(subtaskId), "Подзадача не удалена");
    }

    @Test
    public void deleteEpicById() {
        final Epic epic = new Epic("Эпик", "Переезд");
        final int epicId = taskManager.createEpic(epic);

        assertNotNull(taskManager.getListOfEpics(), "Список эпиков не заполнен");
        taskManager.deleteEpicById(epicId);
        assertNull(taskManager.getEpicById(epicId), "Эпик не удален");
        taskManager.printAllEpics();
    }
    
}
