import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import org.java_websocket.client.WebSocketClient;

public class GestionPartida {

    public static boolean verbose = false;

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
    public static boolean edificarOK = false;
    // Para mostrar la tarjeta correspondiente al superpoder
    public static String superPoder = "0";
    public static boolean mostrarEvento;
    public static double economia = 1.0;
    public static int ronda;
    public static boolean precioPropiedadRecibido = false;

    public static int precioVenta;
    public static boolean skinsObtenidas = false;

    // Contiene las skins del jugador con el formato idSkin1:0, idSkin2:10, etc
    // Si la skin tiene un 0 significa que la tiene, y si tiene un numero diferente
    // es el precio
    public static ArrayList<String> listaSkins = new ArrayList<String>();

    public static String[] skinsJugadores = new String[4];
    public static String skinTablero = "TABLERO1";

    // Hay una subasta pendiente por mostrar en la interfaz
    public static boolean subasta;

    public static ArrayList<String> nombresPropiedadesEdificar = new ArrayList<String>();

    public static ArrayList<String> preciosPropiedadesEdificar = new ArrayList<String>();

    // La subasta esta ocupada y no puedes subastar
    public static boolean subastaOcupada;

    public static String propiedadADesplazarseAvion;

    public static String jugador_subasta;

    public static String propiedad_subasta;

    public static int precio_subasta;

    public static boolean perteneceTorneo;

    // Hashmap que contiene las propiedades de la partida
    public static HashMap<Integer, Propiedad> propiedades = new HashMap<Integer, Propiedad>();

    public static boolean actualizar_cambio_dispositivo = false;

    public static boolean enTorneo;

    public static String IDTorneo;

    public static int[] clasificacionTorneo = new int[4];

    public static boolean ganador;

    public static ArrayList<String> chat = new ArrayList<String>();
    public static ArrayList<String> Usuariochat = new ArrayList<String>();

    public static boolean resultadosTorneo;

    private static int cuentaResultados;

    // Struct que almacena el dueño de una propiedad, el id de la propiedad, el
    // nombre de la propiedad y el numero de casas que tiene
    public static class Propiedad {
        public String dueño;
        public int id;
        public String nombre;
        public int casas;

        // Constructor por defecto
        public Propiedad(String dueño, int id, String nombre, int casas) {
            this.dueño = dueño;
            this.id = id;
            this.nombre = nombre;
            this.casas = casas;
        }
    }

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
        clasificacionTorneo[0] = 0;
        clasificacionTorneo[1] = 0;
        clasificacionTorneo[2] = 0;
        clasificacionTorneo[3] = 0;
    }

    public static void crearPartida() {
        client.send("crearPartida," + nombreUser);
        clasificacionTorneo[0] = 0;
        clasificacionTorneo[1] = 0;
        clasificacionTorneo[2] = 0;
        clasificacionTorneo[3] = 0;
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
        client.send("venderPropiedad," + nombreUser + "," + propiedad + "," + IDPartida);
    }

    public static void edificarPropiedad(String propiedad, String precio) {
        client.send("EDIFICAR," + nombreUser + "," + IDPartida + "," +
                propiedad + "-" + precio);
    }

    public static void enviarCasilla(String casilla) {
        client.send("DESPLAZARSE_CASILLA," + nombreUser + "," + IDPartida + "," +
                casilla);
    }

    // Compra la skin dado su id
    public static void comprarSkin(String idSkin) {
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

    // Subasta la propiedad elegida por el usuario por el precio dado
    public static void subastarPropiedad(String propiedad, String precio) {
        client.send("SUBASTAR," + IDPartida + "," + nombreUser + "," + propiedad + "," + precio);
    }

    public static void ComprarSubasta() {
        client.send("COMPRAR_SUBASTA," + nombreUser + "," + IDPartida + "," + jugador_subasta);
    }

    public static void pagarLiberarseCarcel() {
        client.send("pagarLiberarseCarcel," + nombreUser + "," + IDPartida);
    }

    public static void enviarChat(String mensaje) {
        client.send("chat," + nombreUser + "," + IDPartida + "," + mensaje);
        chat.add(mensaje);
        Usuariochat.add(nombreUser);
    }

    // Torneos

    public static void crearTorneo() {
        client.send("crearTorneo," + nombreUser);
    }

    public static void unirseTorneo(String ID_Torneo) {
        client.send("unirseTorneo," + nombreUser + "," + ID_Torneo);
    }

    public static void empezarPartidaTorneo(String ID_Torneo) {
        client.send("empezarPartidaTorneo," + ID_Torneo + "," + nombreUser);
    }

    // Devolver todas las propiedades del usuario en una lista, leyendolas del
    // diccionario de propiedades "propiedades"
    public static ArrayList<Propiedad> getPropiedades() {
        ArrayList<Propiedad> propiedadesDevolver = new ArrayList<Propiedad>();
        for (Integer key : propiedades.keySet()) {
            Propiedad prop = propiedades.get(key);
            if (prop.dueño.equals(nombreUser)) {
                propiedadesDevolver.add(prop);
            }
        }
        return propiedadesDevolver;
    }

    // Metodo que se encarga de gestionar todos los mensajes recibidos
    public static void gestionMensaje(String message) {
        System.out.println(message);
        String mensaje = new String(message);
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
                enPartida = false;
                System.out.println("Error en unirse a partida");
                break;
            case "EMPEZAR_OK":
                empezarPartida = true;
                enPartida = true;
                IDPartida = partes[1];
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

                jugadoresVivos[0] = true;
                jugadoresVivos[1] = true;
                jugadoresVivos[2] = true;
                jugadoresVivos[3] = true;

                skinTablero = partes[11];

                // Inicializar todas las propiedades de la partida para que no tengan dueño
                for (int i = 1; i < 41; i++) {
                    Propiedad prop = new Propiedad("", i, tablero[i], 0);
                    propiedades.put(i, prop);
                }

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
                propiedades.get(Integer.parseInt(partes[1])).casas = 0;
                propiedades.get(Integer.parseInt(partes[1])).dueño = null;
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
            case "FUERA_CARCEL":
                // Encontrar el jugador que esta en la carcel
                String jugadorFueraCarcel = partes[1];
                int indiceJ2 = 0;
                for (int i = 0; i < 4; i++) {
                    if (jugadorFueraCarcel.equals(ordenJugadores[i])) {
                        indiceJ2 = i;
                    }
                }
                JugadorEnCarcel[indiceJ2] = false;
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
                propiedades.get(Integer.parseInt(partes[2])).dueño = nombreUser;
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
                boolean entrado = false;
                dineroJugadores[indice] = Integer.parseInt(partes[2]);
                posicionesJugadores[indice] = partes[3];
                ArrayList<String> lista = new ArrayList<String>();
                for (int i = 4; i < partes.length; i++) {
                    entrado = true;
                    if (!partes[i].equals("null")) {
                        String[] aux = partes[i].split(":");
                        int propiedadActual = Integer.parseInt(aux[0].substring(9));
                        int casas = Integer.parseInt(aux[1]);
                        lista.add(tablero[propiedadActual]);
                        propiedades.get(propiedadActual).casas = casas;
                        propiedades.get(propiedadActual).dueño = jugador;
                    }
                }
                if (entrado) {
                    // Verificar si las propiedades del diccionario no están en la lista
                    for (Propiedad propiedad : propiedades.values()) {
                        if (propiedad.dueño.equals(jugador) && !lista.contains(propiedad.nombre)) {
                            propiedad.dueño = "";
                            propiedad.casas = 0;
                        }
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
                nombresPropiedadesEdificar.clear();
                preciosPropiedadesEdificar.clear();

                for (int i = 2; i < partes.length; i++) {
                    String[] prop = partes[i].split("-");
                    nombresPropiedadesEdificar.add(prop[0].substring(9));
                    preciosPropiedadesEdificar.add(prop[1]);
                }
                esperarListaEdificar = true;
                break;
            case "EDIFICAR_OK":
                edificarOK = true;
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                int propiedad = Integer.parseInt(partes[1]);
                propiedades.get(propiedad).casas++;
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
                // ganador=true; esto seria aqui??
                break;
            case "ELIMINADO":
                enPartida = false;
                ganador = false;
                break;
            case "JugadorMuerto":
                jugadoresVivos[obtenerIndiceJugador(partes[1])] = false;
                JugadoresVivos--;
                ArrayList<Propiedad> propiedadesDevolver = new ArrayList<Propiedad>();
                for (Integer key : propiedades.keySet()) {
                    Propiedad prop = propiedades.get(key);
                    if (prop.dueño.equals(partes[1])) {
                        propiedades.get(key).dueño = "";
                        propiedades.get(key).casas = 0;
                    }
                }
                break;
            case "EVENTO":
                evento = partes[1];
                mostrarEvento = true;
                break;
            case "ACTUALIZAR_DINERO_BANCO":
                dineroEnBanco = Integer.parseInt(partes[1]);
                break;
            case "CARCEL_NO_PAGADA":
                System.out.println("No has pagado la carcel");
                break;
            case "CARCEL_PAGADA":
                System.out.println("Has pagado la carcel");
                JugadorEnCarcel[indiceJugador] = false;
                break;
            case "ECONOMIA":
                economia = Double.parseDouble(partes[1]);
                break;
            case "FIN_RONDA":
                ronda = Integer.parseInt(partes[1]);
                break;
            case "PRECIO_VENTA":
                precioVenta = Integer.parseInt(partes[1]);
                precioPropiedadRecibido = true;
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
            case "SUBASTA":
                subasta = true;
                jugador_subasta = partes[1];
                propiedad_subasta = partes[2];
                precio_subasta = Integer.parseInt(partes[3]);
                break;
            case "SUBASTA_OCUPADA":
                subastaOcupada = true;
                break;
            case "GANADOR":
                enPartida = false;
                ganador = true;
                gemas = Integer.parseInt(partes[1]);
                break;
            case "DESPLAZAR_JUGADOR_AVION":
                propiedadADesplazarseAvion = partes[1];
                // Sería actualizar despues a esto posicionesJugadores[indiceJugador] =
                // propiedadADesplazarse;
                break;
            case "SUBASTA_OK":
                System.out.println("Subasta realizada con exito");
                break;
            case "SUBASTA_NO_OK":
                System.out.println("Subasta no realizada");
                break;
            case "SUBASTA_COMPRADA_TU":
                System.out.println("Has comprado la propiedad en subasta");
                vectorDePropiedades.get(indiceJugador).add(tablero[Integer.parseInt(partes[3])]);
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                break;
            case "SUBASTA_COMPRADA":
                // Coger la propiedad que se subasta ha comprado otro jugador
                String propiedadA = partes[3];
                // Coger la lista de mis propiedades
                ArrayList<String> misPropiedades = vectorDePropiedades.get(indiceJugador);
                for (int i = 0; i < misPropiedades.size(); i++) {
                    // Obtener el nombre de la propiedad
                    int indicePropiedadVendida = Integer.parseInt(propiedadA);
                    if (misPropiedades.get(i).equals(tablero[indicePropiedadVendida])) {
                        misPropiedades.remove(i);
                    }
                }
                dineroJugadores[indiceJugador] = Integer.parseInt(partes[2]);
                System.out.println("Han comprado tu propiedad en subasta por " + partes[2]);
                break;
            case "ESTADO_PARTIDA":
                ActualizarEstadoPartida(mensaje);
                break;
            case "CREADOT_OK":
                enTorneo = true;
                dueñoPartida = true;
                IDTorneo = partes[1];
                System.out.println("Torneo creado con exito");
                break;
            case "CREADOT_NOOK":
                System.out.println("No se ha podido crear el torneo");
                break;
            case "UNIRSET_OK":
                enTorneo = true;
                IDTorneo = partes[1];
                System.out.println("Te has unido al torneo con exito");
                break;
            case "UNIRSET_NO_OK":
                System.out.println("No se ha podido unir al torneo");
                break;
            case "ELIMINADO_TORNEO":
                enPartida = false;
                enTorneo = true;
                // ganador = false; esto aqui?
                break;
            case "GANADOR_TORNEO":
                enPartida = false;
                enTorneo = true;
                // ganador = true;
                break;
            case "CLASIFICACION_TORNEO":
                System.out.println("Clasificacion del torneo: " + mensaje);
                String jugadorActual = partes[1];
                // Obtener el indice del jugador
                int indiceJugadorTorneo = 0;
                for (int i = 0; i < ordenJugadores.length; i++) {
                    if (ordenJugadores[i].equals(jugadorActual)) {
                        indiceJugadorTorneo = i;
                    }
                }
                clasificacionTorneo[indiceJugadorTorneo] = Integer.parseInt(partes[2]);
                cuentaResultados++;
                if (cuentaResultados == 4) {
                    resultadosTorneo = true;
                    // ordenar clasificacionTorneo de menor a mayor
                    int aux;
                    String aux2;
                    for (int i = 0; i < clasificacionTorneo.length - 1; i++) {
                        for (int j = i + 1; j < clasificacionTorneo.length; j++) {
                            if (clasificacionTorneo[i] > clasificacionTorneo[j]) {
                                aux = clasificacionTorneo[i];
                                clasificacionTorneo[i] = clasificacionTorneo[j];
                                clasificacionTorneo[j] = aux;
                                aux2 = ordenJugadores[i];
                                ordenJugadores[i] = ordenJugadores[j];
                                ordenJugadores[j] = aux2;
                            }
                        }
                    }
                    System.out.println("Clasificacion del torneo: ");
                    for (int i = 0; i < ordenJugadores.length; i++) {
                        System.out.println(ordenJugadores[i] + " " + clasificacionTorneo[i]);
                    }
                }
                break;
            case "TORNEO_FINALIZADO":
                enTorneo = false;
                dueñoPartida = false;
                System.out.println("El torneo ha finalizado");
                break;
            case "CHAT":
                System.out.println("Mensaje del chat: " + mensaje);
                chat.add(partes[2]);
                Usuariochat.add(partes[1]);
                break;
            default:
                System.out.println("Mensaje no tenido en cuenta: " + message);
                return;
        }
        ConexionServidor.liberar(); // Igual no hay que liberar en todos los casos
    }

    // Metodo que se encarga de actualizar el estado de la partida
    // al volver a conectarse a una partida
    private static void ActualizarEstadoPartida(String mensaje) {
        actualizar_cambio_dispositivo = true;
        asignarVariablesIniciales();

        // Si estaba en una partida activa y se habia salido, se vuelve a meter en la
        // partida y recibe toda la informacion de la partida
        String[] aux = mensaje.split("\\|");
        String[] partesPartida = aux[0].split(",");
        // partesPartida contiene [ESTADO_PARTIDA,IDPartida, ronda, bote,
        // economia,evento, perteneceTorneo, turno]

        String turno = asignarPartesPartida(partesPartida);
        asignarTurno(turno);

        String[] partesPropiedades = aux[1].split(";");

        asignarPropiedades(partesPropiedades);

        String[] partesJugadores = aux[2].split(";");
        asignarInformacionJugadores(partesJugadores);

        CuentaInfoRecibida = 4;

        // Scanner scanner = new Scanner(System.in);
        // jugarPartida(client, scanner);
    }

    // Asigna la informacion de los jugadores
    private static void asignarInformacionJugadores(String[] partesJugadores) {
        for (int i = 0; i < 4; i++) {
            // Para cada uno de los jugadores actualizamos su dinero y
            // su posicion en el tablero
            String[] aux2 = partesJugadores[i].split(",");
            String nombreJugador = aux2[0];
            int indiceJugadorActual;
            if (nombreJugador.equals(nombreUser)) {
                // Si es el jugador actualizamos su informacion
                asignarMiInformacion(aux2, nombreJugador);
            } else {
                // Si no es el jugador actualizamos su informacion
                asignarOtrosInformacion(aux2, nombreJugador);
            }
        }
    }

    // Asigna la informacion de un jugador que no es el actual
    private static void asignarOtrosInformacion(String[] aux2, String nombreJugador) {
        int indiceJugadorActual;
        String vivo = aux2[1];
        Boolean vivoJugador = false;
        if (vivo.equals("1")) {
            vivoJugador = true;
            JugadoresVivos++;
        } else {
            vivoJugador = false;
        }
        indiceJugadorActual = Integer.parseInt(aux2[5]) - 1;
        ordenJugadores[indiceJugadorActual] = nombreJugador;
        jugadoresVivos[indiceJugadorActual] = vivoJugador;
        posicionesJugadores[indiceJugadorActual] = aux2[2];
        dineroJugadores[indiceJugadorActual] = Integer.parseInt(aux2[3]);
        skinsJugadores[indiceJugadorActual] = aux2[4];
        int turnosEnCarcel = Integer.parseInt(aux2[6]);
        JugadorEnCarcel[indiceJugadorActual] = turnosEnCarcel > 0;
    }

    // Asigna toda la informacion del jugador actual
    private static void asignarMiInformacion(String[] aux2, String nombreJugador) {
        int indiceJugadorActual;
        indiceJugadorActual = Integer.parseInt(aux2[6]) - 1;
        ordenJugadores[indiceJugadorActual] = nombreJugador;
        indiceJugador = indiceJugadorActual;
        jugadoresVivos[indiceJugadorActual] = true;
        posicionesJugadores[indiceJugadorActual] = aux2[1];
        dineroJugadores[indiceJugadorActual] = Integer.parseInt(aux2[2]);
        dineroEnBanco = Integer.parseInt(aux2[3]);
        skinsJugadores[indiceJugadorActual] = aux2[4];
        skinTablero = aux2[5];
        int turnosEnCarcel = Integer.parseInt(aux2[7]);
        JugadorEnCarcel[indiceJugadorActual] = turnosCarcel > 0;
        turnosCarcel = turnosEnCarcel;
    }

    // Asigna las propiedades a sus respectivos dueños
    private static void asignarPropiedades(String[] partesPropiedades) {
        // Inicializar todas las propiedades de la partida para que no tengan dueño
        for (int i = 1; i < 41; i++) {
            // Para cada una de las propiedades actualizamos su dueño y
            // el numero de casas que tiene
            String[] aux2 = partesPropiedades[i - 1].split(",");
            String dueño = aux2[0];
            int edificaciones = Integer.parseInt(aux2[1]);
            propiedades.get(i).dueño = dueño;
            propiedades.get(i).casas = edificaciones;
        }
    }

    // Asigna las variables iniciales necesarias a los valores por defecto
    private static void asignarVariablesIniciales() {
        JugadoresVivos = 1;
        // Inicializar todas las variables a las por defecto:
        JugadorEnCarcel[0] = false;
        JugadorEnCarcel[1] = false;
        JugadorEnCarcel[2] = false;
        JugadorEnCarcel[3] = false;

        // Inicializar todas las propiedades de la partida para que no tengan dueño
        for (int i = 1; i < 41; i++) {
            Propiedad prop = new Propiedad("", i, tablero[i], 0);
            propiedades.put(i, prop);
        }

        CuentaInfoRecibida = 4; // Lo pongo a 4 asumiendo que he recibido el mensaje de todos :)
    }

    // Asigna el turno al jugador actual si es su turno
    private static void asignarTurno(String turno) {
        System.out.println("Turno: " + turno);
        if (turno.equals(nombreUser)) {
            meToca = true;
            miTurno = true;
            // Saltar a la pantalla de juego
        } else {
            meToca = false;
            miTurno = false;
        }
    }

    // Asigna la informacion de la partida
    private static String asignarPartesPartida(String[] partesPartida) {
        enPartida = true;
        IDPartida = partesPartida[1];
        ronda = Integer.parseInt(partesPartida[2]);
        dineroBote = Integer.parseInt(partesPartida[3]);
        economia = Double.parseDouble(partesPartida[4]);
        evento = partesPartida[5];
        // perteneceTorneo = Boolean.parseBoolean(partesPartida[6]);
        String turno = partesPartida[7];
        return turno;
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
            System.out.println("Empieza la partida 2");
            if (miTurno) {
                while (CuentaInfoRecibida < JugadoresVivos - 1) {
                    System.out.println("Esperando");
                    ConexionServidor.esperar();
                }
                do {
                    System.out.println("Dentro");
                    limpiarTerminal();
                    gestionSubasta(scanner);
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

    private static void gestionSubasta(Scanner scanner) {
        if (subasta) {
            subasta = false;
            System.out.println("Hay una subasta en curso del jugador "
                    + jugador_subasta + " por " + precio_subasta
                    + " de la propiedad " + propiedad_subasta + ". Que desea hacer?");
            System.out.println("1 - Comprar");
            System.out.println("2 - Pasar");
            String mensaje = scanner.nextLine();
            switch (mensaje) {
                case "1":
                    // Comprar
                    ComprarSubasta();
                    break;
                case "2":
                    // Pasar
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
            System.out.println("4 - Subastar propiedad");
            System.out.println("5 - Finalizar turno");
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
                    gestionSubastarPropiedad(scanner);
                    break;
                case "5":
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

    // Le pregunta al usuario que propiedad de las que el tiene quiere subastar
    // y por que precio querria subastarla
    private static void gestionSubastarPropiedad(Scanner scanner) {
        System.out.println("Introduzca el numero de propiedad que quiere subastar:");
        System.out.println("0 - Volver al menú anterior");
        // Leer que propiedad quiere subastar el jugador
        String opcion = scanner.nextLine();
        if (opcion == "0") {
            return;
        } else {
            // Leer el precio de subasta
            System.out.println("Introduzca el precio de subasta:");
            String precio = scanner.nextLine();
            // Enviar la subasta al servidor
            subastarPropiedad(opcion, precio);
        }
        // Esperamos a recibir la respuesta del servidor "SUBASTA_OK"
        ConexionServidor.esperar();

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
        for (int i = 0; i < nombresPropiedadesEdificar.size(); i++) {
            String precio = preciosPropiedadesEdificar.get(i);
            String numProp = nombresPropiedadesEdificar.get(i);
            System.out.println(
                    (i + 1) + " - " + tablero[Integer.parseInt(numProp)] + " (" + precio + ")");
        }
        System.out.println("0 - Volver al menú anterior");

        int opcion = scanner.nextInt();
        if (opcion == 0) {
            return;
        }
        if (opcion > 0 && opcion <= nombresPropiedadesEdificar.size()) {
            // String numProp = nombresPropiedadesEdificar.get(opcion - 1);
            // Pasar la propiedad a entero
            // int propiedad = Integer.parseInt(numProp);
            // edificarPropiedad(propiedad); -- LO HE CAMBIADO YO MORO
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

    public static int obtenerIndiceJugador(String ID_Jugador) {
        for (int i = 0; i < ordenJugadores.length; i++) {
            if (ordenJugadores[i].equals(ID_Jugador)) {
                return i;
            }
        }
        return -1;
    }

};