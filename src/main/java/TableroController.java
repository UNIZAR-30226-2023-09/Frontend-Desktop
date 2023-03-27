import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class TableroController implements Initializable{
    
    @FXML
    private ImageView dado1, dado2;

    private VBox listaJugadores, listaPropiedades, chat; 

    Random random = new Random();

    @FXML
    public void tirarDados(MouseEvent e) //HAY QUE COMPROBAR QUE SEA NUESTRO TURNO 
    {
        ImageView imagenDado = (ImageView) e.getSource();

        if(DatosPartida.esMiTurnoDados =true){
            DatosPartida.esMiTurnoDados =false;
            if(imagenDado.getId().equals("dado1") || imagenDado.getId().equals("dado2")) {
                //PASAR CON WEB SOCKETS QUE QUEREMOS LANZAR DADOS, funcion que tiene que hacer moro?
                GestionPartida.lanzarDados(DatosPartida.nombreUser, DatosPartida.IDPartida);

                ConexionServidor.esperar();

                //tengo ya el valor en DatosPartida.dados[]
                Thread threadL = new Thread(){
                    public void run(){
                        System.out.println("Dado 1 agitandose");
                        try {
                            for (int i = 0; i < 15; i++) {
                                File file = new File("rc/main/resources/Dice" + (random.nextInt(6)+1)+".png");
                                dado1.setImage(new Image(file.toURI().toString()));
                                Thread.sleep(50);
                            }
                            File file = new File("rc/main/resources/Dice" + DatosPartida.dados[0]+".png");
                            dado1.setImage(new Image(file.toURI().toString()));
                            
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
        
                Thread threadR = new Thread(){
                    public void run(){
                        System.out.println("TDado 1 agitandose");
                        try {
                            for (int i = 0; i < 15; i++) {
                                File file = new File("rc/main/resources/Dice" + (random.nextInt(6)+1)+".png");
                                dado2.setImage(new Image(file.toURI().toString()));
                                Thread.sleep(50);
                            }
                            File file = new File("rc/main/resources/Dice" + DatosPartida.dados[1]+".png");
                            dado2.setImage(new Image(file.toURI().toString()));
                            
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
        
                threadL.start();
                threadR.start();


                //
                /* 
                switch (DatosPartida.dados[0]) {
                    case 1:
                        File file01 = new File("src/main/resources/Dice1.png");
                        dado1.setImage(new Image(file01.toURI().toString()));
                        break;
                    case 2:
                        File file02 = new File("src/main/resources/Dice1.png");
                        dado1.setImage(new Image(file02.toURI().toString()));
                        break;
                    case 3:
                        File file03 = new File("src/main/resources/Dice1.png");
                        dado1.setImage(new Image(file03.toURI().toString()));
                        break;
                    case 4:
                        File file04 = new File("src/main/resources/Dice1.png");
                        dado1.setImage(new Image(file04.toURI().toString()));
                        break;
                    case 5:
                        File file05 = new File("src/main/resources/Dice1.png");
                        dado1.setImage(new Image(file05.toURI().toString()));
                        break;
                    case 6:
                        File file06 = new File("src/main/resources/Dice1.png");
                        dado1.setImage(new Image(file06.toURI().toString()));
                        break;
                    default:
                        break;
                }

                switch (DatosPartida.dados[1]) {
                    case 1:
                        File file01 = new File("src/main/resources/Dice1.png");
                        dado2.setImage(new Image(file01.toURI().toString()));
                        break;
                    case 2:
                        File file02 = new File("src/main/resources/Dice1.png");
                        dado2.setImage(new Image(file02.toURI().toString()));
                        break;
                    case 3:
                        File file03 = new File("src/main/resources/Dice1.png");
                        dado2.setImage(new Image(file03.toURI().toString()));
                        break;
                    case 4:
                        File file04 = new File("src/main/resources/Dice1.png");
                        dado2.setImage(new Image(file04.toURI().toString()));
                        break;
                    case 5:
                        File file05 = new File("src/main/resources/Dice1.png");
                        dado2.setImage(new Image(file05.toURI().toString()));
                        break;
                    case 6:
                        File file06 = new File("src/main/resources/Dice1.png");
                        dado2.setImage(new Image(file06.toURI().toString()));
                        break;
                    default:
                        break;

                }
                */

                if(DatosPartida.dados[0] == DatosPartida.dados[1]){
                    DatosPartida.esMiTurnoDados =true;
                    DatosPartida.vecesLanzadoDados++;
                }
                else{
                    DatosPartida.vecesLanzadoDados=0;
                }

                if(DatosPartida.vecesLanzadoDados >= 2){
                    //TOCA IR A LA CAAAARCEL
                }
            }
        }
           
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    
        //listaJugadores = loadForm("SignInForm.fxml");
        //listaPropiedades = loadForm("SignUpForm.fxml");
        //chat = loadForm("SignUpForm.fxml");
          

       
    }

    
}
