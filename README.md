Это репозиторий технического задания №4
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
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task1 = new Task("Task1-1", "Первая задача", Statuses.NEW);
        final int task1Id = inMemoryTaskManager.createTask(task1);

        Epic epic1 = new Epic("Epic1-1", "Первый эпик", Statuses.NEW);
        Epic epic2 = new Epic("Epic1-2", "Второй эпик", Statuses.NEW);
        final int epic1Id = inMemoryTaskManager.createEpic(epic1);
        final int epic2Id = inMemoryTaskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача1-1-1", "Первая подзадача первого эпика", Statuses.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Подзадача1-1-2", "Вторая подзадача первого эпика", Statuses.NEW, epic1Id);
        Subtask subtask3 = new Subtask("Подзадача1-2-1", "Первая подзадача второго эпика", Statuses.NEW, epic2Id);
        final int subtask1Id = inMemoryTaskManager.createSubtask(subtask1);
        final int subtask2Id = inMemoryTaskManager.createSubtask(subtask2);
        final int subtask3Id = inMemoryTaskManager.createSubtask(subtask3);

        Subtask subtask101 = inMemoryTaskManager.getSubtaskById(subtask1Id);
        subtask101.setStatus(Statuses.DONE);
        inMemoryTaskManager.updateSubtask(subtask101);
        Subtask subtask302 = inMemoryTaskManager.getSubtaskById(subtask3Id);
        subtask302.setStatus(Statuses.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask302);
        Subtask subtask202 = inMemoryTaskManager.getSubtaskById(subtask2Id);
        subtask202.setStatus(Statuses.DONE);
        inMemoryTaskManager.updateSubtask(subtask202);

        task1 = inMemoryTaskManager.getTaskById(task1Id);
        task1 = inMemoryTaskManager.getTaskById(task1Id);
        epic1 = inMemoryTaskManager.getEpicById(epic1Id);
        epic2 = inMemoryTaskManager.getEpicById(epic2Id);
        subtask1 = inMemoryTaskManager.getSubtaskById(subtask1Id);
        subtask1 = inMemoryTaskManager.getSubtaskById(subtask1Id);
        subtask1 = inMemoryTaskManager.getSubtaskById(subtask1Id);
        subtask1 = inMemoryTaskManager.getSubtaskById(subtask1Id);


        for (Task task : Managers.getDefaultHistory().getHistory()) {
            System.out.println(task);
        }
}
```
