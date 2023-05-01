import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EdificarController {

    private TableroController tableroController;
    
    @FXML
    private Label lblEdificar;

    @FXML
    private Button btnAceptar, btnCancelar;

    private int numPropiedad;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        Boolean edificado = false;
        if(evt.equals(btnAceptar))
        {
            // enviar el mensaje para vender la propiedad
            System.out.println("todavia no esta implementado el edificar");
        }
        
        // por ultimo ocultamos la pantalla de vender
        tableroController.ocultarVentanaEdificar(numPropiedad, edificado);
    }

    public void actualizarLabel(int numPropiedad, int precio)
    {
        Platform.runLater(() -> {
            lblEdificar.setText("Quieres edificar en " + GestionPartida.tablero[Integer.parseInt(tableroController.posicion_propiedad_tablero[numPropiedad])] + " por " + precio + " $");

            this.numPropiedad = numPropiedad;
        });
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
