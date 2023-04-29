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

    private int orden_propiedad, num_propiedad;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        if(evt.equals(btnVender))
        {
            // enviar el mensaje para vender la propiedad
            System.out.println("Propiedad que trato de vender " + GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);
            // enviar el mensaje para vender la propiedad
            GestionPartida.venderPropiedad(GestionPartida.posicionesJugadores[GestionPartida.indiceJugador]);

            //ConexionServidor.esperar(); // NO SE SI HACE FALTA
        }

        // por ultimo ocultamos la pantalla de vender
        tableroController.ocultarVentanaVenta(num_propiedad);
    }

    public void actualizarLabel(int orden_compra_propiedad, int numPropiedad)
    {
        Platform.runLater(() -> {
            lblImg.setText("Quieres Vender la propiedad por " + GestionPartida.precioVenta + " €");

            orden_propiedad = orden_compra_propiedad;
            num_propiedad = numPropiedad;
        });
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
