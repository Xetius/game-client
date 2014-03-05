package com.xetius;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameClient {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String BASE_URL = "http://localhost:8080/";

    public String login(int userId) throws IOException {
        String url = new StringBuilder(BASE_URL).append(userId).append("/").append("login").toString();
        return doGet(url);
    }

    public void scoreForLevel(int level, String sessionId, int score) throws Exception {
        String url = new StringBuilder(BASE_URL).append(level).append("/").append("score").toString();
        Map<String, String> params = new HashMap<String, String>();
        params.put("score", new Integer(score).toString());
        params.put("sessionkey", sessionId);
        doPost(url, params);
    }

    public String getHighScores(int level) throws IOException {
        String url = new StringBuilder(BASE_URL).append(level).append("/").append("highscorelist").toString();
        return doGet(url);
    }

    private String doGet(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending GET to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private void doPost(String urlString, Map<String, String> params) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-gb,en;q=0.5");

        String urlParameters = buildParams(params);

        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
    }

    private String buildParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while(iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            Map.Entry<String, String> entry = iterator.next();
            builder.append(entry.getKey());

            if (entry.getValue() != null) {
                builder.append("=").append(entry.getValue());
            }

            iterator.remove();
        }
        return  builder.toString();
    }

}
