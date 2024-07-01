package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    //создание Task
    Integer createTask(Task newTask);

    //создание Subtask
    Integer createSubtask(Subtask newSubtask);

    //создание Epic
    Integer createEpic(Epic newEpic);

    // удаление всех задач Task
    void deleteAllTasks();

    // удаление всех задач Subtask
    void deleteAllSubtasks();

    // удаление всех задач Epic
    void deleteAllEpics();

    // Получение списка всех задач Task
    List<Task> getListOfTasks();

    // Получение списка всех задач Subtask
    List<Subtask> getListOfSubtasks();

    // Получение списка всех задач Epic
    List<Epic> getListOfEpics();

    // Получение по идентификатору Task
    Task getTaskById(int id);

    // Получение по идентификатору Subtask
    Subtask getSubtaskById(int id);

    // Получение по идентификатору Epic
    Epic getEpicById(int id);

    //Получение списка всех подзадач определённого эпика
    List<Subtask> getListOfSubtasksByEpicId(int epicId);

    //Удаление по идентификатору Task
    void deleteTaskById(Integer taskIdForDelete);

    //Удаление по идентификатору Subtask
    void deleteSubtaskById(Integer subtaskIdForDelete);

    //Удаление по идентификатору Epic
    void deleteEpicById(int epicIdForDelete);

    // Обновление Task
    // Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task updateTask);

    void updateSubtask(Subtask updateSubtask);

    void updateEpic(Epic updateEpic);

    // Управление статусами для Epic
    void checkEpicStatus(int epicId);

    // печать списка всех тасков
    void printAllTasks();

    // печать списка всех сабтасков
    void printAllSubtasks();

    // печать списка всех эпиков
    void printAllEpics();


    List<Task> getHistory();

}
