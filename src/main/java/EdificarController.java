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

            // faltara mostrar graficamente el edificio
            System.out.println("Falta mostrar en el tablero la casa"); 

            /*
             * 
             * 
             * 
             * 
             */
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
