package main.java.main;

import main.java.managers.http.KVServer;

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
    }
}