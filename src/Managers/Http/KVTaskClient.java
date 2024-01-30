package Managers.Http;

import Managers.Exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;
    public KVTaskClient(int port) {
        url = "http://localhost:" + port + "/";
        apiToken = register(url);
    }
    private String register(String url){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register/"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                throw new ManagerSaveException("Ошибка ответа. Код ответа: " + response.statusCode());
            }
            return response.body();
        }catch (IOException | InterruptedException ex){
            throw new ManagerSaveException("Ошибка отпраки запроса или обработки ответа");
        }
    }
    public String load(String key){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                throw new ManagerSaveException("Ошибка запроса. Код ответа: " + response.statusCode());
            }
            return response.body();
        }catch (IOException | InterruptedException ex){
            //throw new ManagerSaveException("Ошибка отпраки запроса или обработки ответа");
            System.out.println(ex.getMessage());
            return null;
        }
    }
    public void put(String key, String value){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200){
                throw new ManagerSaveException("Ошибка запроса. Код ответа: " + response.statusCode());
            }
        }catch (IOException | InterruptedException ex){
            throw new ManagerSaveException("Ошибка отпраки запроса или обработки ответа");
        }
    }
}
