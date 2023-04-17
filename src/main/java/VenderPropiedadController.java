import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VenderPropiedadController {
    @FXML
    private Button btnCancelar, btnVender;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        if(evt.equals(btnCancelar))
        {
            // 
            TableroController.ocultarVentanaVenta();
        }
        else if(evt.equals(btnVender))
        {
            // enviar el mensaje para vender la propiedad
        }
    }
}
