package main.java.main;

import main.java.managers.http.KVServer;

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
//
//        TaskManager manager = Managers.getDefaultTaskManager("http://localhost:8078");

//        new HttpTaskServer(manager).start();
//
//        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #1,Make 100 push up,null,null,null", ","));
//        manager.createTask(TaskDTO.getTaskDTO("null,EPIC,Epic #2,Make 500 push up,null,null,null", ","));
//        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #1,Make 50 push up,DONE,540,2023-04-01T09:00,0", ","));
//        manager.createTask(TaskDTO.getTaskDTO("null,SUB,SubTask #2,Make 50 push up,DONE,540,2023-04-02T09:00,0", ","));
    }
}