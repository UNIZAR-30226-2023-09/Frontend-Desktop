import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UnirsePartidaController implements Initializable{

    @FXML
    private Button btnUnirse, btnVolver;

    @FXML
    private Label lblNombre, lblGemas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(Sesion.nombre);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
    }
    
    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();

        if(btnUnirse.equals(evt))
        {
            // abrir el tablero
            App.setRoot("Tablero");
        }
        else if(btnVolver.equals(evt))
        {
            // volver al menu
            App.setRoot("MenuPrincipal");
        }
    }
}
