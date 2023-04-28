import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
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
    private Label lblImg;

    @FXML
    public static ImageView propiedadImg;

    private Boolean propiedadComprada = false;

    private Semaphore semaphoreComprar = new Semaphore(0);

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        propiedadComprada = false;

        if (btnComprar.equals(evt)) {
             
            GestionPartida.comprarPropiedad(GestionPartida.propiedadAComprar);
            while (!GestionPartida.compraRealizada) {
                ConexionServidor.esperar();
            }
            GestionPartida.compraRealizada = false;

            propiedadComprada = true;
            
        } else if (btnRechazar.equals(evt)) {
            GestionPartida.comprarPropiedad = false;
        }
        semaphoreComprar.release();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public boolean gestionarComprarPropiedad()
    {
        Platform.runLater(() -> {
            lblImg.setText("Desea comprar " + GestionPartida.tablero[Integer.parseInt(GestionPartida.propiedadAComprar)] + " por: "
            + GestionPartida.precioPropiedadAComprar + "€");
        });

        try {
            semaphoreComprar.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return propiedadComprada;
    }
    
}
