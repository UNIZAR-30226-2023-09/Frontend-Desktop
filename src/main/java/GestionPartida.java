import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;
import org.java_websocket.client.WebSocketClient;

public class GestionPartida {

    private static boolean verbose = false;

    // ***** Información de estado partida *****
    public static boolean sesionIniciada = false;
    public static boolean enPartida = false;
    public static boolean empezarPartida = false;
    public static boolean dueñoPartida = false;
    public static boolean miTurno = false;
    public static WebSocketClient client;
    public static boolean enCarcel = false;
    public static int turnosCarcel = 0;
    public static int indiceJugador = -1;

    // ***** Información del usuario *****
    public static String nombreUser = "";
    public static int gemas = 0;
    public static String IDPartida = "";
    public static String[] ordenJugadores = new String[4];
    public static String[] posicionesJugadores = { "1", "1", "1", "1" };
    public static int[] dineroJugadores = { 1000, 1000, 1000, 1000 };
    public static int[] dados = new int[2];
    public static int dineroBote = 0;
    public static ArrayList<ArrayList<String>> vectorDePropiedades = new ArrayList<ArrayList<String>>();

    public static boolean comprarPropiedad = false;
    public static String propiedadAComprar;
    public static boolean meToca = false;
    public static boolean compraRealizada;

    private final static String[] tablero = { "nada", "Salida", "Monterrey", "Guadalajara", "Treasure", "Tax",
            "AeropuertoNarita", "Tokio", "Kioto", "Superpoder", "Osaka", "Carcel", "Roma", "Milan", "Casino", "Napoles",
            "Aeropuerto Heathrow", "Londres", "Superpoder", "Manchester", "Edimburgo", "Bote", "Madrid",
            "Barcelona", "Treasure", "Zaragoza", "AeropuertoOrly", "Paris", "Banco", "Marsella",
            "Lyon", "IrCarcel", "Toronto", "Vancouver", "Treasure", "Ottawa", "AeropuertoDeLosAngeles",
            "NuevaYork", "LosAngeles", "LuxuryTax", "Chicago" };
    // ***********************************

    // Metodos públicos
    public static void iniciar(WebSocketClient _client) {
        client = _client;
        vectorDePropiedades.add(new ArrayList<String>());
        vectorDePropiedades.add(new ArrayList<String>());
        vectorDePropiedades.add(new ArrayList<String>());
        vectorDePropiedades.add(new ArrayList<String>());
    }

    public static void partida(boolean _verbose) throws URISyntaxException, InterruptedException {

        verbose = _verbose;
        // // *********************** JUEGO ***********************
        Scanner scanner = new Scanner(System.in);
        iniciarSesion(client, scanner);
        menuInicial(client, scanner);
        empezarPartida(client, scanner);
        jugarPartida(client, scanner);
        scanner.close();
    }

    public static void unirsePartida(String _IDPartida) {
        client.send("unirsePartida," + nombreUser + "," + _IDPartida);
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

    public static void finTurno(WebSocketClient client) {
        client.send("finTurno," + nombreUser + "," + IDPartida);
    }

    public static void comprarPropiedad(WebSocketClient client, String propiedad) {
        client.send("SI_COMPRAR_PROPIEDAD," + nombreUser + "," + propiedad + "," + IDPartida);
    }

    // Metodo que se encarga de gestionar todos los mensajes recibidos
    public static void gestionMensaje(String message) {
        if (verbose) {
            System.out.println(message);
        }
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
                // Si hemos creado la cuenta correctamente
                SignUpFormController.cuentaRegistrada = true;
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

                indiceJugador = Integer.parseInt(partes[2]);
                // Almacenar orden de tiradas
                ordenJugadores[0] = partes[3];
                ordenJugadores[1] = partes[4];
                ordenJugadores[2] = partes[5];
                ordenJugadores[3] = partes[6];

                // Almacenar posicion inicial y dinero inicial
                for (int i = 0; i < 4; i++) {
                    posicionesJugadores[i] = "1";
                    dineroJugadores[i] = 1000;
                }
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

                posicionesJugadores[indiceJugador] = partes[3];

                if (Integer.parseInt(partes[4]) > 0) {
                    enCarcel = true;
                    // DatosPartida.estoyCarcel =true;
                    turnosCarcel = Integer.parseInt(partes[4]);
                } else {
                    enCarcel = false;
                    // DatosPartida.estoyCarcel =false;
                    turnosCarcel = 0;
                }
                break;
            case "NUEVO_DINERO_JUGADOR":
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                break;
            case "NUEVO_DINERO_BOTE":
                dineroBote = Integer.parseInt(partes[1]);
                break;
            case "OBTENER_BOTE":
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                break;
            case "VENDER_OK":
                vectorDePropiedades.get(indiceJugador).remove(partes[1]);
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                break;
            case "VENDER_NO_OK":
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
            case "CASILLA":
                meToca = true;
                break;
            case "NADA":
                break;
            case "COMPRAR_OK":
                vectorDePropiedades.get(indiceJugador).add(tablero[Integer.parseInt(partes[2])]);
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[3]);
                compraRealizada = true;
                break;
            case "COMPRAR_NO_OK":
                compraRealizada = true;
                break;
            case "NUEVO_DINERO_ALQUILER":
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[1]);
                break;
            case "ACTUALIZAR_USUARIO":
                int indice = -1;
                String jugador = partes[1];
                for (int i = 0; i < 4; i++) {
                    if (jugador.equals(ordenJugadores[i])) {
                        indice = i;
                    }
                }
                dineroJugadores[indice] = Integer.parseInt(partes[2]);
                posicionesJugadores[indice] = partes[3];
                ArrayList<String> lista = new ArrayList<String>();
                for (int i = 4; i < partes.length; i++) {
                    if (!partes[i].equals("null")) {
                        int propiedad = Integer.parseInt(partes[i].substring(9));
                        lista.add(tablero[propiedad]);
                    }
                }
                vectorDePropiedades.get(indice).clear();
                vectorDePropiedades.get(indice).addAll(lista);
                break;
            default:
                System.out.println("Mensaje no tenido en cuenta: " + message);
                return;
        }
        ConexionServidor.liberar(); // Igual no hay que liberar en todos los casos
    }

    // Metodos privados de gestion del juego por terminal

    // ************* INICIO SESION *************
    private static void iniciarSesion(WebSocketClient client, Scanner scanner)
            throws URISyntaxException, InterruptedException {
        String[] partes;
        String input;
        while (!sesionIniciada) {
            System.out.println("Elija que desea hacer: ");
            System.out.println("1 - Registrarse[mail,password,name] ");
            System.out.println("2 - Iniciar Sesion[mail,password]");
            String mensaje = scanner.nextLine();
            switch (mensaje) {
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

    // ************* MENU INICIAL *************
    private static void menuInicial(WebSocketClient client, Scanner scanner)
            throws URISyntaxException, InterruptedException {
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
            while (!empezarPartida) {
                System.out.println("Introduzca un 1 para comenzar la partida: ");
                String empezar = scanner.nextLine();
                switch (empezar) {
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
        while (enPartida) {
            if (miTurno) {
                limpiarTerminal();
                mostrarInfoJugador();
                System.out.println("Es tu turno, pulsa cualquier tecla para lanzar los dados ");
                scanner.nextLine();
                // Lanzar los dados
                lanzarDados(nombreUser, IDPartida);
                // Esperamos a recibir la respuesta del servidor
                ConexionServidor.esperar();

                // Esperar hasta que me hayan llegado todos los mensajes que espero del servidor
                while (!meToca) {
                    ConexionServidor.esperar();
                }
                meToca = false;
                if (!enCarcel) {
                    if (comprarPropiedad) {
                        System.out.println("Introduzca un 1 si desea comprar la propiedad: "
                                + tablero[Integer.parseInt(propiedadAComprar)] + String.valueOf(propiedadAComprar));
                        if (scanner.nextLine().equals("1")) {
                            comprarPropiedad(client, propiedadAComprar);
                            while (!compraRealizada) {
                                ConexionServidor.esperar();
                            }
                            compraRealizada = false;
                        }
                        comprarPropiedad = false;
                    }

                }

                finTurno(client);
                miTurno = false;
                System.out.println("Esperando turno");
            }
            // Esperamos a recibir la respuesta del servidor
            // Finalizamos el turno
            ConexionServidor.esperar();
        }
    }

    private static void mostrarInfoJugador() {
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("|                          PARTIDA #" + IDPartida + "                         |");
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("|                     JUGADORES PARTICIPANTES                  |");
        System.out.println("+----------------------+----------------------+----------------+");
        System.out.println("|   NOMBRE JUGADOR     |     CASILLA ACTUAL   |      DINERO    |");
        System.out.println("+----------------------+----------------------+----------------+");
        for (int i = 0; i < ordenJugadores.length; i++) {
            System.out.println(String.format("| %-20s | %-20s | %,14d |", ordenJugadores[i],
                    tablero[Integer.parseInt(posicionesJugadores[i])], dineroJugadores[i]));
        }
        System.out.println("+----------------------+----------------------+----------------+");
        System.out.println(String.format("| %-20s | %-20s | %,14d |", "Dinero en el bote", "", dineroBote));
        System.out.println("+----------------------+----------------------+----------------+");

        // Mostrar propiedades del jugador
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("|                          PROPIEDADES                         |");
        System.out.println("+--------------------------------------------------------------+");
        for (int i = 0; i < vectorDePropiedades.size(); i++) {
            ArrayList<String> propiedades = vectorDePropiedades.get(i);
            System.out.printf("Propiedades del jugador %d:\n", i + 1);
            if (propiedades.size() == 0) {
                System.out.println("|                          Ninguna                             |");
            } else {
                for (String propiedad : propiedades) {
                    System.out.printf("|%1$-62s|\n",
                            String.format("%1$" + ((64 + propiedad.length()) / 2) + "s", propiedad));
                }
            }
            System.out.println(); // Salto de línea para separar las propiedades de cada jugador
        }

        System.out.println("+--------------------------------------------------------------+");
    }

    private static void limpiarTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

};