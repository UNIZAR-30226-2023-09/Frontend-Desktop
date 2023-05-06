import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PujarController implements Initializable{
    @FXML
    private Button btnComprar, btnRechazar;

    @FXML
    private Label lblImg, lblError;

    @FXML
    private ImageView propiedadImg;

    private Boolean propiedadComprada = false;

    private Semaphore semaphoreComprar = new Semaphore(0);

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lblError.setVisible(false);    
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        propiedadComprada = false;

        if (btnComprar.equals(evt))
        {
            if(GestionPartida.dineroJugadores[GestionPartida.indiceJugador] >= GestionPartida.precio_subasta)
            {
                // enviamos el mensaje para comprar la subasta
                // System.out.println("Todavia no esta implementada la compra de una subasta");

                GestionPartida.ComprarSubasta();
    
                propiedadComprada = true;
                semaphoreComprar.release();
            }
            else
            {
                lblError.setVisible(true);
            }
        } else if (btnRechazar.equals(evt)) {
            GestionPartida.comprarPropiedad = false;
            semaphoreComprar.release();
        }
    }

    public boolean gestionarPujarPropiedad()
    {
        Platform.runLater(() -> {
            lblImg.setText("Desea comprar " + GestionPartida.tablero[Integer.parseInt(GestionPartida.propiedad_subasta)] + " por: "
            + GestionPartida.precio_subasta + "$");

            File fileCP = new File("src/main/resources/CARTAS_EDIFICIOS/" + GestionPartida.propiedad_subasta +".png");
            propiedadImg.setImage(new Image(fileCP.toURI().toString()));
        });

        try {
            semaphoreComprar.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return propiedadComprada;
    }
}
