Это репозиторий технического задания №6
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

1) Для упаравления классамы были реализованы специальный класс и итерфейс.
2) Реализован класс и интерфейс для отслеживания истории просмотров задач.
3) Реализован новый менеджер, который сохраняет задачи и историю задач в файл, а также достаёт их оттуда

Пример работы программы:

```java
public static void main(String[] args) {
        //TaskManager taskManager = Managers.getDefault();
        File file = new File("tasks.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.getName());
final int task1Id = fileBackedTasksManager.createTask(
        new Task("Task1-1", "Первая таска", Statuses.NEW));
final int epic1Id = fileBackedTasksManager.createEpic(
        new Epic("Epic1-1", "Первый эпик", Statuses.NEW));
final int subtask1Id = fileBackedTasksManager.createSubtask(
        new Subtask("Подзадача1-1-1", "Первая подзадача первого эпика", Statuses.NEW, epic1Id));

        fileBackedTasksManager.getSubtaskById(subtask1Id);
        fileBackedTasksManager.getEpicById(epic1Id);

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(file);
        System.out.println("Из файла");
        System.out.println(fileBackedTasksManager1.getHistory());
        System.out.println();
        System.out.println(fileBackedTasksManager1.getEpics());
        }
```
