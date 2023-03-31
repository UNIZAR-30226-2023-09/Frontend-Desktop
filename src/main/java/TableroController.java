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
    private ImageView dado1, dado2, user1;

    private VBox listaJugadores, listaPropiedades, chat; 

    Random random = new Random();

    @FXML
    public void tirarDados(MouseEvent e) //HAY QUE COMPROBAR QUE SEA NUESTRO TURNO 
    {
        ImageView imagenDado = (ImageView) e.getSource();

        if(DatosPartida.esMiTurnoDados =true){
            DatosPartida.esMiTurnoDados =false;
            if(imagenDado.getId().equals("dado1") || imagenDado.getId().equals("dado2")) {
                //GestionPartida.lanzarDados(GestionPartida.nombreUser, GestionPartida.IDPartida);

                //ConexionServidor.esperar();

                Thread threadL = new Thread(){
                    public void run(){
                        System.out.println("Dado 1 agitandose");
                        try {
                            for (int i = 0; i < 15; i++) {
                                File file = new File("src/main/resources/Dice" + (random.nextInt(6)+1)+".png");
                                dado1.setImage(new Image(file.toURI().toString()));
                                Thread.sleep(50);
                            }
                            //File file = new File("src/main/resources/Dice" + GestionPartida.dados[0] + ".png");
                            //dado1.setImage(new Image(file.toURI().toString()));
                            System.out.println("Dado 1 ");
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
                                File file = new File("src/main/resources/Dice" + (random.nextInt(6)+1)+".png");
                                dado2.setImage(new Image(file.toURI().toString()));
                                Thread.sleep(50);
                            }
                            //File file = new File("src/main/resources/Dice"+ GestionPartida.dados[1] + ".png");
                            //dado2.setImage(new Image(file.toURI().toString()));
                            System.out.println("Dado 2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
        
                threadL.start();
                threadR.start();

                /*if(GestionPartida.dados[0] == GestionPartida.dados[1]){
                    DatosPartida.esMiTurnoDados =true;
                    DatosPartida.vecesLanzadoDados++;
                }
                else{
                    DatosPartida.vecesLanzadoDados=0;
                }

                if(DatosPartida.vecesLanzadoDados >= 2){
                    //TOCA IR A LA CAAAARCEL
                    //DatosPartida.estoyCarcel=true;
                    //habra que mirar algo con la logica de moro para esto, o mandar un mensaje o que contabilicen en el send las veces que llevamos seguidas lanzando

                }
                */
            }

            /*
            if(DatosPartida.estoyCarcel = false){ 
                
                String posi = "Pos"+String.valueOf(GestionPartida.casilla);
                String coordenadas = DatosPartida.mapaPropiedades.get(posi);
                System.out.println(coordenadas);

                String[] partes = coordenadas.split(",");
                int x = Integer.parseInt(partes[0]);
                int y = Integer.parseInt(partes[1]);
                
                user1.setLayoutX(x);
                user1.setLayoutY(y);

                //hacer desaparecer la ficha, habra que hacer una pausa para que terminen primero los dados

                //hacerla aparecer en la casilla que es

            }
            
             */
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