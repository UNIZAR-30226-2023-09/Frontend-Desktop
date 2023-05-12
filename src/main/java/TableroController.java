import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class TableroController implements Initializable {

    @FXML
    private ListaJugadoresController listaJugadoresController;

    @FXML
    private ListaPropiedadesController listaPropiedadesController;

    @FXML
    private ChatController chatController;

    @FXML
    private ComprarPropiedadController comprarPropiedadController;

    @FXML
    private VenderPropiedadController venderPropiedadController;

    @FXML
    private EdificarController edificarController;

    @FXML
    private SubastarController subastarController;

    @FXML
    private ViajeAeropuertosController viajeAeropuertosController;

    @FXML
    private SuperpoderController superpoderController;

    @FXML
    private PujarController pujarController;

    @FXML
    private FianzaController fianzaController;

    @FXML
    private CasinoController casinoController;

    @FXML
    private EventosController eventosController;

    @FXML
    private BancoController bancoController;

    @FXML
    private ImageView dado1, dado2, user1, user2, user3, user4, tableroSkin;

    @FXML
    private ImageView imgCasa1, imgCasa2, imgCasa3, imgCasa4, imgCasa5, imgCasa6, imgCasa7, imgCasa8, imgCasa9,
            imgCasa10, imgCasa11, imgCasa12,
            imgCasa13, imgCasa14, imgCasa15, imgCasa16, imgCasa17, imgCasa18, imgCasa19, imgCasa20, imgCasa21,
            imgCasa22, imgCasa23;

    private final int NUM_CASAS = 23;

    private ObservableList<ImageView> Casas;

    @FXML
    private VBox datosPartida, listaJugadores, listaPropiedades, chat, comprarPropiedad, venderPropiedad,
            edificar, subastar, viajeAeropuertos, superpoder, pujar, fianza, casino, eventos, banco;

    @FXML
    private Button btnChat, btnTerminarTurno;

    @FXML
    private StackPane containerForm;

    @FXML
    public ProgressBar barraEconomia;

    @FXML
    public Circle indicador;

    @FXML
    private Label lblEvento, lblRonda, lblEconomia, lblBote, lblBanco;

    Random random = new Random();

    boolean heTerminadoTurno = false;

    private static Semaphore semaphoreDados = new Semaphore(0); // Semaforo de concurrencia

    private Timeline timeline;

    public String[] posicion_propiedad_tablero; // devuelve la posicion de una propiedad en el tablero

    public Integer eventoActual = 0;

    private void partida() throws IOException {
        // System.out.print("1");
        ConexionServidor.esperar();
        // System.out.print("2");
        // ConexionServidor.esperar(); //?????

        while (GestionPartida.enPartida) {
            // HABRA QUE PONERLO DONDE PEREZ
            // System.out.print("Estoy en partida?");
            dado1.setDisable(true);
            dado2.setDisable(true);

            if (GestionPartida.miTurno == true) {
                // System.out.print("Estoy en mi turno?");
                while (GestionPartida.CuentaInfoRecibida < (GestionPartida.JugadoresVivos - 1)) {
                    System.out.println("cuentaInfoRecibida?2");
                    System.out.println(GestionPartida.CuentaInfoRecibida);
                    System.out.println(" ");
                    System.out.println(" ");
                    ConexionServidor.esperar();
                }

                // si ha muerto algun jugador habra que poner en rojo su nombre y se oculta el
                // dinero
                System.out.println("\n");
                // System.out.println("aqui no llega no?");
                listaJugadoresController.muertos();

                listaJugadoresController.actualizarDinero();

                actualizarDatosPartida();

                actualizarEconomia();

                Integer eventoNuevo;
                eventoNuevo = EventosController.transformarEvento(GestionPartida.evento);

                if (eventoActual != eventoNuevo) {
                    eventoActual = eventoNuevo;
                    if (eventoNuevo != 0) {
                        datosPartida.setVisible(false);
                        chat.setVisible(false);
                        eventos.setVisible(true);

                        eventosController.mostrarEvento(eventoNuevo);

                        datosPartida.setVisible(true);
                        chat.setVisible(false);
                        eventos.setVisible(false);
                    }
                }

                // mostramos los botones de las propiedades que pertenezcan al jugador
                listaPropiedadesController.visibilidadBotonesVenta(true);
                listaPropiedadesController.visibilidadBotonesEdificar(true);
                listaPropiedadesController.visibilidadBotonesSubastar(true);

                // en caso de haber vendido alguna propiedad en una subasta deberemos eliminarla
                // de la lista
                if (subastarController.habiamosSubastado) {
                    subastarController.habiamosSubastado = false;

                    if (subastarController.subastaExitosa()) {
                        listaPropiedadesController.eliminarPropiedad(subastarController.propiedadSubastada);
                    }
                }

                // antes de que el jugador pueda tirar los dados mostramos la puja que este
                // activa
                if (GestionPartida.subasta) {
                    pujar.setVisible(true);
                    datosPartida.setVisible(false);
                    chat.setVisible(false);

                    GestionPartida.subasta = false;

                    if (pujarController.gestionarPujarPropiedad()) {
                        listaPropiedadesController.agnadirPropiedad(Integer.parseInt(GestionPartida.propiedad_subasta));
                        listaPropiedadesController.visibilidadBotonesVenta(true);
                        listaPropiedadesController.visibilidadBotonesEdificar(false);
                        listaPropiedadesController.visibilidadBotonesEdificar(true);
                        listaPropiedadesController.visibilidadBotonesSubastar(true);
                    }

                    pujar.setVisible(false);
                    datosPartida.setVisible(true);
                    chat.setVisible(true);
                }

                do {
                    dado1.setDisable(false);
                    dado2.setDisable(false);

                    // ESPERAR A QUE TIRE DADOS
                    try {
                        semaphoreDados.acquire();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // ConexionServidor.esperar();

                    // AQUI VAMOS A GESTIONAR EN QUE CASILLA HEMOS CAIDO PARA COMPRAR, BANCO Y
                    // CASINO

                    if (!GestionPartida.JugadorEnCarcel[GestionPartida.indiceJugador]) {
                        System.out.println("NO estoy en la carcel");

                        if (!GestionPartida.superPoder.equals("0")) {
                            // Superpoder
                            /*
                             * Los superPoderes son:
                             * 1. Elegir casilla a la que quieres ir,
                             * 2. Ir al banco
                             * 3. Ir al casino
                             * 4. Ir a la casilla de salida
                             * 5. Retroceder 3 casillas
                             * 6. Aumentar la suerte del casino
                             * 7. Vas carcel
                             * 8. Vas casilla de bote
                             * 9. Vas a zaragoza
                             * 10. ??
                             * 11. Vas a japon
                             * 12. ??
                             */
                            System.out.print("Superpoder:");
                            System.out.print(GestionPartida.superPoder);
                            System.out.print(" ");
                            System.out.print(" ");

                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            superpoder.setVisible(true);

                            superpoderController.gestionarSuperpoder();

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            superpoder.setVisible(false);

                            int i = Integer.parseInt(GestionPartida.superPoder);

                            switch (i) {
                                case 1:
                                    // Mover ficha??
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;

                                    while (!GestionPartida.meToca) {
                                        ConexionServidor.esperar();
                                    }
                                    GestionPartida.meToca = false;
                                    System.out.print("Sali me toca");

                                    if (GestionPartida.comprarPropiedad) {

                                        // si hemos caido en una propiedad que podamos comprar mostramos el menu para
                                        // comprar la misma
                                        datosPartida.setVisible(false);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(true);

                                        System.out.println("Compra Propiedad");

                                        if (comprarPropiedadController.gestionarComprarPropiedad()) {
                                            listaPropiedadesController.agnadirPropiedad(Integer
                                                    .parseInt(
                                                            GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]));
                                            listaPropiedadesController.visibilidadBotonesEdificar(false);
                                            listaPropiedadesController.visibilidadBotonesEdificar(true);
                                        }

                                        System.out.println("Propiedad");
                                        // dejamos como estaba todo
                                        datosPartida.setVisible(true);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(false);

                                        GestionPartida.comprarPropiedad = false;

                                    } else if (GestionPartida.enBanco) {
                                        // AQUI PONER QUE LA PANTALLA DE BANCO
                                        datosPartida.setVisible(false);
                                        chat.setVisible(false);
                                        banco.setVisible(true);

                                        if (bancoController.gestionBanco()) {
                                            listaJugadoresController.actualizarDinero();
                                        }

                                        datosPartida.setVisible(true);
                                        chat.setVisible(false);
                                        banco.setVisible(false);
                                        // SEMAFORO DE BANCO
                                        GestionPartida.enBanco = false;
                                    } else if (GestionPartida.apostarDinero) {
                                        // hemos caido en la casilla del casino por lo que se muestra la ventrana
                                        datosPartida.setVisible(false);
                                        chat.setVisible(false);
                                        casino.setVisible(true);

                                        if (casinoController.gestionarRule()) {
                                            listaJugadoresController.actualizarDinero();
                                        }

                                        datosPartida.setVisible(true);
                                        chat.setVisible(false);
                                        casino.setVisible(false);

                                        GestionPartida.apostarDinero = false;

                                    }
                                    break;
                                case 2:
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;

                                    datosPartida.setVisible(false);
                                    chat.setVisible(false);
                                    banco.setVisible(true);

                                    if (bancoController.gestionBanco()) {
                                        listaJugadoresController.actualizarDinero();
                                    }

                                    datosPartida.setVisible(true);
                                    chat.setVisible(false);
                                    banco.setVisible(false);
                                    // SEMAFORO DE BANCO
                                    GestionPartida.enBanco = false;
                                    break;
                                case 3:
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;

                                    datosPartida.setVisible(false);
                                    chat.setVisible(false);
                                    casino.setVisible(true);

                                    if (casinoController.gestionarRule()) {
                                        listaJugadoresController.actualizarDinero();
                                    }

                                    datosPartida.setVisible(true);
                                    chat.setVisible(false);
                                    casino.setVisible(false);

                                    GestionPartida.apostarDinero = false;
                                    break;
                                case 4:
                                    // MOVER FICHA???
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;
                                    break;
                                case 5:
                                    // MOVER FICHA??
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;

                                    if (GestionPartida.comprarPropiedad) {
                                        // si hemos caido en una propiedad que podamos comprar mostramos el menu para
                                        // comprar la misma
                                        datosPartida.setVisible(false);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(true);

                                        System.out.println("Compra Propiedad");

                                        if (comprarPropiedadController.gestionarComprarPropiedad()) {
                                            listaPropiedadesController.agnadirPropiedad(Integer
                                                    .parseInt(
                                                            GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]));
                                            listaPropiedadesController.visibilidadBotonesEdificar(false); // ocultamos
                                                                                                          // el boton de
                                                                                                          // edificar
                                            listaPropiedadesController.visibilidadBotonesEdificar(true); // ahora la

                                        }

                                        System.out.println("Propiedad");
                                        // dejamos como estaba todo
                                        datosPartida.setVisible(true);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(false);

                                        GestionPartida.comprarPropiedad = false;

                                    }
                                    break;
                                case 6:

                                    break;
                                case 7:
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;
                                    break;
                                case 8:
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;
                                    break;
                                case 9:
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;

                                    if (GestionPartida.comprarPropiedad) {
                                        // si hemos caido en una propiedad que podamos comprar mostramos el menu para
                                        // comprar la misma
                                        datosPartida.setVisible(false);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(true);

                                        System.out.println("Compra Propiedad");

                                        if (comprarPropiedadController.gestionarComprarPropiedad()) {
                                            listaPropiedadesController.agnadirPropiedad(Integer
                                                    .parseInt(
                                                            GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]));
                                            listaPropiedadesController.visibilidadBotonesEdificar(false);
                                            listaPropiedadesController.visibilidadBotonesEdificar(true);
                                        }

                                        System.out.println("Propiedad");
                                        // dejamos como estaba todo
                                        datosPartida.setVisible(true);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(false);

                                        GestionPartida.comprarPropiedad = false;

                                    }

                                    break;
                                case 10:

                                    break;
                                case 11:
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;

                                    if (GestionPartida.comprarPropiedad) {
                                        // si hemos caido en una propiedad que podamos comprar mostramos el menu para
                                        // comprar la misma
                                        datosPartida.setVisible(false);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(true);

                                        System.out.println("Compra Propiedad");

                                        if (comprarPropiedadController.gestionarComprarPropiedad()) {
                                            listaPropiedadesController.agnadirPropiedad(Integer
                                                    .parseInt(
                                                            GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]));
                                            listaPropiedadesController.visibilidadBotonesEdificar(false);
                                            listaPropiedadesController.visibilidadBotonesEdificar(true);
                                        }

                                        System.out.println("Propiedad");
                                        // dejamos como estaba todo
                                        datosPartida.setVisible(true);
                                        chat.setVisible(false);
                                        comprarPropiedad.setVisible(false);

                                        GestionPartida.comprarPropiedad = false;

                                    }

                                    break;
                                case 12:

                                    break;
                                default:
                                    System.out.println("ERROR SUPERPODER");
                                    break;
                            }

                            GestionPartida.superPoder = "0";
                        } else if (GestionPartida.comprarPropiedad) {

                            // si hemos caido en una propiedad que podamos comprar mostramos el menu para
                            // comprar la misma
                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            comprarPropiedad.setVisible(true);

                            System.out.println("Compra Propiedad");

                            if (comprarPropiedadController.gestionarComprarPropiedad()) {
                                listaPropiedadesController.agnadirPropiedad(Integer
                                        .parseInt(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]));
                                listaPropiedadesController.visibilidadBotonesEdificar(false);
                                listaPropiedadesController.visibilidadBotonesEdificar(true);
                            }

                            System.out.println("Propiedad");
                            // dejamos como estaba todo
                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            comprarPropiedad.setVisible(false);

                            GestionPartida.comprarPropiedad = false;

                        } else if (GestionPartida.enBanco) {
                            // AQUI PONER QUE LA PANTALLA DE BANCO
                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            banco.setVisible(true);

                            if (bancoController.gestionBanco()) {
                                listaJugadoresController.actualizarDinero();
                            }

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            banco.setVisible(false);
                            // SEMAFORO DE BANCO
                            GestionPartida.enBanco = false;
                        } else if (GestionPartida.apostarDinero) {
                            // hemos caido en la casilla del casino por lo que se muestra la ventrana
                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            casino.setVisible(true);

                            if (casinoController.gestionarRule()) {
                                listaJugadoresController.actualizarDinero();
                            }

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            casino.setVisible(false);

                            GestionPartida.apostarDinero = false;

                        } else if (GestionPartida.propiedadADesplazarseAvion != null) { // PEDIR MENSAJE?
                            System.out.println("Entro aqui, me toca??");

                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            viajeAeropuertos.setVisible(true);

                            try {
                                ViajeAeropuertosController.semaphoreAeropuerto.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            viajeAeropuertos.setVisible(false);

                            System.out.print("A desplazarse:");
                            System.out.print(GestionPartida.propiedadADesplazarseAvion);
                            moverFichaSuperpoder(GestionPartida.propiedadADesplazarseAvion);
                            GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarseAvion;

                            GestionPartida.propiedadADesplazarseAvion = null;
                        }
                    } else {
                        fianzaController.pagar();
                    }

                } while (GestionPartida.dadosDobles);

                System.out.println("Pulsa el boton para terminar el turno");

                // mostar boton de finalizar turno y esperar a que lo pulsen
                btnTerminarTurno.setVisible(true);
                while (!heTerminadoTurno) {
                    // dormir el hilo 1 segundo
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                heTerminadoTurno = false;
                System.out.println("FIN TURNO");

                // ocultar lo que no queramos que se vea cunado no sea nuestro turno
                btnTerminarTurno.setVisible(false);
                listaPropiedadesController.visibilidadBotonesVenta(false);
                listaPropiedadesController.visibilidadBotonesEdificar(false);
                listaPropiedadesController.visibilidadBotonesSubastar(false);

            }
            System.out.print("Estoy aqui verdad?");
            ConexionServidor.esperar(); // ESTE COñexion esperar al morir nosotros creo que nos da problemas
            System.out.print("Estoy verdad?");
        }
        // si salimos del while es que la partida ha terminado para nosotros
        System.out.println("Ganaste rey, ahora sal de aqui");

        App.setRoot("FinPartida");
    }

    @FXML
    public void tirarDados(MouseEvent e) // HAY QUE COMPROBAR QUE SEA NUESTRO TURNO
    {
        dado1.setDisable(true);
        dado2.setDisable(true);
        GestionPartida.CuentaInfoRecibida = 0;
        ImageView imagenDado = (ImageView) e.getSource();
        if (imagenDado.getId().equals("dado1") || imagenDado.getId().equals("dado2")) {
            GestionPartida.lanzarDados(GestionPartida.nombreUser, GestionPartida.IDPartida);

            ConexionServidor.esperar();

            Thread threadL = new Thread() {
                public void run() {

                    try {
                        for (int i = 0; i < 15; i++) {
                            File file = new File("src/main/resources/Dice" + (random.nextInt(6) + 1) + ".png");
                            dado1.setImage(new Image(file.toURI().toString()));
                            Thread.sleep(50);
                        }
                        File file = new File("src/main/resources/Dice" + GestionPartida.dados[0] + ".png");
                        dado1.setImage(new Image(file.toURI().toString()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread threadR = new Thread() {
                public void run() {

                    try {
                        for (int i = 0; i < 15; i++) {
                            File file = new File("src/main/resources/Dice" + (random.nextInt(6) + 1) + ".png");
                            dado2.setImage(new Image(file.toURI().toString()));
                            Thread.sleep(50);
                        }
                        File file = new File("src/main/resources/Dice" + GestionPartida.dados[1] + ".png");
                        dado2.setImage(new Image(file.toURI().toString()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            threadL.start();
            threadR.start();

            while (!GestionPartida.meToca) {
                ConexionServidor.esperar();
            }
            GestionPartida.meToca = false;

            System.out.println("dado1");
            System.out.println(GestionPartida.dados[0]);

            System.out.println("dado2");
            System.out.println(GestionPartida.dados[1]);

            System.out.println(" ");
            System.out.println(" ");

            if (GestionPartida.JugadorEnCarcel[GestionPartida.indiceJugador] == false) {

                String posi = "Pos" + String.valueOf(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);
                Integer jug = GestionPartida.indiceJugador;

                System.out.println("posi");
                System.out.println(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);
                System.out.println(" ");
                System.out.println(" ");

                String coordenadas;
                switch (jug) {
                    case 0:
                        coordenadas = DatosPartida.mapaPropiedades1.get(posi);
                        break;
                    case 1:
                        coordenadas = DatosPartida.mapaPropiedades2.get(posi);
                        break;
                    case 2:
                        coordenadas = DatosPartida.mapaPropiedades3.get(posi);
                        break;
                    case 3:
                        coordenadas = DatosPartida.mapaPropiedades4.get(posi);
                        break;
                    default:
                        coordenadas = "ERROR";
                        System.out.println("ERROR CASILLA1");
                        break;
                }

                String[] partes = coordenadas.split(",");
                int x = Integer.parseInt(partes[0]);
                int y = Integer.parseInt(partes[1]);

                switch (jug) {
                    case 0:
                        user1.setLayoutX(x);
                        user1.setLayoutY(y);
                        break;
                    case 1:
                        user2.setLayoutX(x);
                        user2.setLayoutY(y);
                        break;
                    case 2:
                        user3.setLayoutX(x);
                        user3.setLayoutY(y);
                        break;
                    case 3:
                        user4.setLayoutX(x);
                        user4.setLayoutY(y);
                        break;
                    default:
                        System.out.println("ERROR CASILLA2");
                        break;
                }
            }
            semaphoreDados.release();

        }

        // System.out.println("DadosDobles?");
        // System.out.println(GestionPartida.dadosDobles);
        // System.out.println(" ");
        // System.out.println(" ");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        listaPropiedadesController.setTableroController(this);
        venderPropiedadController.setTableroController(this);
        edificarController.setTableroController(this);
        subastarController.setTableroController(this);
        viajeAeropuertosController.setTableroController(this);
        fianzaController.setTableroController(this);

        // inicializamos el vector
        posicion_propiedad_tablero = new String[] { "0", "2", "3", "7", "8", "10", "12", "13", "15",
                "17", "19", "20", "22", "23", "25", "27", "29", "30",
                "32", "33", "35", "37", "38", "40", "6", "16", "26", "36" };

        Casas = FXCollections.observableArrayList();
        Casas.add(imgCasa1);
        Casas.add(imgCasa2);
        Casas.add(imgCasa3);
        Casas.add(imgCasa4);
        Casas.add(imgCasa5);
        Casas.add(imgCasa6);
        Casas.add(imgCasa7);
        Casas.add(imgCasa8);
        Casas.add(imgCasa9);
        Casas.add(imgCasa10);
        Casas.add(imgCasa11);
        Casas.add(imgCasa12);
        Casas.add(imgCasa13);
        Casas.add(imgCasa14);
        Casas.add(imgCasa15);
        Casas.add(imgCasa16);
        Casas.add(imgCasa17);
        Casas.add(imgCasa18);
        Casas.add(imgCasa19);
        Casas.add(imgCasa20);
        Casas.add(imgCasa21);
        Casas.add(imgCasa22);
        Casas.add(imgCasa23);

        // mostramos y ocultamos los elementos para conseguir la vista basica del
        // tablero
        datosPartida.setVisible(true);
        chat.setVisible(false);
        comprarPropiedad.setVisible(false);
        banco.setVisible(false);
        venderPropiedad.setVisible(false);
        edificar.setVisible(false);
        casino.setVisible(false);
        superpoder.setVisible(false);
        subastar.setVisible(false);
        viajeAeropuertos.setVisible(false);
        pujar.setVisible(false);
        fianza.setVisible(false);
        eventos.setVisible(false);
        btnTerminarTurno.setVisible(false); // hasta que no sea mi turno no mostramos el boton

        inicializarFichas();

        // tableroSkin
        System.out.print("Skin tablero:");
        System.out.print(GestionPartida.skinTablero);
        File file = new File("src/main/resources/TABLEROS/" + GestionPartida.skinTablero + ".png"); //
        tableroSkin.setImage(new Image(file.toURI().toString()));

        for (int i = 1; i <= NUM_CASAS; i++) {
            Casas.get(i - 1).setVisible(false);
        }

        System.out.print("Estoy en otro dispositivo:");
        System.out.print(GestionPartida.actualizar_cambio_dispositivo);
        // comprobamos si venimos de otro dispositivo
        if (GestionPartida.actualizar_cambio_dispositivo) {
            // mostrar las propiedades que tenia el jugador
            listaPropiedadesController.actualizarPropiedades();

            // posiciones de los jugadores
            actualizar();

            // mostrar los edificios de cada jugador

            // mostrar datos partida (dinero, economia...) -> actualizarDatosPartida()
            listaJugadoresController.actualizarDinero();

            actualizarDatosPartida();

            actualizarEconomia();

            // jugadores muertos -> se hace con el actualizarrrr

            // ponemos a false la variable una vez hemos actualizado toda la informacion
            GestionPartida.actualizar_cambio_dispositivo = false;
            System.out.print("He salido del actualizar dispositivo");
        }

        Thread threadIni = new Thread() {
            public void run() {
                try {
                    partida();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        threadIni.start();

        timeline = new Timeline();
        Duration interval = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(interval, event -> {
            if (!estamosActualizando) {
                actualizar();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void actionEvent(ActionEvent e) {
        Object evt = e.getSource();

        if (evt.equals(btnChat)) {
            if (!chat.isVisible()) {
                chatController.actualizarChat(GestionPartida.chat);
            }
            datosPartida.setVisible(!datosPartida.isVisible());
            // TODO: Ocultar el resto de cosas

            chat.setVisible(!chat.isVisible());
        } else if (evt.equals(btnTerminarTurno)) {
            // mirar que hacer cuando le den al boton
            heTerminadoTurno = true;
            GestionPartida.finTurno();
            GestionPartida.miTurno = false;
            System.out.println("LE DI AL BOTON DE TERMINAR TURNO");
        }

    }

    private boolean estamosActualizando = false;

    private void actualizar() {
        if (!estamosActualizando) {
            estamosActualizando = true;
            for (int i = 0; i < 4; i++) {

                String posicion = "Pos" + String.valueOf(GestionPartida.posicionesJugadores[i]);

                String coordenadas;
                switch (i) {
                    case 0:
                        coordenadas = DatosPartida.mapaPropiedades1.get(posicion);
                        break;
                    case 1:
                        coordenadas = DatosPartida.mapaPropiedades2.get(posicion);
                        break;
                    case 2:
                        coordenadas = DatosPartida.mapaPropiedades3.get(posicion);
                        break;
                    case 3:
                        coordenadas = DatosPartida.mapaPropiedades4.get(posicion);
                        break;
                    default:
                        coordenadas = "ERROR";
                        System.out.println("ERROR CASILLA1");
                        break;
                }

                String[] partes = coordenadas.split(",");
                int x = Integer.parseInt(partes[0]);
                int y = Integer.parseInt(partes[1]);

                switch (i) {
                    case 0:
                        user1.setLayoutX(x);
                        user1.setLayoutY(y);
                        break;
                    case 1:
                        user2.setLayoutX(x);
                        user2.setLayoutY(y);
                        break;
                    case 2:
                        user3.setLayoutX(x);
                        user3.setLayoutY(y);
                        break;
                    case 3:
                        user4.setLayoutX(x);
                        user4.setLayoutY(y);
                        break;
                    default:
                        System.out.println("ERROR CASILLA2");
                        break;
                }
                /*
                 * try {
                 * Thread.sleep(1000); //TODAVIA NO FUNCIONA BIEN
                 * } catch (InterruptedException ex) {
                 * ex.printStackTrace();
                 * }
                 */

                if (GestionPartida.jugadoresVivos[i] == false) {
                    System.out.println("La ha palmado alguien");
                    switch (i) {
                        case 0:
                            user1.setVisible(false);
                            break;
                        case 1:
                            user2.setVisible(false);
                            break;
                        case 2:
                            user3.setVisible(false);
                            break;
                        case 3:
                            user4.setVisible(false);
                            break;
                        default:
                            System.out.println("ERROR CASILLA2");
                            break;
                    }
                }
            }

            estamosActualizando = false;
        }

    }

    public void mostrarVentanaVenta(int numPropiedad) {
        venderPropiedad.setVisible(true);
        chat.setVisible(false);
        // datosPartida.setVisible(false);
        listaPropiedades.setVisible(false);

        System.out.println("En el tablero es la posicion: " + posicion_propiedad_tablero[numPropiedad]);

        GestionPartida.quieroVenderPropiedad(posicion_propiedad_tablero[numPropiedad]);

        while (!GestionPartida.precioPropiedadRecibido) {
            // System.out.println("Acabo de entrar");
            // ConexionServidor.esperar();
            // System.out.println("NO salgo");
        }

        GestionPartida.precioPropiedadRecibido = false;

        venderPropiedadController.actualizarLabel(numPropiedad);
    }

    public void ocultarVentanaVenta(int numPropiedad, Boolean vendida) {
        if (vendida) {
            listaJugadoresController.actualizarDinero();
            listaPropiedadesController.eliminarPropiedad(numPropiedad);
        }

        venderPropiedad.setVisible(false);
        chat.setVisible(true);
        // datosPartida.setVisible(true);
        listaPropiedades.setVisible(true);
    }

    public void mostrarVentanaEdificar(int numPropiedad, int precio) {
        edificar.setVisible(true);
        chat.setVisible(false);
        listaPropiedades.setVisible(false);

        edificarController.actualizarLabel(numPropiedad, precio);
    }

    public void ocultarVentanaEdificar(int numPropiedad, Boolean edificada) {
        // faltara poner la imagen que toque

        edificar.setVisible(false);
        chat.setVisible(true);
        listaPropiedades.setVisible(true);
    }

    public void mostrarVentanaSubastar(int numPropiedad) {
        subastar.setVisible(true);
        chat.setVisible(false);
        listaPropiedades.setVisible(false);

        subastarController.actualizarLabel(numPropiedad);
    }

    public void ocultarVentanaSubastar(Boolean subastaActiva, int numPropiedad) {
        if (subastaActiva) {
            listaPropiedadesController.visibilidadBotonesSubastar(false);
            listaPropiedadesController.ocultarBotonesPropiedadSubastada(numPropiedad);
        }

        subastar.setVisible(false);
        chat.setVisible(true);
        listaPropiedades.setVisible(true);
    }

    public void mostrarVentanaFianza() {
        fianza.setVisible(true);
        chat.setVisible(false);
        datosPartida.setVisible(false);
    }

    public void ocultarVentanaFianza() {
        fianza.setVisible(false);
        chat.setVisible(true);
        datosPartida.setVisible(true);
    }

    private void actualizarDatosPartida() {
        Platform.runLater(() -> {
            lblEvento.setText(GestionPartida.evento);
            lblRonda.setText(Integer.toString(GestionPartida.ronda));
            lblEconomia.setText(Double.toString(GestionPartida.economia));
            lblBote.setText(Integer.toString(GestionPartida.dineroBote));
            lblBanco.setText(Integer.toString(GestionPartida.dineroEnBanco));
        });
    }

    private void actualizarEconomia() {
        // double valor = -0.5;
        barraEconomia.setStyle("-fx-accent: green; -fx-base: red;");

        double minValue = 0.5;
        double maxValue = 2.0;
        double progress = (GestionPartida.economia - minValue) / (maxValue - minValue); // Calcular el progreso en base
                                                                                        // al valor
        barraEconomia.setProgress(progress); // Actualizar el progreso de la barra

        double progressBarWidth = barraEconomia.getWidth();
        double circleRadius = indicador.getRadius();
        double circleCenterX = progressBarWidth * progress + circleRadius; // Calcular la posición X del círculo
        indicador.setCenterX(circleCenterX);

        // barraEconomia.setProgress(GestionPartida.economia);
    }

    private void moverFichaSuperpoder(String casilla) {
        Integer jug = GestionPartida.indiceJugador;
        String posi = "Pos" + casilla;
        String coordenadas;
        switch (jug) {
            case 0:
                coordenadas = DatosPartida.mapaPropiedades1.get(posi);
                break;
            case 1:
                coordenadas = DatosPartida.mapaPropiedades2.get(posi);
                break;
            case 2:
                coordenadas = DatosPartida.mapaPropiedades3.get(posi);
                break;
            case 3:
                coordenadas = DatosPartida.mapaPropiedades4.get(posi);
                break;
            default:
                coordenadas = "ERROR";
                System.out.println("ERROR CASILLA1");
                break;
        }

        String[] partes = coordenadas.split(",");
        int x = Integer.parseInt(partes[0]);
        int y = Integer.parseInt(partes[1]);

        switch (jug) {
            case 0:
                user1.setLayoutX(x);
                user1.setLayoutY(y);
                break;
            case 1:
                user2.setLayoutX(x);
                user2.setLayoutY(y);
                break;
            case 2:
                user3.setLayoutX(x);
                user3.setLayoutY(y);
                break;
            case 3:
                user4.setLayoutX(x);
                user4.setLayoutY(y);
                break;
            default:
                System.out.println("ERROR CASILLA2");
                break;
        }
    }

    private void inicializarFichas() {
        System.out.println("SKINS: \n");
        // Mostrar las skins de todos los usuarios
        for (int i = 0; i < 4; i++) {
            System.out.println("Skin del jugador:" + GestionPartida.skinsJugadores[i]);
        }
        ImageView aux = user1;
        for (int i = 0; i < 4; i++) {

            switch (i) {
                case 0:
                    aux = user1;
                    break;
                case 1:
                    aux = user2;
                    break;
                case 2:
                    aux = user3;
                    break;
                case 3:
                    aux = user4;
                    break;
                default:
                    System.out.println("ERROR SKINS1");
                    break;
            }

            switch (GestionPartida.skinsJugadores[i]) {
                case "BAXTER":
                    // azul
                    System.out.println("AZUL");
                    File file1 = new File("src/main/resources/FICHAS/AZUL" + (i + 1) + ".png");
                    aux.setImage(new Image(file1.toURI().toString()));
                    break;
                case "BERTA":
                    // naranja
                    System.out.println("NARANJA");
                    File file2 = new File("src/main/resources/FICHAS/NARANJA" + (i + 1) + ".png");
                    aux.setImage(new Image(file2.toURI().toString()));
                    break;
                case "DIONIX":
                    // verde
                    System.out.println("VERDE");
                    File file3 = new File("src/main/resources/FICHAS/VERDE" + (i + 1) + ".png");
                    aux.setImage(new Image(file3.toURI().toString()));
                    break;
                case "JEANCARLO":
                    // amarillo
                    System.out.println("AMARILLO");
                    File file4 = new File("src/main/resources/FICHAS/AMARILLO" + (i + 1) + ".png");
                    aux.setImage(new Image(file4.toURI().toString()));
                    break;
                case "JULS":
                    // morado
                    System.out.println("MORADO");
                    File file5 = new File("src/main/resources/FICHAS/MORADO" + (i + 1) + ".png");
                    aux.setImage(new Image(file5.toURI().toString()));
                    break;
                case "LUCAS":
                    // rojo
                    System.out.println("ROJO");
                    File file6 = new File("src/main/resources/FICHAS/ROJO" + (i + 1) + ".png");
                    aux.setImage(new Image(file6.toURI().toString()));
                    break;
                case "PLEX":
                    // rosa
                    System.out.println("ROSA");
                    File file7 = new File("src/main/resources/FICHAS/ROSA" + (i + 1) + ".png");
                    aux.setImage(new Image(file7.toURI().toString()));
                    break;
                case "TITE":
                    // marron
                    System.out.println("MARRON");
                    File file8 = new File("src/main/resources/FICHAS/MARRON" + (i + 1) + ".png");
                    aux.setImage(new Image(file8.toURI().toString()));
                    break;
                default:
                    System.out.println("ERROR SKIN2");
                    break;
            }
        }
    }
}