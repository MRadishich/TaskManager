package main.java.main;

import main.java.managers.http.HttpTaskServer;

import java.io.IOException;

class Main {
    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = null;

        try {
            httpTaskServer = new HttpTaskServer();
        } catch (IOException e) {
            System.out.println("Error");
        }

        httpTaskServer.run();
    }


}