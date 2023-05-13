import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FinPartidaController implements Initializable
{
    @FXML
    private Label lblNombre, lblGemas, lblResultado;

    @FXML
    private Button btnVolver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));

        if(GestionPartida.enTorneo)
        {
            btnVolver.setText("Clasificacion");
        }

        if(GestionPartida.ganador == true)
        {
            lblResultado.setText("Has ganado");
        }
        else{
            lblResultado.setText("Has perdido");
        }
        // indicar si he ganado o perdido
        //System.out.println("Todavia no esta implementado indicar quien gana o pierde");
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        if(GestionPartida.enTorneo)
        {
            App.setRoot("Clasificacion");
        }
        else
        {
            // si no estamos en un torneo salimos directamente
            App.setRoot("MenuPrincipal");
        }
    }
    
}
