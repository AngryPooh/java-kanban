import manager.HistoryManager;
import manager.Managers;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    protected Task task = new Task("Задача", "Продать пальто", 1, Status.NEW);
    protected Task task1 = new Task("Задача", "Купить сапоги", 2, Status.NEW);
    protected Epic epic = new Epic("Эпик", "Переезд",10, Status.NEW);
    protected Subtask subtask = new Subtask("Подзадача", "собрать посуду", 1, Status.NEW, 10);
    protected Subtask subtask1 = new Subtask("Подзадача", "заказать машину", 2, Status.NEW, 10);


    @Test
    public void add() {
        historyManager.add(task);
        historyManager.add(task1);
        Assertions.assertEquals(List.of(task, task1), historyManager.getHistory());

        historyManager.add(epic);
        assertEquals(List.of(task,task1,epic), historyManager.getHistory());

        historyManager.add(subtask);
        historyManager.add(subtask1);
        assertEquals(List.of(task,task1,epic,subtask,subtask1), historyManager.getHistory());
    }

    @Test
    public void getHistory() {
        List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "Список истории отсутствует");
        assertTrue(history.isEmpty(), "История не пустая");

        historyManager.add(task);
        historyManager.add(task1);
        history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История не сохранена");

        historyManager.add(epic);
        historyManager.getHistory();
        Assertions.assertEquals(3, history.size(), "История сохранена неверно");
        Assertions.assertEquals(1, task.getId(), "История сохранена неверно");
        Assertions.assertEquals(2, task1.getId(), "История сохранена неверно");

        historyManager.add(subtask);
        historyManager.add(subtask1);
        historyManager.getHistory();
        Assertions.assertEquals(5, history.size(), "История сохранена неверно");
    }
}
