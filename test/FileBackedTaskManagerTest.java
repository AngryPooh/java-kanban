import manager.FileBackedTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager flt;
    File file;

    @DisplayName("Тест проверяет создание файла")
    @Test
    public void shouldReturnPositiveWhenLoadEmptyFile() throws IOException {
        file = File.createTempFile("empty", ".csv");
        flt = new FileBackedTaskManager(file);
        assertEquals(0, flt.getListOfTasks().size(), "Список Tasks должен быть пустым");
        assertEquals(0, flt.getListOfEpics().size(), "Список Epics должен быть пустым");
        assertEquals(0, flt.getListOfSubtasks().size(), "Список Subtasks должен быть пустым");
        assertEquals(0, file.length(), "Размер файла больше нуля");
        flt.save();
        String heading = Files.readAllLines(file.toPath()).getFirst();
        assertNotNull(heading, "Нет заголовка");
    }

    @DisplayName("Тест проверяет добавление задач в файл и выгрузку из файла")
    @Test
    public void shouldSaveToFile() throws IOException {

        file = File.createTempFile("fileTest", ".csv");
        flt = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks = new ArrayList<>();
        Task task = new Task("Задача1", "Создаем task1", Status.NEW);
        Epic epic = new Epic("Epic1", "Создаем epic1");
        Subtask subtask = new Subtask("SubTask1", "Создаем subtask1", Status.NEW, 2);
        flt.createTask(task);
        flt.createEpic(epic);
        flt.createSubtask(subtask);
        assertNotNull(flt, "Файл пуст");

        tasks.add(flt.getTaskById(1));
        tasks.add(flt.getEpicById(2));
        tasks.add(flt.getSubtaskById(3));

        assertNotNull(tasks, "Список задач пустой");

        FileBackedTaskManager flt1 = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks2 = new ArrayList<>();
        assertNotNull(flt1, "Список не должен быть  пустым");

        tasks2.add(flt1.getTaskById(1));
        tasks2.add(flt1.getEpicById(2));
        tasks2.add(flt1.getSubtaskById(3));

        assertEquals(tasks.size(), tasks2.size(), "Размер не совпадает");
        assertEquals(tasks, tasks2, "Задачи должны быть одинаковыми");
    }

    @DisplayName("Тест проверяет обновление задач в файле")
    @Test
    public void shouldSaveToFileAfterUpdate() throws IOException {
        file = File.createTempFile("fileTest", ".csv");
        flt = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks = new ArrayList<>();
        Task task = new Task("Задача1", "Создаем task1", Status.NEW);
        flt.createTask(task);
        tasks.add(flt.getTaskById(1));
        Epic epic = new Epic("Epic1", "Создаем epic1");
        flt.createEpic(epic);
        tasks.add(flt.getEpicById(2));
        Task newTask = flt.getTaskById(1);
        newTask.setStatus(Status.IN_PROGRESS);
        flt.updateTask(newTask);

        FileBackedTaskManager fltNew = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks2 = new ArrayList<>();
        tasks2.add(fltNew.getTaskById(1));
        tasks2.add(fltNew.getEpicById(2));
        assertEquals(tasks, tasks2, "Изменения не сохранены");


    }
}
