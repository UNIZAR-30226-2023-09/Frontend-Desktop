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
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

public class ListaPropiedadesController implements Initializable {

    @FXML
    private VBox propiedades;

    @FXML
    private Label lblVacia;

    private int numPropiedades = 0;

    private final int NUM_PROPIEDADES = 2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            propiedades.getChildren().get(i).setVisible(false);
        }
    }

    public void agnadirPropiedad()
    {
        if(numPropiedades == 0)
        {
            lblVacia.setVisible(false);
            lblVacia.setManaged(false);
        }

        // mostrar la nueva propiedad

        numPropiedades++;
    }

    public void eliminarPropiedad()
    {
        numPropiedades--;

        // ocultar la propiedad que ya no tenemos

        if(numPropiedades == 0)
        {
            lblVacia.setVisible(true);
            lblVacia.setManaged(true);
        }
    }
}
