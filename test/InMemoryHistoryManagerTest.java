import manager.HistoryManager;
import manager.Managers;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    Task task1 = new Task("Задача1", "Разобраться в теории спринта 6", Status.NEW);
    Task task2 = new Task("Задача2", "Сдать TZ_6", Status.NEW);
    Epic epic1 = new Epic("Epic1", "Продать зимнюю резину на дисках");
    Subtask subtask1v1 = new Subtask("SubTask1_1", "Помыть резину, сделать фото", Status.NEW);
    Subtask subtask1v2 = new Subtask("SubTask1_2", "Разместить объявление на Авито", Status.NEW);
    Subtask subtask1v3 = new Subtask("SubTask1_3", "Разместить объявление на Авто.ру", Status.NEW);


    @Test
    public void add() {
        task1.setId(1);
        historyManager.add(task1);//1
        task2.setId(2);
        historyManager.add(task2);//2
        assertEquals(List.of(task1, task2), historyManager.getHistory());

        epic1.setId(3);
        historyManager.add(epic1);
        assertEquals(List.of(task1, task2, epic1), historyManager.getHistory());

        subtask1v1.setEpicId(3);
        subtask1v1.setId(4);
        subtask1v2.setEpicId(3);
        subtask1v2.setId(5);
        subtask1v3.setEpicId(3);
        subtask1v3.setId(6);
        historyManager.add(subtask1v1);
        historyManager.add(subtask1v2);
        historyManager.add(subtask1v3);
        assertEquals(List.of(task1, task2, epic1, subtask1v1, subtask1v2, subtask1v3), historyManager.getHistory());
    }

    @Test
    public void getHistory() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует");
        assertTrue(history.isEmpty(), "История не пустая");

        task1.setId(1);
        historyManager.add(task1);
        task2.setId(2);
        historyManager.add(task2);
        epic1.setId(3);
        historyManager.add(epic1);
        historyManager.getHistory();
        history = historyManager.getHistory();
        assertEquals(3, history.size(), "История не сохранена");
        assertEquals(1, task1.getId(), "История сохранена неверно");
        assertEquals(2, task2.getId(), "История сохранена неверно");
    }

    @Test
    public void doesNotAddDuplicates() {

        Task task1 = new Task("Задача 1", "Разобраться в теории спринта 6");
        task1.setId(1);
        historyManager.add(task1);

        // Добавляем задачу несколько раз, чтобы она добавилась в историю
        historyManager.add(task1);
        historyManager.add(task1);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();

        // Проверяем, что задача только одна в истории
        assertEquals(1, history.size(), "История должна содержать только одну запись для каждой задачи");
        assertTrue(history.contains(task1), "История должна содержать задачу 1");
    }

    @Test
    public void addingTaskToEndWhenAddAgain() {

        task1.setId(1);
        historyManager.add(task1);
        task2.setId(2);
        historyManager.add(task2);

        historyManager.getHistory();

        assertEquals(1, task1.getId(), "История сохранена неверно");
        assertEquals(2, task2.getId(), "История сохранена неверно");

        historyManager.add(task1);
        historyManager.getHistory();

        assertEquals(2, task2.getId(), "История сохранена неверно");
        assertEquals(1, task1.getId(), "История сохранена неверно");

    }
}

