package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.praktikum.app.models.Epic;
import com.praktikum.app.models.Subtask;
import com.praktikum.app.models.Task;
import com.praktikum.app.models.utils.Status;
import com.praktikum.app.services.Managers;
import com.praktikum.app.services.http.HttpTaskServer;
import com.praktikum.app.services.http.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {
    KVServer kvServerTest;
    HttpTaskServer httpServerTest;
    Gson gson = Managers.buildGsonWithLocalDateTimeAdapter();

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        kvServerTest = new KVServer();
        kvServerTest.start();
        httpServerTest = new HttpTaskServer();
        httpServerTest.start();

        Epic epic = new Epic("Эпик-1", "Описание эпика с id 1");

        Subtask subtask = new Subtask("Подзадача-1", "Описание подзадачи с id 2", Status.NEW, 1,
                LocalDateTime.of(2023, 3, 26, 11, 11), 11);

        Task task = new Task("Задача-1", "Описание задачи с id 3", Status.NEW,
                LocalDateTime.of(2024, 3, 1, 1, 1), 11);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> responseEpic = client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> responseSub = client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> responseTask = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseEpic.statusCode(), "неверный статус-код");
        assertEquals(200, responseSub.statusCode(), "неверный статус-код");
        assertEquals(200, responseTask.statusCode(), "неверный статус-код");
    }

    @AfterEach
    void stopHttpTaskManagerTest() {
        httpServerTest.stop();
        kvServerTest.stop();
    }

    @Test
    void testGetEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> list = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(1, list.size(), "Неверное количество задач");
    }

    @Test
    void testGetSubtasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> list = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(1, list.size(), "Неверное количество задач");
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> mapTasks = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "неверный статус-код");
        assertNotNull(mapTasks, "Задачи не возвращаются");
        assertEquals(1, mapTasks.size(), "Неверное количество задач");
    }

    @Test
    void testGetEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/tasks/epic?id=1")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task received = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(received, "Задачи не возвращаются");
    }

    @Test
    void testGetSubtaskById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/tasks/subtask?id=2")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task received = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "неверный статус-код");
        assertNotNull(received, "Задачи не возвращаются");
    }

    @Test
    void testGetTaskById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/tasks/task?id=3")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task received = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "неверный статус-код");
        assertNotNull(received, "Задачи не возвращаются");
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask("Subtask id 4", "Описание подзадачи4", Status.NEW, 1,
                LocalDateTime.of(2023, 5, 26, 11, 11), 11);

        String str = gson.toJson(newSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask?id=4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неверный статус-код");
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task newTask = new Task("AddTask", "Description", Status.NEW,
                LocalDateTime.of(2444, 4, 14, 14, 14), 14);

        String str = gson.toJson(newTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task?id=4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteAllEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testGetHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }
}
