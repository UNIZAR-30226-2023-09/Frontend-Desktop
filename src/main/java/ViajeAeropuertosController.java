import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class ViajeAeropuertosController {

    private TableroController tableroController;
    
    @FXML
    private Button btnAceptar;

    @FXML
    private ImageView imgViaje;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();
         
        // ocultar la ventana
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
