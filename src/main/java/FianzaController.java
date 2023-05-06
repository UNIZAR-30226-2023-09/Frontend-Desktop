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

public class FianzaController implements Initializable{

    private TableroController tableroController;

    @FXML
    private Button btnComprar, btnRechazar;

    @FXML
    private Label lblImg, lblError;

    @FXML
    private ImageView propiedadImg;

    private Boolean fianzaPagada = false;

    private Semaphore semaphore = new Semaphore(0);

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lblError.setVisible(false);    
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        fianzaPagada = false;

        if (btnComprar.equals(evt))
        {
            if(GestionPartida.dineroJugadores[GestionPartida.indiceJugador] >= 50)
            {
                // enviamos el mensaje para comprar la subasta
                // System.out.println("Todavia no esta implementado el pago");

                GestionPartida.JugadorEnCarcel[GestionPartida.indiceJugador] = false;
                GestionPartida.turnosCarcel = 0;
    
                fianzaPagada = true;
                semaphore.release();
            }
            else
            {
                lblError.setVisible(true);
            }
        } else if (btnRechazar.equals(evt))
        {
            semaphore.release();
        }
    }

    public boolean pagar()
    {
        // solo si estoy en partida mostrare la pantalla
        if(GestionPartida.JugadorEnCarcel[GestionPartida.indiceJugador])
        {
            System.out.println("Ofrecemos la posinilidad de pagar la fianza");
            tableroController.mostrarVentanaFianza();

            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            tableroController.ocultarVentanaFianza();

            return fianzaPagada;
        }
        else
        {
            return false;
        }
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}

