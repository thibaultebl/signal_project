package com.data_management;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;

public class WebSocketClient implements Listener {

    private WebSocket webSocket;
    private final String serverUrl;
    private final DataStorage dataStorage;

    public WebSocketClient(String serverUrl, DataStorage dataStorage) {
        this.serverUrl = serverUrl;
        this.dataStorage = dataStorage;
    }


    public void connect() {
        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(serverUrl), this)
                .thenAccept(ws -> {
                    this.webSocket = ws;
                    System.out.println("connected to : " + serverUrl);
                })
                .exceptionally(e -> {
                    System.err.println("connection error : " + e.getMessage());
                    return null;
                });
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Session terminée");
            System.out.println("🔌 Connexion WebSocket fermée.");
        }
    }

}
