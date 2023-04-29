import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VenderPropiedadController {

    private TableroController tableroController;

    @FXML
    private Button btnCancelar, btnVender;

    @FXML
    private Label lblImg;

    private int num_propiedad;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        if(evt.equals(btnVender))
        {
            // enviar el mensaje para vender la propiedad
            System.out.println("Propiedad que trato de vender " + GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);
            // enviar el mensaje para vender la propiedad
            GestionPartida.venderPropiedad(tableroController.posicion_propiedad_tablero[num_propiedad]);

            //ConexionServidor.esperar(); // NO SE SI HACE FALTA
        }

        // por ultimo ocultamos la pantalla de vender
        tableroController.ocultarVentanaVenta(num_propiedad);
    }

    public void actualizarLabel(int numPropiedad)
    {
        Platform.runLater(() -> {
            lblImg.setText("Quieres Vender la propiedad por " + GestionPartida.precioVenta + " €");

            num_propiedad = numPropiedad;
        });
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
