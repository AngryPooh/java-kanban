package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> creationHistory = new ArrayList<>();
    private final static int MAX_HISTORY_SIZE = 10;


    @Override
    public void add(Task task) {
        if (creationHistory.size() >= MAX_HISTORY_SIZE) {
            creationHistory.removeFirst();
        }
        creationHistory.add(task);
    }


    @Override
    public List<Task> getHistory() {
        return creationHistory;
    }

}
