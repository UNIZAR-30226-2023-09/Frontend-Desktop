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

    private final int NUM_PROPIEDADES = 26;

    private int[] casilla_propiedad;  //este vector guarda la relacion entre la casilla que estoy y la propiedad en la lista que es

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ocultamos todas las propiedades
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            propiedades.getChildren().get(i).setVisible(false);
        }

        //inicializamos el vector de la relacion casilla-propiedad
        casilla_propiedad = new int[] 
            {0,0,1,2,0,0,25,3,4,0,5,0,6,7,0,8,0,9,0,10,11,0,12,13,0,14,26,15,0,16,17,0,18,19,20,0,21,27,22,23,0,24};
    }

    public void agnadirPropiedad(int casilla)
    {
        if(numPropiedades == 0)
        {
            lblVacia.setVisible(false);
            lblVacia.setManaged(false);
        }

        // mostrar la nueva propiedad
        propiedades.getChildren().get(casilla_propiedad[casilla]).setVisible(true);

        numPropiedades++;
    }

    public void eliminarPropiedad(int casilla)
    {
        numPropiedades--;

        // ocultar la propiedad que ya no tenemos
        propiedades.getChildren().get(casilla_propiedad[casilla]).setVisible(false);

        if(numPropiedades == 0)
        {
            lblVacia.setVisible(true);
            lblVacia.setManaged(true);
        }
    }
}
