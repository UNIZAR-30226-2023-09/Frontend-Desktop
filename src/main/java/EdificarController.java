import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EdificarController {

    private TableroController tableroController;
    
    @FXML
    private Label lblEdificar;

    @FXML
    private Button btnAceptar, btnCancelar;

    private int numPropiedad, precio;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        Boolean edificado = false;
        if(evt.equals(btnAceptar))
        {
            // enviar el mensaje para vender la propiedad
            GestionPartida.edificarPropiedad(tableroController.posicion_propiedad_tablero[numPropiedad], Integer.toString(precio));
            edificado = true;

            while(!GestionPartida.edificarOK)
            {
                ConexionServidor.esperar();
            }
            GestionPartida.edificarOK = false;
        }
        
        // por ultimo ocultamos la pantalla de vender
        tableroController.ocultarVentanaEdificar(numPropiedad, edificado);
    }

    public void actualizarLabel(int numPropiedad, int precio)
    {
        Platform.runLater(() -> {
            lblEdificar.setText("Quieres edificar en " + GestionPartida.tablero[Integer.parseInt(tableroController.posicion_propiedad_tablero[numPropiedad])] + " por " + precio + " $");

            this.numPropiedad = numPropiedad;
            this.precio = precio;
        });
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
