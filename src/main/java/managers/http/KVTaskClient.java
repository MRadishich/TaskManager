package main.java.managers.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private String apiToken;
    private final String url;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.client = HttpClient.newHttpClient();
        this.url = url;
        registration();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8080");

        kvTaskClient.put("Alex", "accounted");
        kvTaskClient.put("Joe", "programmer");
        System.out.println(kvTaskClient.load("Alex"));
        System.out.println(kvTaskClient.load("Joe"));
        kvTaskClient.put("Alex", "dead");
        System.out.println(kvTaskClient.load("Alex"));

    }

    private void registration() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

        apiToken = response.body();
    }

    public void put(String key, String value) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(value))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

        return response.toString();
    }
}
