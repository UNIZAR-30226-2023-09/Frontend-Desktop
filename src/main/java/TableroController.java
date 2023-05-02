import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    private ImageView dado1, dado2, user1, user2, user3, user4;

    @FXML
    private VBox datosPartida, listaJugadores, listaPropiedades, chat, comprarPropiedad, venderPropiedad, edificar,
            subastar, viajeAeropuertos, superpoder;

    public static VBox banco, casino/*, superpoder*/;

    @FXML
    private Button btnChat, btnTerminarTurno;

    @FXML
    private StackPane containerForm;

    @FXML
    public ProgressBar barraEconomia;

    @FXML
    public Circle indicador;

    @FXML
    private Label lblEvento, lblRonda, lblEconomia;

    Random random = new Random();

    boolean heTerminadoTurno = false;

    private static Semaphore semaphoreDados = new Semaphore(0); // Semaforo de concurrencia

    private Timeline timeline;

    public String[] posicion_propiedad_tablero; // devuelve la posicion de una propiedad en el tablero

    private void partida() {
        ConexionServidor.esperar();
        // ConexionServidor.esperar(); //?????
        while (GestionPartida.enPartida) {
            // HABRA QUE PONERLO DONDE PEREZ
            dado1.setDisable(true);
            dado2.setDisable(true);

            if (GestionPartida.miTurno == true) {

                while (GestionPartida.CuentaInfoRecibida < (GestionPartida.JugadoresVivos - 1)) {
                    System.out.println("cuentaInfoRecibida?2");
                    System.out.println(GestionPartida.CuentaInfoRecibida);
                    System.out.println(" ");
                    System.out.println(" ");
                    ConexionServidor.esperar();
                }

                listaJugadoresController.actualizarDinero();

                actualizarDatosPartida();

                actualizarEconomia();

                listaPropiedadesController.visibilidadBotonesVenta(true);

                listaPropiedadesController.visibilidadBotonesEdificar(true);

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

                            //  --- actualizarSuperpoder();
                            superpoderController.gestionarSuperpoder();

                            // try {
                            //     SuperpoderController.semaphoreSuper.acquire();
                            // } catch (InterruptedException e) {
                            //     e.printStackTrace();
                            // }

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            superpoder.setVisible(false);

                            int i = Integer.parseInt(GestionPartida.superPoder);

                            switch (i) {
                                case 1:
                                    // Mover ficha??
                                    moverFichaSuperpoder(SuperpoderController.casillaS);
                                    break;
                                case 2:
                                    System.out.print("A desplazarse:");
                                    System.out.print(GestionPartida.propiedadADesplazarse);
                                    moverFichaSuperpoder(GestionPartida.propiedadADesplazarse);
                                    GestionPartida.posicionesJugadores[GestionPartida.indiceJugador] = GestionPartida.propiedadADesplazarse;

                                    datosPartida.setVisible(false);
                                    chat.setVisible(false);
                                    banco.setVisible(true);

                                    try {
                                        BancoController.semaphoreBanco.acquire();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
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

                                    // semaforo para esperar a que se pulse algun boton del casino (apostar o
                                    // retirarse)
                                    try {
                                        CasinoController.semaphoreCasino.acquire();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
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

                            try {
                                BancoController.semaphoreBanco.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
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

                            // semaforo para esperar a que se pulse algun boton del casino (apostar o
                            // retirarse)
                            try {
                                CasinoController.semaphoreCasino.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            casino.setVisible(false);

                            GestionPartida.apostarDinero = false;

                        } else if (GestionPartida.propiedadADesplazarseAvion != null){  //PEDIR MENSAJE?
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
                    }

                } while (GestionPartida.dadosDobles);

                // mostar boton de finalizar turno y esperar a que lo pulsen
                btnTerminarTurno.setVisible(true);
                while (!heTerminadoTurno) {
                    ConexionServidor.esperar();
                }
                heTerminadoTurno = false;
                System.out.println("FIN TURNO");

                // ocultar lo que no queramos que se vea cunado no sea nuestro turno
                btnTerminarTurno.setVisible(false);
                listaPropiedadesController.visibilidadBotonesVenta(false);
                listaPropiedadesController.visibilidadBotonesEdificar(false);

            }
            ConexionServidor.esperar();
        }
        System.out.println("Ganaste rey, ahora sal de aqui");
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

        // inicializamos el vector
        posicion_propiedad_tablero = new String[] { "0", "2", "3", "7", "8", "10", "12", "13", "15",
                "17", "19", "20", "22", "23", "25", "27", "29", "30",
                "32", "33", "35", "37", "38", "40", "6", "16", "26", "36" };

        try {
            banco = loadForm("Banco.fxml");
            casino = loadForm("Casino.fxml");
            //superpoder = loadForm("Superpoder.fxml");

            // HAY QUE AÑADIR AQUI EL VBOX COMPRA.CASINO Y BANCO

            containerForm.getChildren().addAll(banco, casino/*, superpoder*/);
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
            btnTerminarTurno.setVisible(false); // hasta que no sea mi turno no mostramos el boton

            inicializarFichas();

            // comprobamos si venimos de otro dispositivo
            if(GestionPartida.actualizar_cambio_dispositivo)
            {
                // mostrar las propiedades que tenia el jugador 
                listaPropiedadesController.actualizarPropiedades();

                // posiciones de los jugadores
                actualizar();

                // mostrar los edificios de cada jugador

                // mostrar datos partida (dinero, economia...) -> actualizarDatosPartida()
                listaJugadoresController.actualizarDinero();

                actualizarDatosPartida();

                actualizarEconomia();

                // jugadores muertos

                // ponemos a false la variable una vez hemos actualizado toda la informacion
                GestionPartida.actualizar_cambio_dispositivo = false;
            }

            Thread threadIni = new Thread() {
                public void run() {
                    partida();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VBox loadForm(String ur1) throws IOException {
        return (VBox) FXMLLoader.load(getClass().getResource(ur1));
    }

    @FXML
    public void actionEvent(ActionEvent e) {
        Object evt = e.getSource();

        if (evt.equals(btnChat)) {
            datosPartida.setVisible(!datosPartida.isVisible());
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
        System.out.println("Vamos pal while");

        while (!GestionPartida.precioPropiedadRecibido) {
            // System.out.println("Acabo de entrar");
            // ConexionServidor.esperar();
            System.out.println("NO salgo");
        }

        GestionPartida.precioPropiedadRecibido = false;

        System.out.println("he salido");

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
    }

    public void ocultarVentanaSubastar() {
        // faltara poner la imagen que toque

        subastar.setVisible(false);
        chat.setVisible(true);
        listaPropiedades.setVisible(true);
    }

    private void actualizarSuperpoder() {
        Platform.runLater(() -> {
            VBox vbox = (VBox) superpoder.getChildren().get(2); // ESTO HAY QUE MIRAR QUE SEAN ESTOS

            Label lbl = (Label) vbox.getChildren().get(3); // ESTO HAY QUE MIRAR QUE SEAN ESTOS

            ImageView imgV = (ImageView) superpoder.getChildren().get(1);

            HBox hbox = (HBox) vbox.getChildren().get(0);

            // TextField text = (TextField) hbox.getChildren().get(0); // TextField
            // textField = (TextField)

            int i = Integer.parseInt(GestionPartida.superPoder);

            switch (i) {
                case 1:
                    lbl.setText("Elija la casilla a la que quiere ir");
                    File file = new File("src/main/resources/SUPERPODERES/SP1.png");
                    imgV.setImage(new Image(file.toURI().toString()));
                    // SuperpoderController.txtCasilla.setVisible(true);
                    break;
                case 2:
                    lbl.setText("Acudes corriendo al banco");
                    File file2 = new File("src/main/resources/SUPERPODERES/SP2.png");
                    imgV.setImage(new Image(file2.toURI().toString()));
                    break;
                case 3:
                    lbl.setText("Acudes corriendo al casino");
                    File file3 = new File("src/main/resources/SUPERPODERES/SP3.png");
                    imgV.setImage(new Image(file3.toURI().toString()));
                    break;
                case 4:
                    lbl.setText("Acudes corriendo a la casilla de salida");
                    File file4 = new File("src/main/resources/SUPERPODERES/SP4.png");
                    imgV.setImage(new Image(file4.toURI().toString()));
                    break;
                case 5:
                    lbl.setText("Retrocedes 3 casillas");
                    File file5 = new File("src/main/resources/SUPERPODERES/SP5.png");
                    imgV.setImage(new Image(file5.toURI().toString()));
                    break;
                case 6:
                    lbl.setText("Aumenta su suerte en el casino");
                    File file6 = new File("src/main/resources/SUPERPODERES/SP6.png");
                    imgV.setImage(new Image(file6.toURI().toString()));
                    break;
                case 7:
                    File file7 = new File("src/main/resources/SUPERPODERES/SP7.png");
                    imgV.setImage(new Image(file7.toURI().toString()));
                    break;
                case 8:
                    File file8 = new File("src/main/resources/SUPERPODERES/SP8.png");
                    imgV.setImage(new Image(file8.toURI().toString()));
                    break;
                case 9:
                    File file9 = new File("src/main/resources/SUPERPODERES/SP9.png");
                    imgV.setImage(new Image(file9.toURI().toString()));
                    break;
                case 10:
                    File file10 = new File("src/main/resources/SUPERPODERES/SP10.png");
                    imgV.setImage(new Image(file10.toURI().toString()));
                    break;
                case 11:
                    File file11 = new File("src/main/resources/SUPERPODERES/SP11.png");
                    imgV.setImage(new Image(file11.toURI().toString()));
                    break;
                case 12:
                    File file12 = new File("src/main/resources/SUPERPODERES/SP12.png");
                    imgV.setImage(new Image(file12.toURI().toString()));
                    break;
                default:
                    System.out.println("ERROR SUPERPODER");
                    break;
            }

        });
    }

    private void actualizarDatosPartida() {
        Platform.runLater(() -> {
            lblEvento.setText(GestionPartida.evento);
            lblRonda.setText(Integer.toString(GestionPartida.ronda));
            lblEconomia.setText(Double.toString(GestionPartida.economia));
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