// Compilar con mvn compile
// Ejecutar con mvn exec:java -Dexec.mainClass=ConexionServidor
// Para limpiar los ejecutables usar mvn clean
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ConexionServidor {
    private static final String host = "34.175.156.130";
    private static String serverUrl =  "ws://"+ host + ":8080";
 
    // Variables locales a la clase que TIENEN que estar para que funcione
    // la logica del juego.
    private static boolean sesionIniciada = false;
    private static String nombreUser = "";
    private static int gemas = 0;
    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        partida();
    }

    public static void partida() throws URISyntaxException, InterruptedException {
        WebSocketClient client = new ClienteWebSocket(new URI(serverUrl),new CountDownLatch(1));
        
        client.connect();
        while (!client.isOpen()) {
            Thread.sleep(100);
        }
        
        Scanner scanner = new Scanner(System.in);
        // while (true) {
        //     System.out.print("Ingrese su mensaje: ");
        //     String message = scanner.nextLine();
        //     client.send(message);
        // }

        // ***********************  JUEGO ***********************

        // *************  INICIO SESION *************
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

            // Esperamos medio segundo a recibir la respuesta del servidor
            Thread.sleep(500);
        }
        // Sesion Iniciada
        // *************  MENU INICIAL *************
        System.out.println("Bienvenido: " + nombreUser + ". Que desea hacer: ");
        System.out.println("1 - Crear partida");
        System.out.println("2 - Unirse a partida");
        // TODO: Skins, etc


        scanner.close();
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
            if (partes[0] == "INICIO_OK") {
                // Si hemos iniciado sesion correctamente
                nombreUser = partes[1];
                gemas = Integer.parseInt(partes[1]); 
                sesionIniciada = true;
            } else if (partes[0] == "INICIO_NO_OK"){
                System.out.println("Error en inicio de sesion");
            }
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
