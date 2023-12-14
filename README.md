Это репозиторий технического задания №5
---

#### Задание: Трекер задач

В программе реализовано 3 вида задач:

* Обычная задача (task)
* Эпик (epic)
* Подзадача (subtask)

Каждая подзачада является частью эпика.

Также, у задач имеется статусы:

* NEW
* IN_PROGRESS
* DONE

Для упаравления классамы были реализованы специальный класс и итерфейс.
Также, был реализован класс и интерфейс для отслеживания истории просмотров задач.

Пример работы программы:

```java
public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Epic epic1 = new Epic("Epic1-1", "Первый эпик", Statuses.NEW);
        Epic epic2 = new Epic("Epic1-2", "Второй эпик", Statuses.NEW);
final int epic1Id = inMemoryTaskManager.createEpic(epic1);
final int epic2Id = inMemoryTaskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача1-1-1", "Первая подзадача первого эпика", Statuses.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Подзадача1-1-2", "Вторая подзадача первого эпика", Statuses.NEW, epic1Id);
        Subtask subtask3 = new Subtask("Подзадача1-2-1", "Третья подзадача первого эпика", Statuses.NEW, epic1Id);
final int subtask1Id = inMemoryTaskManager.createSubtask(subtask1);
final int subtask2Id = inMemoryTaskManager.createSubtask(subtask2);
final int subtask3Id = inMemoryTaskManager.createSubtask(subtask3);

        System.out.println("Список задач:");
        List<Task> tasks = new ArrayList<>(inMemoryTaskManager.getEpics());
        tasks.addAll(inMemoryTaskManager.getSubtasks());
        for (Task task : tasks){
        System.out.println(task.toString());
        }
        System.out.println();

        inMemoryTaskManager.getSubtaskById(subtask1Id);
        inMemoryTaskManager.getEpicById(epic1Id);
        inMemoryTaskManager.getSubtaskById(subtask2Id);
        inMemoryTaskManager.getEpicById(epic2Id);
        inMemoryTaskManager.getSubtaskById(subtask1Id);

        System.out.println("История просмотров:");
        List<Task> tasksHistory = inMemoryTaskManager.getHistory();
        for(Task task : tasksHistory) {
        System.out.println(task);
        }
        System.out.println();

        System.out.println("История после удаления задачи:");
        inMemoryTaskManager.deleteEpicById(epic1Id);
        tasksHistory = inMemoryTaskManager.getHistory();
        for(Task task : tasksHistory) {
        System.out.println(task);
        }
```
