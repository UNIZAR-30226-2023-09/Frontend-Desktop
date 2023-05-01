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

    private int numPropiedad;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        Boolean vendida = false;
        if(evt.equals(btnVender))
        {
            // enviar el mensaje para vender la propiedad
            System.out.println("Propiedad que trato de vender " + tableroController.posicion_propiedad_tablero[numPropiedad]);
            // enviar el mensaje para vender la propiedad
            GestionPartida.venderPropiedad(tableroController.posicion_propiedad_tablero[numPropiedad]);
            vendida = true;
            //ConexionServidor.esperar(); // NO SE SI HACE FALTA
        }
        
        // por ultimo ocultamos la pantalla de vender
        tableroController.ocultarVentanaVenta(numPropiedad, vendida);
    }

    public void actualizarLabel(int numPropiedad)
    {
        Platform.runLater(() -> {
            lblImg.setText("Quieres vender " + GestionPartida.tablero[Integer.parseInt(tableroController.posicion_propiedad_tablero[numPropiedad])] + " por " + GestionPartida.precioVenta + " €");

            this.numPropiedad = numPropiedad;
        });
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
