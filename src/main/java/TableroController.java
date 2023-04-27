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
    private ImageView dado1, dado2, user1, user2, user3, user4;

    @FXML
    private VBox datosPartida, listaJugadores, listaPropiedade, chat, comprarPropiedad;

    public static VBox banco, vender, casino, superpoder;

    @FXML
    private Button btnChat, btnTerminarTurno, Qb, Wb;

    @FXML
    private StackPane containerForm;

    @FXML
    public ProgressBar barraEconomia;

    @FXML
    public Circle indicador;

    @FXML
    private Label lblEvento, lblRonda, lblEconomia; 

    Random random = new Random();
    
    private static Semaphore semaphoreDados = new Semaphore(0); // Semaforo de concurrencia

    private Timeline timeline;

    private void partida(){
        ConexionServidor.esperar();
        //ConexionServidor.esperar();  //?????
        while(GestionPartida.enPartida){          
              //HABRA QUE PONERLO DONDE PEREZ
            dado1.setDisable(true);
            dado2.setDisable(true);
            
            
            if (GestionPartida.miTurno == true) {
                 
                while (GestionPartida.CuentaInfoRecibida < (GestionPartida.JugadoresVivos-1)) {
                    System.out.println("cuentaInfoRecibida?2");
                    System.out.println(GestionPartida.CuentaInfoRecibida);
                    System.out.println(" ");
                    System.out.println(" ");
                    ConexionServidor.esperar();
                }
                
                listaJugadoresController.actualizarDinero();
                actualizarDatosPartida();

                actualizarEconomia();
                
                do {
                    dado1.setDisable(false);
                    dado2.setDisable(false);
                    
                    //ESPERAR A QUE TIRE DADOS
                    try {
                        semaphoreDados.acquire();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    ConexionServidor.esperar(); 

                    //AQUI VAMOS A GESTIONAR EN QUE CASILLA HEMOS CAIDO PARA COMPRAR, BANCO Y CASINO
                    
                    if (!GestionPartida.enCarcel) {
                        if (GestionPartida.comprarPropiedad) {

                            // si hemos caido en una propiedad que podamos comprar mostramos el menu para comprar la misma
                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            comprarPropiedad.setVisible(true);

                            System.out.println("Compra Propiedad");

                            if(comprarPropiedadController.gestionarComprarPropiedad())
                            {
                                listaPropiedadesController.agnadirPropiedad(Integer.parseInt(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]));
                            }
                             
                            System.out.println("Propiedad");
                            // dejamos como estaba todo
                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            comprarPropiedad.setVisible(false);

                            GestionPartida.comprarPropiedad=false;
                            
                        } else if (GestionPartida.apostarDinero) {
                            // hemos caido en la casilla del casino por lo que se muestra la ventrana
                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            casino.setVisible(true);

                            // semaforo para esperar a que se pulse algun boton del casino (apostar o retirarse)
                            try {
                                CasinoController.semaphoreCasino.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            casino.setVisible(false);

                            GestionPartida.apostarDinero=false;

                        } else if (GestionPartida.enBanco) {
                            //AQUI PONER QUE LA PANTALLA DE BANCO
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
                            //SEMAFORO DE BANCO
                            GestionPartida.enBanco=false;
                        } else if(GestionPartida.superPoder != "0"){
                            //Superpoder
                            /*
                            Los superPoderes son:
                            1. Elegir casilla a la que quieres ir,
                            2. Ir al banco
                            3. Ir al casino
                            4. Ir a la casilla de salida
                            5. Retroceder 3 casillas
                            6. Aumentar la suerte del casino
                             */
                            System.out.print("Superpoder:");
                            System.out.print(GestionPartida.superPoder);
                            System.out.print(" ");
                            System.out.print(" ");
                            
                            datosPartida.setVisible(false);
                            chat.setVisible(false);
                            superpoder.setVisible(true);

                            actualizarSuperpoder();
                            
                            try {
                                Superpoder.semaphoreSuper.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            superpoder.setVisible(false);


                            int i = Integer.parseInt(GestionPartida.superPoder);

                            switch (i) {
                                case 1:
                                    //Mover ficha??
                                    Integer jug = GestionPartida.indiceJugador;
                                    String posi = "Pos" + Superpoder.casillaS;
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

                                    break;
                                case 2:
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
                                    //SEMAFORO DE BANCO
                                    GestionPartida.enBanco=false;
                                    break;
                                case 3:
                                    datosPartida.setVisible(false);
                                    chat.setVisible(false);
                                    casino.setVisible(true);
        
                                    // semaforo para esperar a que se pulse algun boton del casino (apostar o retirarse)
                                    try {
                                        CasinoController.semaphoreCasino.acquire();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
        
                                    datosPartida.setVisible(true);
                                    chat.setVisible(false);
                                    casino.setVisible(false);
        
                                    GestionPartida.apostarDinero=false;
                                    break; 
                                case 4:
                                    //MOVER FICHA???
                                    break;
                                case 5:
                                    //MOVER FICHA??
                                    break;
                                case 6:
                                    
                                    break;
                                default:
                                    System.out.println("ERROR SUPERPODER");
                                    break;
                            }


                            GestionPartida.superPoder="0";
                        }
                    }

                } while(GestionPartida.dadosDobles);
                System.out.println("FIN TURNO");
                GestionPartida.finTurno();
                GestionPartida.miTurno = false;

            }
            ConexionServidor.esperar();
        }    
    }

    @FXML
    public void tirarDados(MouseEvent e) // HAY QUE COMPROBAR QUE SEA NUESTRO TURNO
    {   
        dado1.setDisable(true);
        dado2.setDisable(true);
        GestionPartida.CuentaInfoRecibida = 0;
        ImageView imagenDado = (ImageView) e.getSource();
        if (imagenDado.getId().equals("dado1") || imagenDado.getId().equals("dado2")) {
            GestionPartida.lanzarDados(GestionPartida.nombreUser,GestionPartida.IDPartida);

            ConexionServidor.esperar();
            semaphoreDados.release();

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
                        File file = new File("src/main/resources/Dice"+ GestionPartida.dados[1] + ".png");
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

            if(GestionPartida.enCarcel == false){    

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
            
        }
         
        //System.out.println("DadosDobles?");
        //System.out.println(GestionPartida.dadosDobles);
        //System.out.println(" ");
        //System.out.println(" ");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            banco = loadForm("Banco.fxml");
            vender = loadForm("VenderPropiedad.fxml");
            casino = loadForm("Casino.fxml");
            superpoder = loadForm("Superpoder.fxml");

            //HAY QUE AÑADIR AQUI EL VBOX COMPRA.CASINO Y BANCO

            containerForm.getChildren().addAll(banco, vender, casino,superpoder);
            datosPartida.setVisible(true);
            chat.setVisible(false);
            comprarPropiedad.setVisible(false);
            banco.setVisible(false);
            vender.setVisible(false);
            casino.setVisible(false);
            superpoder.setVisible(false);
            btnTerminarTurno.setVisible(false); // hasta que no sea mi turno no mostramos el boton


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
        }
        else if(evt.equals(btnTerminarTurno))
        {
            // mirar que hacer cuando le den al boton
        }
        else if (evt.equals(Qb)) {
            GestionPartida.enBanco=true;
            System.out.println("BANCo");
            System.out.println(GestionPartida.dineroEnBanco);
        }
        else if(evt.equals(Wb))
        {
            GestionPartida.apostarDinero=true;
            System.out.println("CASINo");
        }

    }
    
    private boolean estamosActualizando = false;

    private void actualizar(){
        if(!estamosActualizando){
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
                try {
                    Thread.sleep(1000); //TODAVIA NO FUNCIONA BIEN
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }*/
            }
            
            estamosActualizando = false;       
        }

    }

    public static void mostrarVentanaVenta(int num_propiedad)
    {
        vender.setVisible(true);
    }

    public static void ocultarVentanaVenta()
    {
        vender.setVisible(false);
    }

    private void actualizarSuperpoder(){
        Platform.runLater(() -> {
            VBox vbox = (VBox) superpoder.getChildren().get(2); //ESTO HAY QUE MIRAR QUE SEAN ESTOS

            Label lbl = (Label) vbox.getChildren().get(3); //ESTO HAY QUE MIRAR QUE SEAN ESTOS

            HBox hbox = (HBox) vbox.getChildren().get(0);

            //TextField text = (TextField) hbox.getChildren().get(0); // TextField textField = (TextField)

            int i = Integer.parseInt(GestionPartida.superPoder);

            switch (i) {
                case 1:
                    lbl.setText("Elija la casilla a la que quiere ir");
                    //Superpoder.txtCasilla.setVisible(true);
                    break;
                case 2:
                    lbl.setText("Acudes corriendo al banco");
                    break;
                case 3:
                    lbl.setText("Acudes corriendo al casino");
                    break; 
                case 4:
                    lbl.setText("Acudes corriendo a la casilla de salida");
                    break;
                case 5:
                    lbl.setText("Retrocedes 3 casillas");
                    break;
                case 6:
                    lbl.setText("Aumenta su suerte en el casino");
                    break;
                default:
                    System.out.println("ERROR SUPERPODER");
                    break;
            }
            
        });
    }

    private void actualizarDatosPartida(){
        Platform.runLater(() -> {
            lblEvento.setText(GestionPartida.evento);
            lblRonda.setText(Integer.toString(GestionPartida.ronda)); 
            lblEconomia.setText(Double.toString(GestionPartida.economia)); 
        });
    }

    private void actualizarEconomia(){
        //double valor = -0.5;
        barraEconomia.setStyle("-fx-accent: green; -fx-base: red;");

        double minValue = 0.5;
        double maxValue = 2.0;
        double progress = (GestionPartida.economia - minValue) / (maxValue - minValue); // Calcular el progreso en base al valor
        barraEconomia.setProgress(progress); // Actualizar el progreso de la barra
    
        double progressBarWidth = barraEconomia.getWidth();
        double circleRadius = indicador.getRadius();
        double circleCenterX = progressBarWidth * progress + circleRadius; // Calcular la posición X del círculo
        indicador.setCenterX(circleCenterX);

       
        //barraEconomia.setProgress(GestionPartida.economia);
    }
}