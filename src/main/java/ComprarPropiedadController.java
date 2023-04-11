import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ComprarPropiedadController implements Initializable{

    @FXML
    private Button btnComprar, btnRechazar;

    public static Semaphore semaphoreComprar = new Semaphore(0);

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

    public static void gestionarCompraPropiedad(){
        TableroController.datosPartida.setVisible(false);
        TableroController.chat.setVisible(false);
        TableroController.propiedad.setVisible(true);
    }
    
}
