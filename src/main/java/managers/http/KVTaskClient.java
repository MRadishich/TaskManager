package main.java.managers.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private String apiToken;
    private final String urlKVServer;

    public KVTaskClient(String url) {
        this.client = HttpClient.newHttpClient();
        this.urlKVServer = url;
        registration();
    }

    private void registration() {
        try {
            final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(urlKVServer + "/register"))
                    .build();

            HttpResponse<String> response = client.send(request, handler);

            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + urlKVServer + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }
    }

    public void put(String key, String value) {
        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .uri(URI.create(urlKVServer + "/save/" + key + "?API_TOKEN=" + apiToken))
                    .build();

            client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + urlKVServer + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(urlKVServer + "/load/" + key + "?API_TOKEN=" + apiToken))
                    .build();

            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);

            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + urlKVServer + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        return "";
    }
}
