import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UnirsePartidaController {

    @FXML
    private Button btnUnirse, btnCancelar;
    
    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();

        if(btnUnirse.equals(evt))
        {
            // abrir el tablero
            App.setRoot("Tablero");
        }
        else if(btnCancelar.equals(evt))
        {
            // volver al menu
            App.setRoot("MenuPrincipal");
        }
    }
}
