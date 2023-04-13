import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;




public class TableroController implements Initializable {

    @FXML
    private ImageView dado1, dado2, user1, user2, user3, user4;

    @FXML
    public static VBox datosPartida;

    public static VBox listaJugadores, listaPropiedades, chat, propiedad;

    @FXML
    private Button btnChat;

    @FXML
    private StackPane containerForm;

    Random random = new Random();
    
    private static Semaphore semaphoreDados = new Semaphore(0); // Semaforo de concurrencia

    private Timeline timeline;

    private void partida(){
        ConexionServidor.esperar();  
        while(GestionPartida.enPartida){          
            //ConexionServidor.esperar();       //HABRA QUE PONERLO DONDE PEREZ
            dado1.setDisable(true);
            dado2.setDisable(true);
            if (GestionPartida.miTurno == true) {

                while (GestionPartida.CuentaInfoRecibida < 3) {
                    //System.out.println("cuentaInfoRecibida?2");
                    //System.out.println(GestionPartida.CuentaInfoRecibida);
                    ConexionServidor.esperar();
                }
                 
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
                    
                    //AQUI VAMOS A GESTIONAR EN QUE CASILLA HEMOS CAIDO PARA COMPRAR, BANCO Y CASINO
                    
                    if (!GestionPartida.enCarcel) {
                        if (GestionPartida.comprarPropiedad) {
                            //AQUI PONER QUE LA PANTALLA DE COMPRA SE INICIE
                            ComprarPropiedadController.gestionarCompraPropiedad();
                            
                            //SEMAFORO DE COMPRA
                            try {
                                ComprarPropiedadController.semaphoreComprar.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            /* 
                            //VUELTA A COMO ESTABAMOS
                            datosPartida.setVisible(true);
                            chat.setVisible(false);
                            propiedad.setVisible(false);
                            */
                        } else if (GestionPartida.apostarDinero) {
                            //AQUI PONER QUE LA PANTALLA DE CASINO
                            //SEMAFORO DE CASINO
                        } else if (GestionPartida.enBanco) {
                            //AQUI PONER QUE LA PANTALLA DE BANCO
                            //SEMAFORO DE BANCO
                        }
                    }

                } while(GestionPartida.dadosDobles);
                
                GestionPartida.finTurno(GestionPartida.client);
                GestionPartida.miTurno = false;

            }
            ConexionServidor.esperar();
        }    
    }


    @FXML
    public void comprarPropiedad(MouseEvent e){
        /* 
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
        */

        //MOSTRAR: QUE CARTA ES Y SU PRECIO
        //ESPERAR AL BOTON DE COMPRA O RECHAZO
        //DESBLOQUEAR SEMAFORO
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

            //System.out.println("dado1");
            //System.out.println(GestionPartida.dados[0]);

            //System.out.println("dado2");
            //System.out.println(GestionPartida.dados[1]);

            if(GestionPartida.enCarcel == false){    

                String posi = "Pos" + String.valueOf(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);
                Integer jug = GestionPartida.indiceJugador;

                //System.out.println("posi");
                //System.out.println(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);

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
        while (!GestionPartida.meToca) {
            ConexionServidor.esperar();
        }
        GestionPartida.meToca = false; 

        //System.out.println("DadosDobles?");
        //System.out.println(GestionPartida.dadosDobles);
        //System.out.println(" ");
        //System.out.println(" ");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            listaJugadores = loadForm("ListaJugadores.fxml");
            listaPropiedades = loadForm("ListaPropiedades.fxml");
            chat = loadForm("Chat.fxml");
            propiedad = loadForm("CompraPropiedad.fxml");

            datosPartida = new VBox();
            datosPartida.getChildren().addAll(listaJugadores, listaPropiedades);

            //HAY QUE AÑADIR AQUI EL VBOX COMPRA.CASINO Y BANCO

            containerForm.getChildren().addAll(datosPartida, chat, propiedad);
            datosPartida.setVisible(true);
            chat.setVisible(false);
            propiedad.setVisible(false);


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
            }
            try {
                Thread.sleep(1000); //TODAVIA NO FUNCIONA BIEN
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            estamosActualizando = false;       
        }

    }

}