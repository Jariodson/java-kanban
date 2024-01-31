package Http;

import KVServer.KVServer;
import Managers.Http.HttpTaskManager;
import Managers.Http.HttpTaskServer;
import Managers.Managers;
import Managers.TaskManager.TaskManager;
import Tasks.Enums.Status;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest {
    private TaskManager taskManager;

    private HttpTaskServer httpTaskServer;
    private KVServer server;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void beforeEach() throws IOException {
        server = new KVServer();
        server.start();
        taskManager = Managers.getDefault();

        int task1Id = taskManager.createTask(task = new Task("Task1", "Описание таски",
                Status.NEW, 20, "23.01.2024;15:04"));
        int epic1Id = taskManager.createEpic(epic = new Epic("Epic1", "Описание эпика", Status.NEW));
        int subtask1Id = taskManager.createSubtask(
                subtask = new Subtask("Subtask1", "Описание сабтаски", Status.NEW, epic1Id,
                        10, "23.01.2024;16:04")
        );

        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void afterEach() {
        server.stop();
        httpTaskServer.stop();
    }

    @Test
    void testShouldReturnTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        System.out.println(response.body());
    }

    @Test
    void testShouldReturnTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().header("Accept", "application/txt").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    @Test
    void testShouldReturnNewTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = Managers.getGson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(body).header("Accept", "application/txt").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpTaskManager httpTaskManager = new HttpTaskManager(8078, true);
        System.out.println(httpTaskManager.getTasks());
    }

    @Test
    void testShouldReturnNewEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = Managers.getGson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).header("Accept", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpTaskManager httpTaskManager = new HttpTaskManager(8078, true);
        System.out.println(httpTaskManager.getEpics());
    }

    @Test
    void testShouldReturnNewSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = Managers.getGson();
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).header("Accept", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpTaskManager httpTaskManager = new HttpTaskManager(8078, true);
        System.out.println(httpTaskManager.getSubtasks());
    }
}
