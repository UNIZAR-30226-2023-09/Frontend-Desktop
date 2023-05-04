import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    private int numPropiedad;

    public Boolean habiamosSubastado = false;

    public int propiedadSubastada = 0;

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
                // System.out.println("Todavia no esta implementado el mensaje de efectuar la subasta");
                GestionPartida.subastarPropiedad(tableroController.posicion_propiedad_tablero[numPropiedad],dinero);

                System.out.println("Subastamos la propiedad por " + dinero);

                habiamosSubastado = true;
                propiedadSubastada = numPropiedad;

                tableroController.ocultarVentanaSubastar(true,numPropiedad);
            }
            else
            {
                lblError.setVisible(true);
            }
        }
        else if(evt.equals(btnCancelar))
        {
            tableroController.ocultarVentanaSubastar(false,0);
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

    public void actualizarLabel(int numPropiedad)
    {
        Platform.runLater(() -> {
            lblSubastar.setText("Por cuanto quieres subastar " + GestionPartida.tablero[Integer.parseInt(tableroController.posicion_propiedad_tablero[numPropiedad])]);

            this.numPropiedad = numPropiedad;
        });
    }

    public Boolean subastaExitosa()
    {
        ArrayList<GestionPartida.Propiedad> propiedades = GestionPartida.getPropiedades();

        for( int i=0; i<propiedades.size(); i++)
        {
            GestionPartida.Propiedad prop = propiedades.get(i);
            if (prop.id == propiedadSubastada)
            {
                return false;
            }
        }
        return true;
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
