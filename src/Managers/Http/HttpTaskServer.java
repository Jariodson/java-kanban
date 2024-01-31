package Managers.Http;

import Managers.Managers;
import Managers.TaskManager.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public static void main(String[] args) throws IOException {
        final HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

    private void handler(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка события /tasks");
        try (httpExchange) {
            String endpoint = httpExchange.getRequestURI().getPath().substring("/tasks/".length());
            switch (endpoint) {
                case "" -> allTasksHandler(httpExchange);
                case "task/" -> taskHandler(httpExchange);
                case "epic/" -> epicHandler(httpExchange);
                case "subtask/" -> subtaskHandler(httpExchange);
                case "subtask/epic/" -> subtaskEpicHandler(httpExchange);
                case "history/" -> historyHandler(httpExchange);
                default -> writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    private void allTasksHandler(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            System.out.println("Вывод всех задач");
            final String responses = gson.toJson(taskManager.getPrioritizedTasks());
            writeResponse(httpExchange, responses, 200);
        } else {
            writeResponse(httpExchange, "Неверный метод! Ожидался метод GET", 400);
        }
    }

    private void taskHandler(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET" -> {
                String id;
                if (httpExchange.getRequestURI().getQuery() != null) {
                    id = httpExchange.getRequestURI().getQuery().substring("id=".length());
                } else {
                    id = "";
                }
                if (id.isEmpty()) {
                    System.out.println("Вывод тасков");
                    final List<Task> tasks = taskManager.getTasks();
                    writeResponse(httpExchange, gson.toJson(tasks), 200);
                    return;
                }
                if (!taskManager.getTasks().isEmpty()
                        && taskManager.getTasks().contains(taskManager.getTaskById(Integer.parseInt(id)))) {
                    System.out.println("Вывод таски по id: " + id);
                    final String taskByIdInJson = gson.toJson(taskManager.getTaskById(Integer.parseInt(id)));
                    writeResponse(httpExchange, taskByIdInJson, 200);
                    return;
                }
                writeResponse(httpExchange, "Данного id:" + id + " не существует!", 404);
            }
            case "POST" -> {
                System.out.println("Запись тасков");
                for (Task task : readJason(httpExchange)) {
                    if (taskManager.getTasks().stream()
                            .map(Task::getId)
                            .collect(Collectors.toList())
                            .contains(task.getId())) {
                        try {
                            taskManager.updateTask(task);
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                        writeResponse(httpExchange, "Таска обновлена!", 201);
                    } else {
                        taskManager.createTask(task);
                        writeResponse(httpExchange, "Таска записана!", 201);
                    }
                }
            }
            case "DELETE" -> {
                System.out.println("Удаление таски");
                String id = httpExchange.getRequestURI().getQuery().substring("id=".length());
                taskManager.deleteTaskById(Integer.parseInt(id));
                writeResponse(httpExchange, "Таска удалена!", 201);
            }
        }
    }

    private void epicHandler(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET" -> {
                String id;
                if (httpExchange.getRequestURI().getQuery() != null) {
                    id = httpExchange.getRequestURI().getQuery().substring("id=".length());
                } else {
                    id = "";
                }
                if (id.isEmpty()) {
                    System.out.println("Вывод эпиков");
                    final List<Epic> tasks = taskManager.getEpics();
                    writeResponse(httpExchange, gson.toJson(tasks), 200);
                    return;
                }
                if (taskManager.getEpics().contains(taskManager.getEpicById(Integer.parseInt(id)))) {
                    System.out.println("Вывод эпика по id: " + id);
                    final String epicByIdInJson = gson.toJson(taskManager.getEpicById(Integer.parseInt(id)));
                    writeResponse(httpExchange, epicByIdInJson, 200);
                } else {
                    writeResponse(httpExchange, "Данного id:" + id + " не существует!", 404);
                }
            }
            case "POST" -> {
                System.out.println("Запись эпиков");
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), UTF_8);
                Collection<Epic> epics;
                try {
                    Type collectionType = new TypeToken<Collection<Task>>() {
                    }.getType();
                    epics = gson.fromJson(body, collectionType);
                } catch (Exception exception) {
                    epics = Collections.singleton(gson.fromJson(body, Epic.class));
                }
                for (Epic epic : epics) {
                    if (taskManager.getEpics().stream()
                            .map(Task::getId)
                            .collect(Collectors.toList())
                            .contains(epic.getId())) {
                        try {
                            taskManager.updateEpic(epic);
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                        writeResponse(httpExchange, "Эпик обновлен!", 201);
                    } else {
                        taskManager.createEpic(epic);
                        writeResponse(httpExchange, "Эпик записан!", 201);
                    }
                }
            }
            case "DELETE" -> {
                System.out.println("Удаление эпика");
                String id = httpExchange.getRequestURI().getQuery().substring("id=".length());
                taskManager.deleteEpicById(Integer.parseInt(id));
                writeResponse(httpExchange, "Эпик удален!", 201);
            }
        }
    }

    private void subtaskHandler(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET" -> {
                String id;
                if (httpExchange.getRequestURI().getQuery() != null) {
                    id = httpExchange.getRequestURI().getQuery().substring("id=".length());
                } else {
                    id = "";
                }
                if (id.isEmpty()) {
                    System.out.println("Вывод сабтасков");
                    final List<Subtask> tasks = taskManager.getSubtasks();
                    writeResponse(httpExchange, gson.toJson(tasks), 200);
                    return;
                }
                if (taskManager.getSubtasks().contains(taskManager.getSubtaskById(Integer.parseInt(id)))) {
                    System.out.println("Вывод сабтаски по id: " + id);
                    final String epicByIdInJson = gson.toJson(taskManager.getSubtaskById(Integer.parseInt(id)));
                    writeResponse(httpExchange, epicByIdInJson, 200);
                } else {
                    writeResponse(httpExchange, "Данного id:" + id + " не существует!", 404);
                }
            }
            case "POST" -> {
                System.out.println("Запись сабтасков");
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), UTF_8);
                Collection<Subtask> subtaskCollection;
                try {
                    Type collectionType = new TypeToken<Collection<Task>>() {
                    }.getType();
                    subtaskCollection = gson.fromJson(body, collectionType);
                } catch (Exception exception) {
                    subtaskCollection = Collections.singleton(gson.fromJson(body, Subtask.class));
                }
                for (Subtask subtask : subtaskCollection) {
                    if (taskManager.getSubtasks().stream()
                            .map(Task::getId)
                            .collect(Collectors.toList())
                            .contains(subtask.getId())) {
                        try {
                            taskManager.updateSubtask(subtask);
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                        writeResponse(httpExchange, "Сабтаска обновлена!", 201);
                    } else {
                        taskManager.createSubtask(subtask);
                        writeResponse(httpExchange, "Сабтаска записана!", 201);
                    }
                }
            }
            case "DELETE" -> {
                System.out.println("Удаление сабтаски");
                String id = httpExchange.getRequestURI().getQuery().substring("id=".length());
                taskManager.deleteSubtaskById(Integer.parseInt(id));
                writeResponse(httpExchange, "Сабтаска удалена!", 201);
            }
        }
    }

    private void historyHandler(HttpExchange httpExchange) throws IOException {
        System.out.println("Вывод истории");
        final String historyString = gson.toJson(taskManager.getHistory());
        writeResponse(httpExchange, historyString, 200);
    }

    private void subtaskEpicHandler(HttpExchange httpExchange) throws IOException {
        System.out.println("Вывод сабтасков указанного эпика");
        String id = httpExchange.getRequestURI().getQuery().substring("subtask/epic/".length());
        final String taskString = gson.toJson(taskManager.getEpicSubtasksById(Integer.parseInt(id)));
        writeResponse(httpExchange, taskString, 200);
    }

    private Collection<Task> readJason(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), UTF_8);
        try {
            Type collectionType = new TypeToken<Collection<Task>>() {
            }.getType();
            return gson.fromJson(body, collectionType);
        } catch (Exception exception) {
            return Collections.singleton(gson.fromJson(body, Task.class));
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks/");
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

}
