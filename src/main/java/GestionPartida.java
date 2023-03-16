import java.net.URISyntaxException;
import java.util.Scanner;
import org.java_websocket.client.WebSocketClient;

public class GestionPartida {

    // ***** Información de estado partida *****
    private static boolean sesionIniciada = false;
    private static boolean enPartida = false;
    private static boolean empezarPartida = false;
    private static boolean dueñoPartida = false;
    private static WebSocketClient client;

    // ***** Información del usuario *****
    private static String nombreUser = "";
    private static int gemas = 0;
    private static String IDPartida = "";
    // ***********************************

    // Metodos públicos 
    public static void iniciar(WebSocketClient _client) {
        client = _client;
    }

    public static void partida() throws URISyntaxException, InterruptedException {

        // // ***********************  JUEGO ***********************
        Scanner scanner = new Scanner(System.in);
        iniciarSesion(client, scanner);
        menuInicial(client, scanner);
        // TODO: Meter todo esto en una funcion?
        if (dueñoPartida) {
            // Empezar la partida
            System.out.println("Introduzca un 1 para comenzar la partida: ");
            String empezar = scanner.nextLine();
            switch(empezar) {
                case "1":
                    empezarPartida(IDPartida);
                    break;
                default:
                    // TODO: Volver a preguntar hasta que quiera empezar
            }
        } else {
            // Esperar a que empiece
            while (!empezarPartida) {
                ConexionServidor.esperar();
            }
        }

        scanner.close();
    }

    private static void empezarPartida(String iDPartida2) {
    }

    public static void registrarse(String email, String contrasenya, String nombre) {
        client.send("registrarse," + email + "," + contrasenya + "," + nombre);
    }

    public static void iniciarSesion(String email, String contrasenya) {
        client.send("iniciarSesion," + email + "," + contrasenya);
    }
    
    // Metodo que se encarga de gestionar todos los mensajes recibidos
    public static void gestionMensaje(String message) {
        String[] partes = message.split(",");
        switch (partes[0]) {
            case "INICIO_OK":
                // Si hemos iniciado sesión correctamente
                nombreUser = partes[1];
                gemas = Integer.parseInt(partes[2]);
                sesionIniciada = true;
                break;
            case "INICIO_NO_OK":
                System.out.println("Error en inicio de sesión");
                break;
             case "REGISTRO_OK":
                System.out.println("Registro correcto");
                break;
            case "REGISTRO_NOOK":
                System.out.println("Error al registrarse");
                break;
            case "CREADAP_OK":
                enPartida = true;
                dueñoPartida = true;
                IDPartida = partes[1];
                break;
            case "CREADAP_NOOK":
                System.out.println("Error en crear partida");
                break;
            case "UNIRP_OK":
                enPartida = true;
                break;
            case "UNIRP_NO_OK":
                System.out.println("Error en unirse a partida");
                break;
            default:
                System.out.println("Mensaje no tenido en cuenta: " + message);
                return;
        }
        ConexionServidor.liberar(); // Igual no hay que liberar en todos los casos
    }

    // Metodos privados de gestion del juego por terminal

    // *************  INICIO SESION *************
    private static void iniciarSesion(WebSocketClient client, Scanner scanner) throws URISyntaxException, InterruptedException {
        String[] partes;
        String input;
        while (!sesionIniciada) {
            System.out.println("Elija que desea hacer: ");
            System.out.println("1 - Registrarse[mail,password,name] ");
            System.out.println("2 - Iniciar Sesion[mail,password]");
            String mensaje = scanner.nextLine();
            switch(mensaje) {
                case "1":
                    // Registrarse
                    System.out.println("Rellene los campos: [mail,password,name]: ");
                    input = scanner.nextLine();
                    partes = input.split(",");

                    if (partes.length == 3) {
                        String mail = partes[0];
                        String password = partes[1];
                        String name = partes[2];
                        registrarse(mail, password, name);
                    } else {
                        System.out.println("Debe ingresar los tres valores separados por coma");
                    }
                    break;
                case "2":
                    // Iniciar sesión
                    System.out.println("Rellene los campos: [mail,password]: ");
                    input = scanner.nextLine();
                    partes = input.split(",");
                    if (partes.length == 2) {
                        String mail = partes[0];
                        String password = partes[1];
                        iniciarSesion(mail, password); // Llamada a la función iniciarSesion con los datos obtenidos
                    } else {
                        System.out.println("Debe ingresar los dos valores separados por coma");
                    }
                    break;
                default:
                    // Opción incorrecta
                    System.out.println("Elija una opción válida");
                    break;
            }
        
            // Esperamos a recibir la respuesta del servidor
            ConexionServidor.esperar();
        }
    }

    // *************  MENU INICIAL *************
    private static void menuInicial (WebSocketClient client, Scanner scanner) throws URISyntaxException, InterruptedException {
        System.out.println("Bienvenido: " + nombreUser + ". Que desea hacer: ");
        System.out.println("1 - Crear partida");
        System.out.println("2 - Unirse a partida");
        while (!enPartida) {
            String mensaje = scanner.nextLine();
            switch (mensaje) {
                case "1":
                    crearPartida();
                    break;
                case "2":
                    System.out.println("Rellene los campos: [IDPartida]: ");
                    String IDPartida = scanner.nextLine();
                    unirsePartida(IDPartida);
                    break;
                default:
                    System.out.println("Elija una opción válida");
                    return;
            }
            // Esperamos a recibir la respuesta del servidor
            ConexionServidor.esperar();
        }
    }

    private static void unirsePartida(String _IDPartida) {
        client.send("iniciarSesion," + IDPartida + "," + nombreUser);
        IDPartida = _IDPartida;
    }

    private static void crearPartida() {
        client.send("crearPartida," + nombreUser);
    }
};