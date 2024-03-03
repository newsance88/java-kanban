package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class ManagersTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }
    @Test
    void managersNotNull() {
        Assertions.assertNotNull(taskManager);
        Assertions.assertNotNull(historyManager);
    }

}
