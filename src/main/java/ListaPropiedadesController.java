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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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

    @FXML
    private Button  btnV1, btnV2, btnV3, btnV4, btnV5, btnV6, btnV7, btnV8, btnV9, btnV10, btnV11, btnV12, btnV13, btnV14, btnV15, btnV16, btnV17, btnV18, btnV19, btnV20, btnV21, btnV22, btnV23, btnV24, btnV25, btnV26;

    private  ObservableList<Button> vboxList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ocultamos todas las propiedades
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            propiedades.getChildren().get(i).setVisible(false);
            propiedades.getChildren().get(i).setManaged(false);
        }

        //inicializamos el vector de la relacion casilla-propiedad
        casilla_propiedad = new int[] 
            {0,0,1,2,0,0,25,3,4,0,5,0,6,7,0,8,0,9,0,10,11,0,12,13,0,14,26,15,0,16,17,0,18,19,20,0,21,27,22,23,0,24};

        // lista de botones de vender
        vboxList = FXCollections.observableArrayList();

        vboxList.add(btnV1); vboxList.add(btnV2); vboxList.add(btnV3); vboxList.add(btnV4); vboxList.add(btnV5);
        vboxList.add(btnV6); vboxList.add(btnV7); vboxList.add(btnV8); vboxList.add(btnV9); vboxList.add(btnV10);
        vboxList.add(btnV11); vboxList.add(btnV12); vboxList.add(btnV13); vboxList.add(btnV14); vboxList.add(btnV15);
        vboxList.add(btnV16); vboxList.add(btnV17); vboxList.add(btnV18); vboxList.add(btnV19); vboxList.add(btnV20);
        vboxList.add(btnV21); vboxList.add(btnV22); vboxList.add(btnV23); vboxList.add(btnV24); vboxList.add(btnV25);
        vboxList.add(btnV26);

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
        propiedades.getChildren().get(casilla_propiedad[casilla]).setManaged(true);

        numPropiedades++;
    }

    public void eliminarPropiedad(int casilla)
    {
        numPropiedades--;

        // ocultar la propiedad que ya no tenemos
        propiedades.getChildren().get(casilla_propiedad[casilla]).setVisible(false);
        propiedades.getChildren().get(casilla_propiedad[casilla]).setManaged(false);

        if(numPropiedades == 0)
        {
            lblVacia.setVisible(true);
            lblVacia.setManaged(true);
        }
    }

    /*
     * Esta funcion permite ocultar todos los botones de la lista de propiedades.
     */
    public void ocultarBotones()
    {
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            HBox hbox = (HBox) propiedades.getChildren().get(i);

            hbox.getChildren().get(2).setVisible(false);

            hbox.getChildren().get(3).setVisible(false);

        }
    }

    /*
     * Esta funcion permite mostrar todos los botones de la lista de propiedades.
     * Pero solo de aquella propiedad que se pueda ver en pantalla
     */
    public void mostrarBotones()
    {
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            HBox hbox = (HBox) propiedades.getChildren().get(i);

            // solo si la propiedad se puede ver mostraremos sus botones
            if(hbox.isVisible())
            {
                hbox.getChildren().get(2).setVisible(true);
                hbox.getChildren().get(3).setVisible(true);
            }

        }
    }
}
