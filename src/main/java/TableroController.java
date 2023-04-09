import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class TableroController implements Initializable {

    @FXML
    private ImageView dado1, dado2, user1, user2, user3, user4;

    @FXML
    private VBox datosPartida;

    private VBox listaJugadores, listaPropiedades/*, chat*/;

    Random random = new Random();

    @FXML
    public void tirarDados(MouseEvent e) // HAY QUE COMPROBAR QUE SEA NUESTRO TURNO
    {   
        ImageView imagenDado = (ImageView) e.getSource();

        if (DatosPartida.esMiTurnoDados = true) {
            DatosPartida.esMiTurnoDados = false;
            if (imagenDado.getId().equals("dado1") || imagenDado.getId().equals("dado2")) {
                GestionPartida.lanzarDados(GestionPartida.nombreUser,
                GestionPartida.IDPartida);

                ConexionServidor.esperar();

                Thread threadL = new Thread() {
                    public void run() {
                        System.out.println("Dado 1 agitandose");
                        try {
                            for (int i = 0; i < 15; i++) {
                                File file = new File("src/main/resources/Dice" + (random.nextInt(6) + 1) + ".png");
                                dado1.setImage(new Image(file.toURI().toString()));
                                Thread.sleep(50);
                            }
                            File file = new File("src/main/resources/Dice" + GestionPartida.dados[0] + ".png");
                            dado1.setImage(new Image(file.toURI().toString()));
                            System.out.println("Dado 1 ");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Thread threadR = new Thread() {
                    public void run() {
                        System.out.println("TDado 1 agitandose");
                        try {
                            for (int i = 0; i < 15; i++) {
                                File file = new File("src/main/resources/Dice" + (random.nextInt(6) + 1) + ".png");
                                dado2.setImage(new Image(file.toURI().toString()));
                                Thread.sleep(50);
                            }
                            File file = new File("src/main/resources/Dice"+ GestionPartida.dados[1] + ".png");
                            dado2.setImage(new Image(file.toURI().toString()));
                            System.out.println("Dado 2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                threadL.start();
                threadR.start();

                /*
                 * if(GestionPartida.dados[0] == GestionPartida.dados[1]){
                 * DatosPartida.esMiTurnoDados =true;
                 * DatosPartida.vecesLanzadoDados++;
                 * }
                 * else{
                 * DatosPartida.vecesLanzadoDados=0;
                 * }
                 * 
                 * if(DatosPartida.vecesLanzadoDados >= 2){
                 * //TOCA IR A LA CAAAARCEL
                 * //DatosPartida.estoyCarcel=true;
                 * //habra que mirar algo con la logica de moro para esto, o mandar un mensaje o
                 * que contabilicen en el send las veces que llevamos seguidas lanzando
                 * 
                 * }
                 */
            }

            
            if(DatosPartida.estoyCarcel = false){
                
                String posi = "Pos"+String.valueOf(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);
                Integer jug = GestionPartida.indiceJugador;
                String coordenadas;
                switch (jug) {
                        case 1:
                            coordenadas = DatosPartida.mapaPropiedades1.get(posi);
                            break;
                        case 2:
                            coordenadas = DatosPartida.mapaPropiedades2.get(posi);
                            break;
                        case 3:
                            coordenadas = DatosPartida.mapaPropiedades3.get(posi);
                            break;
                        case 4:
                            coordenadas = DatosPartida.mapaPropiedades4.get(posi);
                            break;
                        default:
                            coordenadas = "ERROR";
                            System.out.println("ERROR CASILLA");
                            break;
                }

                System.out.println(coordenadas);
                
                String[] partes = coordenadas.split(",");
                int x = Integer.parseInt(partes[0]);
                int y = Integer.parseInt(partes[1]);
                

                switch (jug) {
                    case 1:
                        user1.setLayoutX(x);
                        user1.setLayoutY(y);
                    case 2:
                        user2.setLayoutX(x);
                        user2.setLayoutY(y);
                    case 3:
                        user3.setLayoutX(x);
                        user3.setLayoutY(y);
                    case 4:
                        user4.setLayoutX(x);
                        user4.setLayoutY(y);  
                }
                
                
            }
             
            
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            listaJugadores = loadForm("ListaJugadores.fxml");
            listaPropiedades = loadForm("ListaPropiedades.fxml");
            // chat = loadForm("SignUpForm.fxml");
            datosPartida.getChildren().addAll(listaJugadores, listaPropiedades);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VBox loadForm(String ur1) throws IOException {
        return (VBox) FXMLLoader.load(getClass().getResource(ur1));
    }

}
