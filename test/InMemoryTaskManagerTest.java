import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryTaskManagerTest {

    static TaskManager taskManager = Managers.getDefault();


    @BeforeEach
    void creatingTaskEpicSubTask() {
        taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Задача1", "Разобраться в теории спринта 6", Status.NEW);
        Task task2 = new Task("Задача2", "Сдать TZ_6", Status.NEW);
        taskManager.createTask(task1);//1
        taskManager.createTask(task2);//2

        Epic epic1 = new Epic("Epic1", "Продать зимнюю резину на дисках");
        taskManager.createEpic(epic1);
        int epic1Id = epic1.getId();//3

        Subtask subtask1p1 = new Subtask("SubTask1_1", "Помыть резину, сделать фото", Status.NEW, epic1Id);
        Subtask subtask1p2 = new Subtask("SubTask1_2", "Разместить объявление на Авито", Status.NEW, epic1Id);
        Subtask subtask1p3 = new Subtask("SubTask1_3", "Разместить объявление на Авто.ру", Status.NEW, epic1Id);
        taskManager.createSubtask(subtask1p1);//4
        taskManager.createSubtask(subtask1p2);//5
        taskManager.createSubtask(subtask1p3);//6

        Epic epic2 = new Epic("Epic2", "Продать морозильник");
        taskManager.createEpic(epic2);
        int epic2d = epic2.getId();//7

        Subtask subtask2p1 = new Subtask("SubTask2_1", "Разобрать морозильник, выкинуть ненужное", Status.NEW, epic2d);
        Subtask subtask2p2 = new Subtask("SubTask2_2", "Помыть морозильник", Status.NEW, epic2d);
        Subtask subtask2p3 = new Subtask("SubTask2_3", "Сделать фото", Status.NEW, epic2d);
        Subtask subtask2p4 = new Subtask("SubTask2_3", "Разместить объявление на Авито", Status.NEW, epic2d);
        taskManager.createSubtask(subtask2p1);//8
        taskManager.createSubtask(subtask2p2);//9
        taskManager.createSubtask(subtask2p3);//10
        taskManager.createSubtask(subtask2p4);//11

        Epic epic3 = new Epic("Epic3", "Для проверки");
        taskManager.createEpic(epic3);//12
    }

    @Test
    public void createTask() {
        assertNotNull(taskManager.getListOfTasks(), "Список задач пуст");
    }

    @Test
    public void createEpic() {
        assertNotNull(taskManager.getListOfEpics(), "Список эпиков пуст");
    }

    @Test
    public void createSubtask() {
        assertNotNull(taskManager.getListOfSubtasks(), "Список подзадач пуст");
    }

    @Test
    public void checkAddOperationsWorkingCorrectly() {
        Task task3 = new Task("Задача3", "Проверка себя");
        String title = task3.getTitle();
        String description = task3.getDescription();
        Status status = task3.getStatus();
        taskManager.createTask(task3);//12
        int task3Id = task3.getId();

        assertEquals(taskManager.getTaskById(task3Id).getTitle(), title, "Ошибка в имени до и после добавления задачи");
        assertEquals(taskManager.getTaskById(task3Id).getDescription(), description, "Ошибка в описании до и после добавления задачи");
        assertEquals(taskManager.getTaskById(task3Id).getStatus(), status, "Ошибка статуса до и после добавления задачи");
    }

    @Test
    public void checkUpdateOperations() {
        Task taskUpdate = new Task("Задача обновилась", "Новое описание");
        taskUpdate.setId(1);
        taskManager.updateTask(taskUpdate);

        Epic epicUpdate = new Epic("Эпик обновился", "Новая Подзадача Эпика");
        epicUpdate.setId(3);
        taskManager.updateEpic(epicUpdate);

        Subtask subtaskUpdate = new Subtask("Подзадача обновилась", "Новая Подзадача Эпика", Status.DONE, epicUpdate.getId());
        subtaskUpdate.setId(4);
        taskManager.updateSubtask(subtaskUpdate);

        assertEquals(taskUpdate, taskManager.getTaskById(1), "Ошибка обновления Задачи");
        assertEquals(epicUpdate.getTitle(), taskManager.getEpicById(3).getTitle(), "Ошибка обновления Эпика");
        assertEquals(epicUpdate.getDescription(), taskManager.getEpicById(3).getDescription(), "Ошибка обновления Эпика");
        assertEquals(subtaskUpdate, taskManager.getSubtaskById(4), "Ошибка обновления Подзадачи");
    }

    @Test
    public void getHistory() {

        Task taskTest1 = taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(7);
        taskManager.getSubtaskById(5);

        Task taskTest2 = taskManager.getSubtaskById(8);
        taskManager.getSubtaskById(11);
        taskManager.getSubtaskById(10);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(9);
        Task taskTest3 = taskManager.getEpicById(3);

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "Ошибка просмотра истории");
        assertEquals(taskTest1, history.get(0), "Ошибка соответствия Задачи 1");
        assertEquals(taskTest2, history.get(4), "Ошибка соответствия Задачи 2");
        assertEquals(taskTest3, history.get(9), "Ошибка соответствия Задачи 3");
    }

    @Test
    public void checkEpicStatus() {

        //проверить статус эпика IN_PROGRESS если Subtask NEW и DONE
        int epicId = taskManager.getEpicById(12).getId();
        Epic expectedEpicOfSubtask = taskManager.getEpicById(epicId);

        Subtask subtask3p1 = new Subtask("SubTask3_1", "Проверка обновления", epicId);
        Subtask subtask3p2 = new Subtask("SubTask3_1", "Проверка обновления обновления", epicId);
        taskManager.createSubtask(subtask3p1);
        taskManager.createSubtask(subtask3p2);
        assertEquals(Status.NEW, expectedEpicOfSubtask.getStatus(), "Статус не NEW");

        Subtask updateSubtask3p1 = new Subtask("SubTask3_1", "Проверка обновления 1.0", subtask3p1.getId(), Status.DONE, epicId);
        taskManager.updateSubtask(updateSubtask3p1);
        assertEquals(Status.IN_PROGRESS, expectedEpicOfSubtask.getStatus(), "Статус не IN_PROGRESS");

        //проверить статус эпика IN_PROGRESS если Subtask IN_PROGRESS
        Subtask updateSubtask3p1p1 = new Subtask("SubTask3_1", "Проверка обновления 2.0", subtask3p1.getId(), Status.IN_PROGRESS, epicId);
        Subtask updateSubtask3p2p1 = new Subtask("SubTask3_2", "Проверка обновления обновления 1.0", subtask3p2.getId(), Status.IN_PROGRESS, epicId);
        taskManager.updateSubtask(updateSubtask3p1p1);
        taskManager.updateSubtask(updateSubtask3p2p1);
        assertEquals(Status.IN_PROGRESS, expectedEpicOfSubtask.getStatus(), "Статус не IN_PROGRESS");

        //проверить статус эпика DONE если Subtask DONE
        Subtask updateSubtask3p1p1p1 = new Subtask("SubTask3_1", "Проверка обновления 3.0", subtask3p1.getId(), Status.DONE, epicId);
        Subtask updateSubtask3p2p1p1 = new Subtask("SubTask3_2", "Проверка обновления обновления 2.0", subtask3p2.getId(), Status.DONE, epicId);
        taskManager.updateSubtask(updateSubtask3p1p1p1);
        taskManager.updateSubtask(updateSubtask3p2p1p1);
        assertEquals(Status.DONE, expectedEpicOfSubtask.getStatus(), "Статус не DONE");

    }

    @Test
    public void deleteTaskById() {
        int task1Id = taskManager.getTaskById(1).getId();
        int task2Id = taskManager.getTaskById(2).getId();
        final List<Task> tasks = taskManager.getListOfTasks();

        assertNotNull(tasks, "Список задач не заполнен");
        assertEquals(2, tasks.size(), "Неверное количество задач.");

        tasks.remove(task1Id);
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(2, task2Id, "В списке должна остаться только Задача 2");

    }

    @Test
    public void deleteSubtaskById() {
        int subtask1p1 = taskManager.getSubtaskById(5).getId();
        assertNotNull(taskManager.getListOfSubtasks(), "Список подзадач не заполнен");
        assertEquals(7, taskManager.getListOfSubtasks().size(), "Неверное количество задач.");
        taskManager.deleteSubtaskById(subtask1p1);
        assertNull(taskManager.getSubtaskById(subtask1p1), "Подзадача не удалена");
    }

    @Test
    public void deleteEpicById() {
        int epic1Id = taskManager.getEpicById(3).getId();
        assertNotNull(taskManager.getListOfEpics(), "Список эпиков не заполнен");
        taskManager.deleteEpicById(epic1Id);
        assertNull(taskManager.getEpicById(epic1Id), "Эпик не удален");
    }

    @Test
    public void deleteAllTasks() {
        taskManager.deleteAllTasks();
        assertNull(null, "Список задач не пуст");
    }

    @Test
    public void deleteAllEpics() {
        taskManager.deleteAllEpics();
        assertNull(null, "Список задач не пуст");
    }

    @Test
    public void deleteAllSubTasks() {
        taskManager.deleteAllSubtasks();
        assertNull(null, "Список подзадач не пуст");
    }

}

