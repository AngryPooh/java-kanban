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

    private final HistoryManager historyManager;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected int generatorId = 0;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    private int getNextId() {
        return ++generatorId;
    }

    //создание Task
    @Override
    public Task createTask(Task newTask) {
        newTask.setId(getNextId());
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    //создание Subtask
    @Override
    public Subtask createSubtask(Subtask newSubtask) {
        Epic epic = epics.get(newSubtask.getEpicId());

        if (epic == null) {
            return null;
        }

        newSubtask.setId(getNextId());
        subtasks.put(newSubtask.getId(), newSubtask);
        newSubtask.setEpicId(epic.getId());
        epic.addSubtaskId(newSubtask.getId());
        checkEpicStatus(epic);
        return newSubtask;
    }

    //создание Epic
    @Override
    public Epic createEpic(Epic newEpic) {
        newEpic.setId(getNextId());
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    // удаление всех задач Task
    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    //Удаление по идентификатору Task
    @Override
    public void deleteTaskById(int taskIdForDelete) {
        tasks.remove(taskIdForDelete);
        historyManager.remove(taskIdForDelete);

    }

    // удаление всех задач Subtask
    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            checkEpicStatus(epic);
        }
    }

    //Удаление по идентификатору Subtask
    @Override
    public void deleteSubtaskById(int subtaskIdForDelete) {
        int epicId = subtasks.get(subtaskIdForDelete).getEpicId();
        subtasks.remove(subtaskIdForDelete);
        epics.get(epicId).getSubtaskIds().remove((Integer) subtaskIdForDelete);
        checkEpicStatus(epics.get(epicId));
        historyManager.remove(subtaskIdForDelete);
    }


    // удаление всех задач Epic
    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
            for (int subtaskId : epic.getSubtaskIds()) {
                historyManager.remove(subtaskId);
            }
        }
        epics.clear();
        subtasks.clear();
    }

    //Удаление по идентификатору Epic
    @Override
    public void deleteEpicById(int epicIdForDelete) {
        Epic epic = epics.remove(epicIdForDelete);
        historyManager.remove(epicIdForDelete);
        for (int subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
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
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    // Получение по идентификатору Subtask
    @Override
    public Subtask getSubtaskById(int subTaskId) {
        Subtask subtask = subtasks.get(subTaskId);
        historyManager.add(subtask);
        return subtask;
    }

    // Получение по идентификатору Epic
    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
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

    /*
     Обновление Task
     Новая версия объекта с верным идентификатором передаётся в виде параметра.
    */
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /*
     Обновление Subtask
     Новая версия объекта с верным идентификатором передаётся в виде параметра.
    */
    @Override
    public void updateSubtask(Subtask updateSubtask) {
        int subtaskId = updateSubtask.getId();
        if (subtasks.containsKey(subtaskId)) {
            subtasks.put(subtaskId, updateSubtask);
            checkEpicStatus(epics.get(updateSubtask.getEpicId()));
        }
    }
/*
 Обновление Epic
 Новая версия объекта с верным идентификатором передаётся в виде параметра.
*/

    @Override
    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            epics.get(updateEpic.getId()).setTitle(updateEpic.getTitle());
            epics.get(updateEpic.getId()).setDescription(updateEpic.getDescription());
        }
    }

    // Управление статусами для Epic
    @Override
    public void checkEpicStatus(Epic epic) {
        int countNew = 0;
        int countInProgress = 0;
        int countDone = 0;
        if (epic.getSubtaskIds().isEmpty()) {
            epics.get(epic.getId()).setStatus(Status.NEW);
            return;
        }
        for (int sbId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(sbId);
            if (subtask.getStatus() == Status.NEW) {
                countNew++;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                countInProgress++;
            } else if (subtask.getStatus() == Status.DONE) {
                countDone++;
            }
        }
        if (countNew >= 0 && countInProgress == 0 && countDone == 0) {
            epics.get(epic.getId()).setStatus(Status.NEW);
        } else if (countDone > 0 && countNew == 0 && countInProgress == 0) {
            epics.get(epic.getId()).setStatus(Status.DONE);
        } else {
            epics.get(epic.getId()).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}