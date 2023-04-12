import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public static int CuentaInfoRecibida = 3;
    public static int dineroEnBanco = 0;

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
    public static List<String> nombresPropiedades = new ArrayList<>();
    public static List<String> preciosPropiedades = new ArrayList<>();

    public static boolean comprarPropiedad = false;
    public static String propiedadAComprar;
    public static boolean meToca = false;
    public static boolean compraRealizada;

    public static boolean dadosDobles = false;

    public static String precioPropiedadAComprar;

    public static boolean apostarDinero;

    public static boolean enBanco;

    private static boolean respuestaBanco;

    private static boolean finMenu;

    public final static String[] tablero = { "nada", "Salida", "Monterrey", "Guadalajara", "Treasure", "Tax",
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

    public static void apostarDinero(WebSocketClient client, String dineroApostar) {
        client.send("APOSTAR," + nombreUser + "," + IDPartida + "," + dineroApostar);
    }

    private static void retirarDinero(WebSocketClient client2, int cantidad) {
        client.send("SACAR," + nombreUser + "," + IDPartida + "," + Integer.toString(cantidad));
    }

    private static void depositarDinero(WebSocketClient client2, int cantidad) {
        client.send("METER," + nombreUser + "," + IDPartida + "," + Integer.toString(cantidad));
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

                //
                enCarcel = false;
                //

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

                dineroEnBanco = 0;
                CuentaInfoRecibida = 3;
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
                dadosDobles = dados[0] == dados[1];

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
                precioPropiedadAComprar = partes[4];
                break;
            case "DENTRO_CARCEL":
                enCarcel = true;
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
                CuentaInfoRecibida++;
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
            case "DINERO_APOSTAR":
                // He caido en la casilla del banco
                apostarDinero = true;
                break;
            case "ACCION_BANCO":
                enBanco = true;
                dineroEnBanco = Integer.parseInt(partes[3]);
                break;
            case "SACAR_DINERO_BANCO_NO_OK":
                respuestaBanco = true;
                break;
            case "SACAR_DINERO_BANCO":
                respuestaBanco = true;
                dineroEnBanco = Integer.parseInt(partes[3]);
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[4]);
                break;
            case "APOSTAR_OK":
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                break;
            case "APOSTAR_NOOK":
                break;
            case "METER_DINERO_BANCO":
                dineroEnBanco = Integer.parseInt(partes[3]);
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[4]);
                respuestaBanco = true;
                break;
            case "EDIFICAR":
                nombresPropiedades.clear();
                preciosPropiedades.clear();

                for (int i = 1; i < partes.length; i++) {
                    String[] prop = partes[i].split(":");
                    nombresPropiedades.add(prop[0]);
                    preciosPropiedades.add(prop[1]);
                }

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

    // Bucle de juego de una partida
    private static void jugarPartida(WebSocketClient client, Scanner scanner) {
        System.out.println("Empieza la partida");
        while (enPartida) {
            if (miTurno) {
                while (CuentaInfoRecibida < 3) {
                    ConexionServidor.esperar();
                }
                do {
                    limpiarTerminal();
                    lanzarLosDados(scanner);
                    JugarTurno(client, scanner);
                } while (dadosDobles);

                menuTurno(client, scanner);
                miTurno = false;
                System.out.println("Esperando turno");
            }
            // Esperamos a recibir la respuesta del servidor
            // Finalizamos el turno
            ConexionServidor.esperar();
        }
    }

    private static void lanzarLosDados(Scanner scanner) {
        CuentaInfoRecibida = 0;
        mostrarInfoJugador();
        if (dadosDobles) {
            System.out.print("Has sacado dados dobles, ");
        }
        System.out.print("Pulsa cualquier tecla para lanzar los dados: ");
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

        // Mostrar el valor de los dados
        System.out.println("+----------------------+----------------------+----------------+");
        System.out
                .println(String.format("| %-20s | %-20d | %-14d |", "Resultado dados", dados[0], dados[1]));
        System.out.println("+----------------------+----------------------+----------------+");
    }

    // Gestiona la entrada salida necesaria para jugar un turno
    private static void JugarTurno(WebSocketClient client, Scanner scanner) {
        if (!enCarcel) {
            if (comprarPropiedad) {
                gestionCompraPropiedad(client, scanner);
            } else if (apostarDinero) {
                gestionApuestaDinero(client, scanner);
            } else if (enBanco) {
                gestionBanco(client, scanner);
            }
        }
    }

    private static void menuTurno(WebSocketClient client, Scanner scanner) {
        while (!finMenu) {
            System.out.println("Es tu turno, " + nombreUser + ". ¿Qué deseas hacer?");
            System.out.println("1 - Edificar");
            System.out.println("2 - Intercambiar propiedad");
            System.out.println("3 - Acabar turno");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    edificar(client, scanner);
                    break;
                case "2":
                    intercambiarPropiedad();
                    break;
                case "3":
                    finTurno(client);
                    finMenu = true;
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
        finMenu = false;
    }

    private static void acabarTurno() {
    }

    private static void intercambiarPropiedad() {
    }

    private static void edificar(WebSocketClient client, Scanner scanner) {
        System.out.println("Seleccione una propiedad para edificar:");
        for (int i = 0; i < nombresPropiedades.size(); i++) {
            String precio = preciosPropiedades.get(i);
            System.out.println((i + 1) + " - " + nombresPropiedades.get(i) + " (" + precio + ")");
        }
        System.out.println("0 - Volver al menú anterior");

        int opcion = scanner.nextInt();
        if (opcion == 0) {
            return;
        }
        if (opcion > 0 && opcion <= nombresPropiedades.size()) {
            String propiedadElegida = nombresPropiedades.get(opcion - 1);
            edificarPropiedad(propiedadElegida);
        } else {
            System.out.println("Opción inválida");
        }
    }

    private static void edificarPropiedad(String propiedadElegida) {
    }

    private static void gestionBanco(WebSocketClient client, Scanner scanner) {
        System.out.println(
                "Ha caido en la casilla del banco. Que operacion desea realizar:");
        System.out.println("1- Depositar dinero");
        System.out.println("2- Retirar dinero");
        System.out.println("3- No realizar ninguna operacion");
        String mensaje = scanner.nextLine();
        int cantidad;
        switch (mensaje) {
            case "1":
                cantidad = convertirStringAEnteroValido(client, scanner);
                if (cantidad > dineroJugadores[indiceJugador]) {
                    System.out.println("No tiene tanto dinero");
                } else {
                    depositarDinero(client, cantidad);
                }
                break;
            case "2":
                cantidad = convertirStringAEnteroValido(client, scanner);
                if (cantidad > dineroEnBanco) {
                    System.out.println("No tiene tanto dinero en el banco");
                } else {
                    retirarDinero(client, cantidad);
                }
                break;
            case "3":
                break;
            default:
                System.out.println("No ha seleccionado una opción válida");
                break;
        }
        enBanco = false;

        // Esperar la respuesta del servidor
        while (!respuestaBanco) {
            ConexionServidor.esperar();
        }
        respuestaBanco = false;
    }

    private static int convertirStringAEnteroValido(WebSocketClient client, Scanner scanner) {
        boolean valido;
        do {
            System.out.println("Introduzca una cantidad valida");
            String cantidad = scanner.nextLine();
            try {
                int numero = Integer.parseInt(cantidad);
                valido = true;
                if (numero > 0) {
                    return numero;
                } else {
                    System.out.println("Ha decidio no retirar dinero");
                    return 0;
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        "El String '" + cantidad + "' no puede ser convertido a un entero.");
                valido = false;
            }
        } while (!valido);
        return -1;
    }

    private static void gestionApuestaDinero(WebSocketClient client, Scanner scanner) {
        boolean valido = true;
        do {
            System.out.println(
                    "Ha caido en la casilla del casino. Cuanto dinero desea apostar? (0 si no desea apostar)");
            String dineroApostar = scanner.nextLine();
            try {
                int numero = Integer.parseInt(dineroApostar);
                valido = true;
                if (numero > 0) {
                    apostarDinero(client, dineroApostar);
                } else {
                    System.out.println("Ha decidio no apostar dinero");
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        "El String '" + dineroApostar + "' no puede ser convertido a un entero.");
                valido = false;
            }
        } while (!valido);

        apostarDinero = false;
    }

    private static void gestionCompraPropiedad(WebSocketClient client, Scanner scanner) {
        System.out.println("Introduzca un 1 si desea comprar la propiedad: "
                + tablero[Integer.parseInt(propiedadAComprar)] + "(" + String.valueOf(propiedadAComprar) + ")"
                + " por " + precioPropiedadAComprar + "€?");
        if (scanner.nextLine().equals("1")) {
            comprarPropiedad(client, propiedadAComprar);
            while (!compraRealizada) {
                ConexionServidor.esperar();
            }
            compraRealizada = false;
        }
        comprarPropiedad = false;
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
        System.out.println(String.format("| %-20s | %-20s | %,14d |", "Dinero en el banco", "", dineroEnBanco));
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
                    int numeroPropiedad = Arrays.asList(tablero).indexOf(propiedad);
                    System.out.printf("|%1$-62s|\n",
                            String.format("%1$" + ((64 + propiedad.length()) / 2) + "s",
                                    propiedad + " (" + numeroPropiedad + ")"));
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