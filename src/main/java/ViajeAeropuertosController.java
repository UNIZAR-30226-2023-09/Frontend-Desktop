import java.io.IOException;
import java.util.concurrent.Semaphore;

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

    public static Semaphore semaphoreAeropuerto = new Semaphore(0);

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();
         
        // ocultar la ventana
        if (evt.equals(btnAceptar)){
            semaphoreAeropuerto.release();   
        }
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
