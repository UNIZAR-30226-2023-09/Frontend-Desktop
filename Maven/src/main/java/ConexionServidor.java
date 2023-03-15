// Este fichero es el main del ejecutable de la aplicación
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.util.concurrent.Semaphore;

public class ConexionServidor {
    private static final String host = "34.175.156.130";
    private static String serverUrl =  "ws://"+ host + ":8080";
    private static WebSocketClient client;
 
    // Variables locales a la clase que TIENEN que estar para que funcione
    // la logica del juego.

    // ***** Información del usuario *****
    public static String nombreUser = "";
    public static int gemas = 0;
    // ***********************************

    // ***** Información de estado partida *****
    public static boolean sesionIniciada = false;
    public static boolean empezarPartida = false;
    // *******************************************

    private static Semaphore semaphore = new Semaphore(0); // Semaforo de concurrencia
   
    // public static void main(String[] args) throws URISyntaxException, InterruptedException {
    //     partida();
    // }

    private static void liberar() {
        System.out.println("Liberando semaforo");
        semaphore.release();
    }

    private static void esperar() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Funcion principal de la partida
    public static void partida() throws URISyntaxException, InterruptedException {
        client = new ClienteWebSocket(new URI(serverUrl),new CountDownLatch(1));
        
        client.connect();
        while (!client.isOpen()) {
            Thread.sleep(100);
        }
        
        // // ***********************  JUEGO ***********************
        // Scanner scanner = new Scanner(System.in);
        // iniciarSesion(client, scanner);
        // // Sesion Iniciada
        // menuInicial(client, scanner);
        // scanner.close();
    }

    public static void enviarMensaje(String mensaje) {
        client.send(mensaje);
    }

    // ************* Metodos privados de gestion del juego *************

    // *************  INICIO SESION *************
    private static void iniciarSesion(WebSocketClient client, Scanner scanner) throws URISyntaxException, InterruptedException {
        while (!sesionIniciada) {
            System.out.println("Elija que desea hacer: ");
            System.out.println("1 - Registrarse[mail,password,name] ");
            System.out.println("2 - Iniciar Sesion[mail,password]");
            String mensaje = scanner.nextLine();
            if (mensaje.equals("1")) {
                // Registrarse
                System.out.println("Rellene los campos: [mail,password,name]: ");
                mensaje = scanner.nextLine();
                client.send("registrarse," + mensaje);

            } else if (mensaje.equals("2")) {
                // Iniciar sesion
                System.out.println("Rellene los campos: [mail,password]: ");
                mensaje = scanner.nextLine();
                client.send("iniciarSesion," + mensaje);
                
            } else {
                // Opcion incorrecta
                System.out.println("Elija una opción válida");
            }

            // Esperamos a recibir la respuesta del servidor
            esperar();
        }
    }

    // *************  MENU INICIAL *************
    private static void menuInicial (WebSocketClient client, Scanner scanner) throws URISyntaxException, InterruptedException {
        System.out.println("Bienvenido: " + nombreUser + ". Que desea hacer: ");
        System.out.println("1 - Crear partida");
        System.out.println("2 - Unirse a partida");
        // TODO: Skins, etc
        String mensaje = scanner.nextLine();
        if (mensaje.equals("1")) {
            // Crear partida
            client.send("crearPartida," + nombreUser);

        } else if (mensaje.equals("2")) {
            // Iniciar sesion
            System.out.println("Rellene los campos: [IDPartida]: ");
            String IDPartida = scanner.nextLine();
            client.send("iniciarSesion," + IDPartida + "," + nombreUser);
        } else {
            // Opcion incorrecta
            System.out.println("Elija una opción válida");
        }
    }

    private static class ClienteWebSocket extends WebSocketClient {
        private final CountDownLatch latch;
    
        public ClienteWebSocket(URI serverUri, CountDownLatch latch) {
            super(serverUri);
            this.latch = latch;
        }
    
        @Override
        public void onOpen(ServerHandshake handshakeData) {
            System.out.println("Conectado al servidor");
            latch.countDown();
        }
        
        // Funcion que gestiona todos los mensajes recibidos
        @Override
        public void onMessage(String message) {
            System.out.println("Mensaje recibido: " + message);
            String[] partes = message.split(",");
            System.out.println(partes[0]);
            if (partes[0].equals("INICIO_OK")) {
                // Si hemos iniciado sesion correctamente
                nombreUser = partes[1];
                gemas = Integer.parseInt(partes[2]); 
                sesionIniciada = true;
            } else if (partes[0].equals("INICIO_NO_OK")){
                System.out.println("Error en inicio de sesion");
            }
            liberar(); // TODO: Igual no hay que liberar en todos los casos
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
