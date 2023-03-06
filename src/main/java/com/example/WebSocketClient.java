package com.example;

import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import javax.websocket.ContainerProvider;

@ClientEndpoint
public class WebSocketClient {
    private CountDownLatch latch;
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Conexión establecida");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Mensaje recibido: " + message);
        latch.countDown();
    }

    @OnError
    public void onError(Throwable t) {
        System.err.println("Error en el websocket: " + t.getMessage());
        latch.countDown();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Conexión cerrada: " + reason);
        latch.countDown();
    }

    public void connect(String uri) {
        latch = new CountDownLatch(1);
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
        } catch (URISyntaxException | DeploymentException | IOException ex) {
            System.err.println(ex);
        }
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
