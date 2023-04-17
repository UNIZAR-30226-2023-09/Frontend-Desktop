import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ClasificacionController implements Initializable{
    
    @FXML
    private Button btnEmpezar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // habra que comprobar que el boton de empezar solo me aparezca si soy el lider
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        if(evt.equals(btnEmpezar))
        {
            // empezar la siguiente partida
        }
    }
    
}
