package test;
import org.junit.jupiter.api.*;
import tasks.*;
import manager.*;

import java.util.*;

class TasksTest { //Я постарался покрыть все основные функции тестами, хотя по сути в программе мы мало что поменяли, а в прошлый раз и так много тестов было проведено
   private Managers managers = new Managers();
   private TaskManager taskManager;
   private HistoryManager historyManager;
   @Test
   void equalsTest() {
      taskManager =  managers.getDefault();
      Task task1 = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
      Task task2 = new Task("Задача2", Status.NEW, "описаниеЗадачи2");
      Task task3 = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
      Assertions.assertEquals(task1,task3,"Не равны");
      Assertions.assertNotEquals(task1,task2);
      task1.setName("Задача11");
      task3.setName("Задача11");
      Assertions.assertEquals(task1,task3,"Не равны");
      task3.setName("Задача13");
      Assertions.assertNotEquals(task1,task3,"Не равны");
   }
   @Test
   void addTaskstest() {
      taskManager =  managers.getDefault();
      Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
      taskManager.addTask(task);
      Task savedTask = taskManager.getTask(task.getId());
      Assertions.assertNotNull(savedTask,"Задача не найдена");
      Assertions.assertEquals(task,savedTask,"задачи не совподают");
      List<Task> tasks = taskManager.getAllTasks();
      Assertions.assertNotNull(tasks,"задачи нулевые");
      Assertions.assertEquals(1,tasks.size(),"размер не равен");
      Assertions.assertEquals(task,tasks.get(0),"задачи не совпадают");
   }
   @Test
   void addEpicsandSubsstest() {
      taskManager =  managers.getDefault();
      Epic epic = new Epic("Эпик1", Status.NEW, "описаниеЗадачи1");
      taskManager.addEpic(epic);
      SubTask subTask = new SubTask("Сабтаск1", Status.NEW, "описаниеЗадачи1", epic.getId());
      taskManager.addSub(subTask);
      SubTask savedSub = taskManager.getSub(subTask.getId());
      Epic savedEpic = taskManager.getEpic(epic.getId());

      Assertions.assertNotNull(savedSub,"Задача не найдена");
      Assertions.assertEquals(subTask,savedSub,"задачи не совподают");
      List<SubTask> tasks = taskManager.getAllSubs();
      Assertions.assertNotNull(tasks,"задачи нулевые");
      Assertions.assertEquals(1,tasks.size(),"размер не равен");
      Assertions.assertEquals(subTask,tasks.get(0),"задачи не совпадают");

      Assertions.assertNotNull(epic,"Задача не найдена");
      Assertions.assertEquals(epic,savedEpic,"задачи не совподают");
      List<Epic> epics = taskManager.getAllEpics();
      Assertions.assertNotNull(epics,"задачи нулевые");
      Assertions.assertEquals(1,epics.size(),"размер не равен");
      Assertions.assertEquals(epic,epics.get(0),"задачи не совпадают");
   }
   @Test
   void addHistoryAndHistoryHasMemoryandInMemoryTaskManagerWorksRight() {
      taskManager =  managers.getDefault();
      historyManager =  managers.getDefaultHistory();
      Task task = new Task("Задача1", Status.NEW, "описаниеЗадачи1");
      taskManager.addTask(task);
      historyManager.add(task);
      final List<Task> history = historyManager.getHistory();
      Assertions.assertNotNull(history, "История не пустая.");
      Assertions.assertEquals(1, history.size(), "История не пустая.");
      Task updatedTask = new Task("Обновил", Status.NEW, "описаниеЗадачи1",1);
      taskManager.updateTask(updatedTask);
      Assertions.assertNotEquals(historyManager.getHistory().get(0),taskManager.getTask(1));
      Epic epic = new Epic("Эпик", Status.NEW, "описаниеЗадачи1");
      taskManager.addEpic(epic);
      SubTask subTask = new SubTask("Саб1", Status.NEW, "описаниеЗадачи1",epic.getId());
      taskManager.addSub(subTask);
      Assertions.assertEquals(taskManager.getSub(subTask.getId()), subTask);
   }
}