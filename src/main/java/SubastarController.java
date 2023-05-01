import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;;

public class SubastarController implements Initializable{

    private TableroController tableroController;
    
    @FXML
    private Label lblSubastar;

    @FXML
    private Button btnAceptar, btnCancelar;

    @FXML
    private TextField txtDinero;

    @FXML
    private Label lblError;

    private int numPropiedad, precio;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();
    
        if (evt.equals(btnAceptar))
        {
            String dinero = txtDinero.getText();
            if(!dinero.isEmpty())
            {
                // rellenar informacion necesaria
                System.out.println("Todavia no esta implementado el mensaje de efectuar la subasta");
                GestionPartida.subastarPropiedad(tableroController.posicion_propiedad_tablero[numPropiedad],Integer.toString(precio));

                tableroController.ocultarVentanaSubastar();
            }
            else
            {
                lblError.setVisible(true);
            }
        }
        else if(evt.equals(btnCancelar))
        {
            tableroController.ocultarVentanaSubastar();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Crea un TextFormatter para valores enteros y establece un Filter para aceptar solo valores numéricos
        TextFormatter<Integer> textFormatter = new TextFormatter<Integer>((Change c) -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            } else {
                return null;
            }
        });
        // Establece el TextFormatter en el TextField
        txtDinero.setTextFormatter(textFormatter);

        lblError.setVisible(false);
    }

    public void actualizarLabel(int numPropiedad, int precio)
    {
        Platform.runLater(() -> {
            lblSubastar.setText("Por cuanto quieres subastar " + GestionPartida.tablero[Integer.parseInt(tableroController.posicion_propiedad_tablero[numPropiedad])]);

            this.numPropiedad = numPropiedad;
            this.precio = precio;
        });
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
