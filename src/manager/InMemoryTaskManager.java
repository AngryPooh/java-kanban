package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected int generatorId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    //создание Task
    @Override
    public Integer createTask(Task newTask) {
        int taskId;
        if (newTask.getId() != 0) {
            taskId = newTask.getId();
        } else {
            taskId = ++generatorId;
        }
        newTask.setId(taskId);
        tasks.put(taskId, newTask);
        return taskId;
    }

    //создание Subtask
    @Override
    public Integer createSubtask(Subtask newSubtask) {
        Epic epic = epics.get(newSubtask.getEpicId());
        if (epic == null) return null;
        int newSubtaskId;
        if (newSubtask.getId() != 0) {
            newSubtaskId = newSubtask.getId();
        } else {
            newSubtaskId = ++generatorId;
        }
        newSubtask.setId(newSubtaskId);
        subtasks.put(newSubtaskId, newSubtask);
        epic.addSubtaskId(newSubtaskId);
        return newSubtaskId;
    }

    //создание Epic
    @Override
    public Integer createEpic(Epic newEpic) {
        int epicId;
        if (newEpic.getId() != 0) {
            epicId = newEpic.getId();
        } else {
            epicId = ++generatorId;
        }
        newEpic.setId(epicId);
        epics.put(epicId, newEpic);
        return epicId;
    }

    // удаление всех задач Task
    @Override
    public void deleteAllTasks() {
        for (int id : tasks.keySet()) {
            tasks.remove(id);
        }
    }

    // удаление всех задач Subtask
    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            checkEpicStatus(epic.getId());
        }
        for (Integer id : subtasks.keySet()) {
            subtasks.remove(id);
        }
    }

    // удаление всех задач Epic
    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            Integer epicId = epic.getId();
            List<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
            for (Integer subtaskId : subtaskIds) {
                subtasks.remove(subtaskId);
            }
        }
        for (Integer id : epics.keySet()) {
            epics.remove(id);
        }
    }

    // Получение списка всех задач Task
    @Override
    public List<Task> getListOfTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Получение списка всех задач Subtask
    @Override
    public List<Subtask> getListOfSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Получение списка всех задач Epic
    @Override
    public List<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение по идентификатору Task
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    // Получение по идентификатору Subtask
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    // Получение по идентификатору Epic
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    //Получение списка всех подзадач определённого эпика
    @Override
    public List<Subtask> getListOfSubtasksByEpicId(int epicId) {
        List<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        List<Subtask> subtasksByOneEpic = new ArrayList<>();
        for (int subtaskId : subtaskIds) {
            subtasksByOneEpic.add(subtasks.get(subtaskId));
        }
        return subtasksByOneEpic;
    }

    //Удаление по идентификатору Task
    @Override
    public void deleteTaskById(Integer taskIdForDelete) {
        tasks.remove(taskIdForDelete);

    }

    //Удаление по идентификатору Subtask
    @Override
    public void deleteSubtaskById(Integer subtaskIdForDelete) {
        int epicId = subtasks.get(subtaskIdForDelete).getEpicId();
        List<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        subtaskIds.remove(subtaskIdForDelete);
        subtasks.remove(subtaskIdForDelete);
        checkEpicStatus(epicId);
    }

    //Удаление по идентификатору Epic
    @Override
    public void deleteEpicById(int epicIdForDelete) {
        for (Integer epic : epics.keySet()) {
            List<Integer> subtaskIds = epics.get(epicIdForDelete).getSubtaskIds();
            for (Integer subtaskId : subtaskIds) {
                subtasks.remove(subtaskId);
            }
            epics.remove(epicIdForDelete);
        }
    }

    /*
     Обновление Task
     Новая версия объекта с верным идентификатором передаётся в виде параметра.
    */
    @Override
    public void updateTask(Task updateTask) {
        tasks.put(updateTask.getId(), updateTask);
    }

    /*
     Обновление Subtask
     Новая версия объекта с верным идентификатором передаётся в виде параметра.
    */
    @Override
    public void updateSubtask(Subtask updateSubtask) {
        int id = updateSubtask.getId();
        subtasks.put(id, updateSubtask);
        int epicId = subtasks.get(id).getEpicId();
        checkEpicStatus(epicId);
    }
/*
 Обновление Epic
 Новая версия объекта с верным идентификатором передаётся в виде параметра.
*/

    @Override
    public void updateEpic(Epic updateEpic) {
        epics.put(updateEpic.getId(), updateEpic);
    }

    // Управление статусами для Epic
    @Override
    public void checkEpicStatus(int epicId) {
        int counterNEW = 0;
        int counterDONE = 0;
        List<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            if (subtasks.get(subtaskId).getStatus() == Status.NEW) {
                counterNEW++;
            } else if (subtasks.get(subtaskId).getStatus() == Status.DONE) {
                counterDONE++;
            }
        }
        if (subtaskIds.size() == counterNEW || subtaskIds.isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
        } else if (subtaskIds.size() == counterDONE) {
            epics.get(epicId).setStatus(Status.DONE);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void printAllTasks() {
        if (tasks.isEmpty())
            System.out.println("Список задач пуст.");

        for (int id : tasks.keySet()) {
            var value = tasks.get(id);
            System.out.println("№" + id + " " + value);
        }
    }

    @Override
    public void printAllSubtasks() {
        if (subtasks.isEmpty())
            System.out.println("Список подзадач пуст.");

        for (int id : subtasks.keySet()) {
            var value = subtasks.get(id);
            System.out.println("№" + id + " " + value);
        }
    }

    @Override
    public void printAllEpics() {
        if (epics.isEmpty())
            System.out.println("Список эпиков пуст.");

        for (int id : epics.keySet()) {
            var value = epics.get(id);
            System.out.println("№" + id + " " + value);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}


