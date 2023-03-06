// Compilar con mvn compile
// Ejecutar con mvn exec:java -Dexec.mainClass=WebSocketExample
// Para limpiar los ejecutables usar mvn clean
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketExample {
    public static final String host = "34.175.156.130";
    // public static final String port = "8080";
    // public static final String websocket = "ws://" + host + ":" + port;

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        String serverUrl =  "ws://"+ host + ":8080"; // cambiar por la URL de tu servidor WebSocket
        
        WebSocketClient client = new WebSocketClient(new URI(serverUrl)) {
            private final CountDownLatch latch = new CountDownLatch(1);
            
            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("Conectado al servidor");
                latch.countDown();
            }
            
            @Override
            public void onMessage(String message) {
                System.out.println("Mensaje recibido: " + message);
            }
            
            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Conexión cerrada con código " + code + " y razón: " + reason);
            }
            
            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        
        client.connect();
        while (!client.isOpen()) {
            Thread.sleep(100);
        }
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Ingrese su mensaje: ");
            String message = scanner.nextLine();
            client.send(message);
           // client.
        }
    }
    
    private static class CustomWebSocketClient extends WebSocketClient {
        private final CountDownLatch latch;
    
        public CustomWebSocketClient(URI serverUri, CountDownLatch latch) {
            super(serverUri);
            this.latch = latch;
        }
    
        @Override
        public void onOpen(ServerHandshake handshakeData) {
            System.out.println("Conectado al servidor");
            latch.countDown();
        }
    
        @Override
        public void onMessage(String message) {
            System.out.println("Mensaje recibido: " + message);
        }
    
        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Conexión cerrada con código " + code + " y razón: " + reason);
        }
    
        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    
        public CountDownLatch getLatch() {
            return latch;
        }
    }
    
}
