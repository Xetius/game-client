package com.xetius;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static int USER_COUNT = 20;
    private static int MAX_COUNT = 10;
    private static int MAX_LEVEL = 10;

    private GameClient client;

    public static void main(String[] args) throws Exception {

        Main main = new Main();
        main.run();
    }

    private void run() throws Exception {
        client = new GameClient();

        Random random = new Random();

        List<String> users = createUsers();

        for(int count = 0; count < MAX_COUNT; count++) {
            for(int level = 1; level <= MAX_LEVEL; level++) {
                for(String user : users) {
                    client.scoreForLevel(level, user, random.nextInt(1000000));
                }
            }
        }

        for(int level = 1; level <= MAX_LEVEL; level++) {
            System.out.println(client.getHighScores(level));
        }
    }

    private List<String> createUsers() throws IOException {
        List<String> users = new ArrayList<String>();
        for(int userId = 1; userId <= USER_COUNT; userId++) {
            users.add(client.login(userId));
        }
        return users;
    }
}
