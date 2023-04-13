import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ComprarPropiedadController implements Initializable{

    @FXML
    private Button btnComprar, btnRechazar;

    @FXML
    public static Label textImg;

    @FXML
    public static ImageView propiedadImg;

    public static Semaphore semaphoreComprar = new Semaphore(0);

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnComprar.equals(evt)) {
             
            GestionPartida.comprarPropiedad(GestionPartida.propiedadAComprar);
            while (!GestionPartida.compraRealizada) {
                ConexionServidor.esperar();
            }
            GestionPartida.compraRealizada = false;
            
        } else if (btnRechazar.equals(evt)) {
            GestionPartida.comprarPropiedad = false;
        }
        semaphoreComprar.release();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public static void gestionarCompraPropiedad(){
        TableroController.datosPartida.setVisible(false);
        TableroController.chat.setVisible(false);
        TableroController.propiedad.setVisible(true);

        //system.out de las variables para ver si estan
        System.out.println("text:");
        System.out.println(GestionPartida.tablero[Integer.parseInt(GestionPartida.propiedadAComprar)]);

        System.out.println("precio:");
        System.out.println(GestionPartida.precioPropiedadAComprar);

        //textImg.setText("Desea comprar " + GestionPartida.tablero[Integer.parseInt(GestionPartida.propiedadAComprar)] + " por: "
        //+ GestionPartida.precioPropiedadAComprar + "€");

        //File fileCP = new File("src/main/resources/chicago.png");
        //propiedadImg.setImage(new Image(fileCP.toURI().toString()));

        
    }
    
}
