import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ComprarPropiedadController implements Initializable{

    @FXML
    private Button btnComprar, btnRechazar;

    @FXML
    private static Label textImg;

    @FXML
    private static ImageView propiedadImg;

    public static Semaphore semaphoreComprar = new Semaphore(0);

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnComprar.equals(evt)) {
            
        } else if (btnRechazar.equals(evt)) {
            
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }

    public static void gestionarCompraPropiedad(){
        TableroController.datosPartida.setVisible(false);
        TableroController.chat.setVisible(false);
        TableroController.propiedad.setVisible(true);

        textImg.setText("Desea comprar " + GestionPartida.tablero[Integer.parseInt(GestionPartida.propiedadAComprar)] + " por: "
        + GestionPartida.precioPropiedadAComprar + "€");

        File fileCP = new File("src/main/resources/chicago.png");
        propiedadImg.setImage(new Image(fileCP.toURI().toString()));

        /*
         * System.out.println("Introduzca un 1 si desea comprar la propiedad: "
                + tablero[Integer.parseInt(propiedadAComprar)] + "(" + String.valueOf(propiedadAComprar) + ")"
                + " por " + precioPropiedadAComprar + "€?");
         */
    }
    
}
