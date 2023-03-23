import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;
import org.java_websocket.client.WebSocketClient;

public class GestionPartida {

    // ***** Información de estado partida *****
    private static boolean sesionIniciada = false;
    private static boolean enPartida = false;
    private static boolean empezarPartida = false;
    private static boolean dueñoPartida = false; 
    private static boolean miTurno = false;
    private static WebSocketClient client;
    private static boolean enCarcel = false;
    private static int turnosCarcel = 0;

    // ***** Información del usuario *****
    private static String nombreUser = "";
    private static int gemas = 0;
    private static String IDPartida = "";
    private static String[] ordenJugadores = new String[4];
    private static int casilla = 1;
    private static int dinero = 1000;
    private static int[] dados = new int[2];
    private static int dineroBote;
    private static ArrayList<String> propiedades = new ArrayList<String>();
    private static boolean comprarPropiedad;
    private static String propiedadAComprar = "";

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
        empezarPartida(client, scanner);
        jugarPartida(client, scanner);
        scanner.close();
    }

    public static void unirsePartida(String _IDPartida) {
        client.send("unirsePartida," + _IDPartida + "," + nombreUser);
        IDPartida = _IDPartida;
    }

    public static void crearPartida() {
        client.send("crearPartida," + nombreUser);
    }

    public static void empezarPartida(String iDPartida) {
        client.send("empezarPartida," + iDPartida + "," + nombreUser); 
    }

    public static void registrarse(String email, String contrasenya, String nombre) {
        client.send("registrarse," + email + "," + contrasenya + "," + nombre);
    }

    public static void iniciarSesion(String email, String contrasenya) {
        client.send("iniciarSesion," + email + "," + contrasenya);
    }

    public static void lanzarDados(String nombreUser, String iDPartida) {
        client.send("lanzarDados," + nombreUser + "," + iDPartida);
    }
    
    // Metodo que se encarga de gestionar todos los mensajes recibidos
    public static void gestionMensaje(String message) {
        System.out.println(message);
        String[] partes = message.split(",");
        switch (partes[0]) {
            case "INICIO_OK":
                // Si hemos iniciado sesión correctamente
                nombreUser = partes[1];
                gemas = Integer.parseInt(partes[2]);
                sesionIniciada = true;
                SignInFormController.sesionIniciada = true;
                break;
            case "INICIO_NO_OK":
                System.out.println("Error en inicio de sesión");
                break;
             case "REGISTRO_OK":
                System.out.println("Registro correcto");
                break;
            case "REGISTRO_NO_OK":
                System.out.println("Error al registrarse");
                break;
            case "CREADAP_OK":
                enPartida = true;
                dueñoPartida = true;
                IDPartida = partes[1];
                System.out.println("El id de la partida es:" + IDPartida);
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
            case "EMPEZAR_OK":
                empezarPartida = true;
                enPartida = true;

                // Almacenar orden de tiradas
                ordenJugadores[0] = partes[2];
                ordenJugadores[1] = partes[3];
                ordenJugadores[2] = partes[4];
                ordenJugadores[3] = partes[5];

                // Almacenar posicion inicial y dinero inicial
                casilla = 1;
                dinero = 1000;
                break;
            case "EMPEZAR_NO_OK":
                System.out.println("Error en empezar partida");
                break;
            case "TURNO":
                miTurno = true;
                // TODO: ver si sigo en la carcel?
                break;
            case "DADOS":
                dados[0] = Integer.parseInt(partes[1]);
                dados[1] = Integer.parseInt(partes[2]);
                casilla =  Integer.parseInt(partes[3]);
                if (Integer.parseInt(partes[3]) > 0) {
                    enCarcel = true;
                    turnosCarcel = Integer.parseInt(partes[3]);
                } else {
                    enCarcel = false;
                    turnosCarcel = 0;
                }
                break;
            case "NUEVO_DINERO_JUGADOR":
                dinero = Integer.parseInt(partes[2]);
                break;
            case "NUEVO_DINERO_BOTE":
                dineroBote = Integer.parseInt(partes[1]);
                break;
            case "OBTENER_BOTE":
                dinero = Integer.parseInt(partes[2]);
                break;
            case "VENDER_OK":
                propiedades.remove(partes[1]);
                dinero = Integer.parseInt(partes[2]);
                break;
            case "VENDER_NO_OK":
                // TODO: ?
                break;
            case "QUIERES_COMPRAR_PROPIEDAD":
                comprarPropiedad = true;
                propiedadAComprar = partes[1];
                break;
            case "DENTRO_CARCEL":
                enCarcel = true;
                System.out.println("Estas en la carcel durante " + partes[1]);
                break;
            case "SALIR_CARCEL":
                break;
            case "NADA":
                break;
            case "NUEVO_DINERO_ALQUILER":
                System.out.println("Has caido en la propiedad de otro jugador. Tu nuevo saldo es: " + partes[1]);
                dinero = Integer.parseInt(partes[1]);
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
                        // Esperamos a recibir la respuesta del servidor
                        ConexionServidor.esperar();
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
                        // Esperamos a recibir la respuesta del servidor
                        ConexionServidor.esperar();
                    } else {
                        System.out.println("Debe ingresar los dos valores separados por coma");
                    }
                    break;
                default:
                    // Opción incorrecta
                    System.out.println("Elija una opción válida");
                    break;
            }
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

    private static void empezarPartida(WebSocketClient client, Scanner scanner) {
        if (dueñoPartida) {
            // Empezar la partida
            while(!empezarPartida) {
                System.out.println("Introduzca un 1 para comenzar la partida: ");
                String empezar = scanner.nextLine();
                switch(empezar) {
                    case "1":
                        empezarPartida(IDPartida);
                        ConexionServidor.esperar();
                        break;
                    default:
                        // Volver a preguntar hasta que quiera empezar
                        break;
                }
            }
        } else {
            // Esperar a que empiece
            while (!empezarPartida) {
                ConexionServidor.esperar();
            }
        }
    }

    private static void jugarPartida(WebSocketClient client, Scanner scanner) { 
        System.out.println("Empieza la partida");
        while(enPartida) {
            System.out.println("Esperando turno ");
            while(!miTurno) {
                mostrarInfoJugador();
                System.out.println("Es tu turno, lanzando dados ");
                // Lanzar los dados
                lanzarDados(nombreUser,IDPartida);
                // Esperamos a recibir la respuesta del servidor
                ConexionServidor.esperar();
                
                // Si no sigo este turno en la carcel
                if (!enCarcel) {
                    if (comprarPropiedad) {
                        System.out.println("Introduzca un 1 si desea comprar la propiedad: " + propiedadAComprar);
                        if (scanner.nextLine().equals("1")) {
                            comprarPropiedad(client);
                        }
                        comprarPropiedad = false;
                    }
                    
                }
                // Actualizar posicion jugador y hacer accion correspondiente

                // Puedo recibir :
                //  - Casilla para comprar
                //  - Casilla suerte/cajacomunidad
                //  - Ir a la carcel
                //  - Propiedad que ya tiene dueño
                //  - Casillas banco y casino
                finTurno(client);
                miTurno = false;
            }
            // Esperamos a recibir la respuesta del servidor
            // Finalizamos el turno
            ConexionServidor.esperar();
        }
    }

    public static void finTurno(WebSocketClient client) {
        client.send("finTurno");
    }

    public static void comprarPropiedad(WebSocketClient client) {
        client.send("SI_COMPRAR_PROPIEDAD," + nombreUser + "," + IDPartida);
    }

    private static void mostrarInfoJugador() {
        System.out.println("+-----------------------------------------+");
        System.out.println(String.format("| %-20s | %10s |", "Nombre", nombreUser));
        System.out.println("+----------------------+------------------+");
        System.out.println(String.format("| %-20s | %,10d |", "Dinero", dinero));
        System.out.println("+----------------------+------------------+");
        System.out.println(String.format("| %-20s | %10d |", "Casilla actual", casilla));
        System.out.println("+----------------------+------------------+");
        System.out.println(String.format("| %-20s | %,10d |", "Dinero en el bote", dinero));
        System.out.println("+-----------------------------------------+");
    }
        
};