package Http;

import KVServer.KVServer;
import Managers.Http.HttpTaskManager;
import Managers.Http.HttpTaskServer;
import Managers.Managers;
import Managers.TaskManager.TaskManager;
import Tasks.Enums.Statuses;
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

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getDefault();

        int task1Id = taskManager.createTask(task = new Task("Task1", "Описание таски",
                Statuses.NEW, 20, "23.01.2024;15:04"));
        int epic1Id = taskManager.createEpic(epic = new Epic("Epic1", "Описание эпика", Statuses.NEW));
        int subtask1Id = taskManager.createSubtask(
               subtask = new Subtask("Subtask1", "Описание сабтаски", Statuses.NEW, epic1Id,
                        10, "23.01.2024;16:04")
        );

        taskManager.getEpicById(epic1Id);
        taskManager.getSubtaskById(subtask1Id);

        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void afterEach(){
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void testShouldReturnAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        System.out.println(response.body());
    }

    @Test
    void testShouldReturnTaskById() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Task4", "Описание таски",
                Statuses.NEW, 20, "27.01.2024;19:44"));
        taskManager.createTask(new Task("Task4", "Описание таски",
                Statuses.NEW, 20, "28.01.2024;19:44"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().header("Accept", "application/txt").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    @Test
    void testShouldReturnHistory() throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        System.out.println(response.body());
    }
}
