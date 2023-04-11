import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ComprarPropiedadController {

    @FXML
    private Button btnComprar, btnRechazar;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnComprar.equals(evt)) {
            // cerrar la sesion
            App.setRoot("MainView");
        } else if (btnRechazar.equals(evt)) {
            // abrir la tienda
            App.setRoot("Tienda");
        }
    }
    
}
