
// Este fichero es el main del ejecutable de la aplicación
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ConexionServidor {
    private static final String host = "34.175.149.140";
    // private static final String host = "localhost";
    private static String serverUrl = "ws://" + host + ":8080";
    private static WebSocketClient client;
    private static Semaphore semaphore = new Semaphore(0); // Semaforo de concurrencia
    private static boolean conexionEstablecida = false;

    // Si grafico es true se jugara con la interfaz grafica, sino se jugara
    // con la terminal
    public static void iniciar(boolean grafico, boolean verbose) {
        try {
            client = new ClienteWebSocket(new URI(serverUrl), new CountDownLatch(1));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Conectar el cliente al servidor
        client.connect();
        while (!client.isOpen()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Juego grafico o por terminal
        GestionPartida.iniciar(client);
        if (!grafico) {
            // Gestion del juego por terminal
            try {
                GestionPartida.partida(verbose);
            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Libera el semaforo
    public static void liberar() {
        semaphore.release();
    }

    // Bloquea el hilo hasta que se libere el semaforo
    public static void esperar() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Cierra la conexion con el cliente
    public static void cerrarConexion() {
        if (conexionEstablecida) {
            client.close();
        }
    }

    // Gestion de la conexion con webSockets
    private static class ClienteWebSocket extends WebSocketClient {
        private final CountDownLatch latch;

        public ClienteWebSocket(URI serverUri, CountDownLatch latch) {
            super(serverUri);
            this.latch = latch;
        }

        @Override
        public void onOpen(ServerHandshake handshakeData) {
            conexionEstablecida = true;
            System.out.println("Conectado al servidor");
            latch.countDown();
        }

        // Funcion que gestiona todos los mensajes recibidos
        @Override
        public void onMessage(String message) {
            GestionPartida.gestionMensaje(message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Conexión cerrada con código " + code + " y razón: " + reason);
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }

    }
}
