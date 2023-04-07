/*
 -----------------------------------------------------------------------
 * Fichero: SignInFormController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: 
 -----------------------------------------------------------------------
*/
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

public class ListaPropiedadesController implements Initializable {

    @FXML
    private Label lblJ1, lblJ2, lblJ3, lblJ4,
            lblD1, lblD2, lblD3, lblD4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

        lblJ1.setText(Sesion.nombre);
        // lblD1.setText(Integer.toString(GestionPartida.dinero));
    }

    public void agnadirPropiedad()
    {}
}
