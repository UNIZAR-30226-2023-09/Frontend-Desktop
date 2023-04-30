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
    public static Boolean[] JugadorEnCarcel = new Boolean[4];
    public static int turnosCarcel = 0;
    public static int indiceJugador = -1;
    public static int CuentaInfoRecibida = 3;
    public static int dineroEnBanco = 0;
    public static int JugadoresVivos = 4;
    public static String propiedadADesplazarse;

    // ***** Información del usuario *****
    public static String nombreUser = "";
    public static int gemas = 0;
    public static String IDPartida = "";
    public static String[] ordenJugadores = new String[4];
    public static Boolean[] jugadoresVivos = new Boolean[4];
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
    public static boolean respuestaBanco;
    public static boolean finMenu;
    public static boolean esperarListaEdificar;
    public static String tengoSuerte;
    public static boolean elegirCasilla;
    public static String evento = "Ninguno";
    // Para mostrar la tarjeta correspondiente al superpoder
    public static String superPoder = "0";
    public static boolean mostrarEvento;
    public static double economia = 1.0;
    public static int ronda;
    public static boolean precioPropiedadRecivido = false;

    public static int precioVenta;
    public static boolean skinsObtenidas = false;

    // Contiene las skins del jugador con el formato idSkin1:0, idSkin2:10, etc
    // Si la skin tiene un 0 significa que la tiene, y si tiene un numero diferente
    // es el precio
    public static ArrayList<String> listaSkins = new ArrayList<String>();

    public static String[] skinsJugadores = new String[4];

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
        while (true) {
            menuInicial(client, scanner);
            empezarPartida(client, scanner);
            jugarPartida(client, scanner);
        }
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

    public static void finTurno() {
        client.send("finTurno," + nombreUser + "," + IDPartida);
    }

    public static void comprarPropiedad(String propiedad) {
        client.send("SI_COMPRAR_PROPIEDAD," + nombreUser + "," + propiedad + "," + IDPartida);
    }

    public static void apostarDinero(String dineroApostar) {
        client.send("APOSTAR," + nombreUser + "," + IDPartida + "," + dineroApostar + "," + tengoSuerte);
    }

    public static void retirarDinero(int cantidad) {
        client.send("SACAR," + nombreUser + "," + IDPartida + "," + Integer.toString(cantidad));
    }

    public static void depositarDinero(int cantidad) {
        client.send("METER," + nombreUser + "," + IDPartida + "," + Integer.toString(cantidad));
    }

    public static void quieroEdificar() {
        client.send("QUIERO_EDIFICAR," + nombreUser + "," + IDPartida);
    }

    // Vende un edificio (casa) de la propiedad elegida
    public static void venderCasa(String propiedad) {
        client.send("venderEdificio," + nombreUser + "," + IDPartida + "," + propiedad);
    }

    // Vende la propiedad elegida
    public static void quieroVenderPropiedad(String propiedad) {
        client.send("QUIERO_VENDER_PROPIEDAD," + nombreUser + "," + IDPartida + "," + propiedad);
    }

    // Vende la propiedad elegida
    public static void venderPropiedad(String propiedad) {
        client.send("venderPropiedad," + nombreUser + "," + IDPartida + "," + propiedad);
    }

    public static void edificarPropiedad(int propiedadElegida) {
        String propiedad = nombresPropiedades.get(propiedadElegida);
        String precio = preciosPropiedades.get(propiedadElegida);
        client.send("EDIFICAR," + nombreUser + "," + IDPartida + "," +
                propiedad + "-" + precio);
    }

    public static void enviarCasilla(String casilla) {
        client.send("DESPLAZARSE_CASILLA" + nombreUser + "," + IDPartida + "," +
                casilla);
    }

    // Compra la skin dado su id
    public static void comprarSkin(int idSkin) {
        client.send("comprarSkin," + nombreUser + "," + idSkin);
    }

    // Muestra las skins disponibles
    public static void mostrarSkins() {
        client.send("MOSTRAR_SKINS," + nombreUser);
    }

    // Equipa la skin dada
    public static void equiparSkin(String idSkin) {
        client.send("EQUIPAR_SKIN," + nombreUser + "," + idSkin);
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
                // Ningun jugador esta en la carcel
                JugadorEnCarcel[0] = false;
                JugadorEnCarcel[1] = false;
                JugadorEnCarcel[2] = false;
                JugadorEnCarcel[3] = false;
                evento = "Ninguno";
                economia = 1.0;
                ronda = 1;
                JugadoresVivos = 4;

                indiceJugador = Integer.parseInt(partes[2]);
                // Almacenar orden de tiradas
                ordenJugadores[0] = partes[3];
                ordenJugadores[1] = partes[4];
                ordenJugadores[2] = partes[5];
                ordenJugadores[3] = partes[6];

                skinsJugadores[0] = partes[7];
                skinsJugadores[1] = partes[8];
                skinsJugadores[2] = partes[9];
                skinsJugadores[3] = partes[10];

                // Mostrar por pantalla las skins de los jugadores
                for (int i = 0; i < 4; i++) {
                    System.out.println("Skin jugador " + i + ": " + skinsJugadores[i]);
                }

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
                break;
            case "DADOS":
                dados[0] = Integer.parseInt(partes[1]);
                dados[1] = Integer.parseInt(partes[2]);
                dadosDobles = dados[0] == dados[1];

                posicionesJugadores[indiceJugador] = partes[3];

                if (Integer.parseInt(partes[4]) > 0) {
                    JugadorEnCarcel[indiceJugador] = true;
                    // DatosPartida.estoyCarcel =true;
                    turnosCarcel = Integer.parseInt(partes[4]);
                } else {
                    JugadorEnCarcel[indiceJugador] = false;
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
                // Encontrar el jugador que esta en la carcel
                String jugadorCarcel = partes[1];
                int indiceJ = 0;
                for (int i = 0; i < 4; i++) {
                    if (jugadorCarcel.equals(ordenJugadores[i])) {
                        indiceJ = i;
                    }
                }
                JugadorEnCarcel[indiceJ] = true;
                posicionesJugadores[indiceJ] = "11";
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
            case "ACTUALIZAR_BANCO":
                dineroEnBanco = Integer.parseInt(partes[1]);
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

                for (int i = 2; i < partes.length; i++) {
                    String[] prop = partes[i].split("-");
                    nombresPropiedades.add(prop[0].substring(9));
                    preciosPropiedades.add(prop[1]);
                }
                esperarListaEdificar = true;
                break;
            case "EDIFICAR_OK":
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                break;
            case "EDIFICAR_NOOK":
                break;
            case "NUEVO_DINERO_ALQUILER_RECIBES":
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[1]);
                int indiceJugadorPropiedad = obtenerIndiceJugador(partes[2]);
                dineroJugadores[indiceJugadorPropiedad] = Integer.parseInt(partes[3]);
            case "REINICIAR_SUERTE":
                tengoSuerte = "0";
                break;
            case "AUMENTAR_SUERTE":
                tengoSuerte = "1";
                break;
            case "DESPLAZAR_JUGADOR":
                propiedadADesplazarse = partes[1];
                // Sería actualizar despues a esto posicionesJugadores[indiceJugador] =
                // propiedadADesplazarse;
                break;
            case "ELEGIR_CASILLA":
                elegirCasilla = true;
                break;
            case "SUPERPODER":
                superPoder = partes[1];
                break;
            case "FinPartida":
                enPartida = false;
                break;
            case "ELIMINADO":
                enPartida = false;
                break;
            case "JugadorMuerto":
                jugadoresVivos[obtenerIndiceJugador(partes[1])] = false;
                JugadoresVivos--;
                break;
            case "EVENTO":
                evento = partes[1];
                mostrarEvento = true;
                break;
            case "ACTUALIZAR_DINERO_BANCO":
                dineroEnBanco = Integer.parseInt(partes[1]);
                break;
            case "ECONOMIA":
                economia = Double.parseDouble(partes[1]);
                break;
            case "FIN_RONDA":
                ronda = Integer.parseInt(partes[1]);
                break;
            case "PRECIO_VENTA":
                precioVenta = Integer.parseInt(partes[1]);
                precioPropiedadRecivido = true;
                break;
            case "SUMAR_GEMAS":
                gemas += Integer.parseInt(partes[1]);
                break;
            case "LISTA_SKIN":
                // Si lista skins tenia algo lo borramos
                if (!listaSkins.isEmpty()) {
                    listaSkins.clear();
                }
                for (int i = 1; i < partes.length; i++) {
                    listaSkins.add(partes[i]);
                }
                skinsObtenidas = true;
                break;
            case "SKIN_EQUIPADA_OK":
                System.out.println(nombreUser + " tiene la skin " + partes[2] + " equipada");
                break;
            case "SKIN_EQUIPADA_NOOK":
                System.out.println(nombreUser + " no tiene la skin " + partes[2] + " equipada");
                break;
            case "SKIN_COMPRADA_OK":
                System.out.println(nombreUser + " ha comprado la skin y ahora tiene " + partes[2] + " gemas");
                // Actualizar el numero de gemas
                gemas = Integer.parseInt(partes[2]);
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
        System.out.println("3 - Ver skins");
        System.out.println("4 - Equipar skin");
        boolean skinsVistas = false;
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
                case "3":
                    // Ver listado de skins
                    verSkins();
                    skinsVistas = true;
                    break;
                case "4":
                    // Equipar skin
                    System.out.println("Rellene los campos: [skin]: ");
                    String skin = scanner.nextLine();
                    equiparSkin(skin);
                    break;
                default:
                    System.out.println("Elija una opción válida");
                    return;
            }
            // Esperamos a recibir la respuesta del servidor
            if (!skinsVistas) {
                ConexionServidor.esperar();
            } else {
                skinsVistas = false;
            }
        }
    }

    private static void verSkins() {
        System.out.println("Skins disponibles: ");
        mostrarSkins();
        while (!skinsObtenidas) {
            ConexionServidor.esperar();
        }
        skinsObtenidas = false;
        // Recorrer la lista de skins y mostrarlas
        for (String skin : listaSkins) {
            System.out.println(skin);
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
                while (CuentaInfoRecibida < JugadoresVivos - 1) {
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
        if (!JugadorEnCarcel[indiceJugador]) {
            if (comprarPropiedad) {
                gestionCompraPropiedad(client, scanner);
            } else if (apostarDinero) {
                gestionApuestaDinero(client, scanner);
            } else if (enBanco) {
                gestionBanco(client, scanner);
            } else if (superPoder != "0") {
                gestionSuperpoder(client, scanner);
            }
        }
    }

    private static void gestionSuperpoder(WebSocketClient client2, Scanner scanner) {
        if (superPoder.equals("1")) {
            System.out.print("Elija a que casilla desea desplazarse:");
            String casilla = scanner.nextLine();
            enviarCasilla(casilla);
        }
        superPoder = "0";
    }

    private static void menuTurno(WebSocketClient client, Scanner scanner) {
        while (!finMenu) {
            System.out.println("Es tu turno, " + nombreUser + ". ¿Qué deseas hacer?");
            System.out.println("1 - Edificar");
            System.out.println("2 - Intercambiar propiedad");
            System.out.println("3 - Vender una propiedad");
            System.out.println("4 - Acabar turno");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    edificar(client, scanner);
                    break;
                case "2":
                    intercambiarPropiedad();
                    break;
                case "3":
                    venderUnaPropiedad(client, scanner);
                    break;
                case "4":
                    finTurno();
                    finMenu = true;
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
        finMenu = false;
    }

    // Muestra las propiedades que tiene el jugador y le da la opción de vender
    // la que elija
    private static void venderUnaPropiedad(WebSocketClient client, Scanner scanner) {
        System.out.println("Seleccione una propiedad para vender:");
        for (int i = 0; i < nombresPropiedades.size(); i++) {
            String precio = preciosPropiedades.get(i);
            String numProp = nombresPropiedades.get(i);
            System.out.println(
                    (i + 1) + " - " + tablero[Integer.parseInt(numProp)] + " (" + precio + ")");
        }
        System.out.println("0 - Volver al menú anterior");

        int opcion = scanner.nextInt();
        if (opcion == 0) {
            return;
        }
        if (opcion > 0 && opcion <= nombresPropiedades.size()) {
            String numProp = nombresPropiedades.get(opcion - 1);
            venderPropiedad(numProp);
        } else {
            System.out.println("Opción inválida");
        }
    }

    private static void intercambiarPropiedad() {
    }

    private static void edificar(WebSocketClient client, Scanner scanner) {
        quieroEdificar();
        while (!esperarListaEdificar) {
            ConexionServidor.esperar();
        }
        esperarListaEdificar = false;
        System.out.println("Seleccione una propiedad para edificar:");
        for (int i = 0; i < nombresPropiedades.size(); i++) {
            String precio = preciosPropiedades.get(i);
            String numProp = nombresPropiedades.get(i);
            System.out.println(
                    (i + 1) + " - " + tablero[Integer.parseInt(numProp)] + " (" + precio + ")");
        }
        System.out.println("0 - Volver al menú anterior");

        int opcion = scanner.nextInt();
        if (opcion == 0) {
            return;
        }
        if (opcion > 0 && opcion <= nombresPropiedades.size()) {
            String numProp = nombresPropiedades.get(opcion - 1);
            // Pasar la propiedad a entero
            int propiedad = Integer.parseInt(numProp);
            edificarPropiedad(propiedad);
        } else {
            System.out.println("Opción inválida");
        }
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
                    depositarDinero(cantidad);
                }
                break;
            case "2":
                cantidad = convertirStringAEnteroValido(client, scanner);
                if (cantidad > dineroEnBanco) {
                    System.out.println("No tiene tanto dinero en el banco");
                } else {
                    retirarDinero(cantidad);
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

    // Gestiona la entrada salida necesaria para apostar en el casino
    private static void gestionApuestaDinero(WebSocketClient client, Scanner scanner) {
        boolean valido = true;
        do {
            System.out.println(
                    "Ha caido en la casilla del casino. Cuanto dinero desea apostar? (0 si no desea apostar)");
            String dineroApostar = scanner.nextLine();
            try {
                int numero = Integer.parseInt(dineroApostar);
                valido = true;
                if (numero > 0 && numero <= dineroJugadores[indiceJugador]) {
                    apostarDinero(dineroApostar);
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
            comprarPropiedad(propiedadAComprar);
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

    private static int obtenerIndiceDeCasilla(String nombreCasilla) {
        for (int i = 0; i < tablero.length; i++) {
            if (tablero[i].equals(nombreCasilla)) {
                return i;
            }
        }
        // Si no se encuentra el nombre de la casilla en el tablero, devolvemos -1
        return -1;
    }

    private static int obtenerIndiceJugador(String ID_Jugador) {
        for (int i = 0; i < ordenJugadores.length; i++) {
            if (ordenJugadores[i].equals(ID_Jugador)) {
                return i;
            }
        }
        return -1;
    }

};