/*
 -----------------------------------------------------------------------
 * Fichero: ListaPropiedadesController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: 
 -----------------------------------------------------------------------
*/
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

public class ListaPropiedadesController implements Initializable {

    private TableroController tableroController;

    @FXML
    private VBox propiedades;

    @FXML
    private Label lblVacia;

    private int numPropiedades = 0;

    private final int NUM_PROPIEDADES = 27;

    private int[] casilla_propiedad;  //este vector guarda la relacion entre la casilla que estoy y la propiedad en la lista que es

    private int[] orden_compra = new int[NUM_PROPIEDADES]; //este vector sirve para ir almacenando en que orden han sido compradas las propiedades (-1 es que no esta comprada)

    @FXML
    private Button  btnV1, btnV2, btnV3, btnV4, btnV5, btnV6, btnV7, btnV8, btnV9, btnV10, btnV11, btnV12, btnV13, btnV14, btnV15, btnV16, btnV17, btnV18, btnV19, btnV20, btnV21, btnV22, btnV23, btnV24, btnV25, btnV26, btnV27;

    private  ObservableList<Button> botonesV;

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
            {0,0,1,2,0,0,
                24,3,4,0,5,0,6,7,0,8,
                25,9,0,10,11,0,12,
                13,0,14,26,15,0,16,
                17,0,18,19,0,20,27,
                21,22,0,23};

        // lista de botones de vender
        botonesV = FXCollections.observableArrayList();

        botonesV.add(btnV1); botonesV.add(btnV2); botonesV.add(btnV3); botonesV.add(btnV4); botonesV.add(btnV5);
        botonesV.add(btnV6); botonesV.add(btnV7); botonesV.add(btnV8); botonesV.add(btnV9); botonesV.add(btnV10);
        botonesV.add(btnV11); botonesV.add(btnV12); botonesV.add(btnV13); botonesV.add(btnV14); botonesV.add(btnV15);
        botonesV.add(btnV16); botonesV.add(btnV17); botonesV.add(btnV18); botonesV.add(btnV19); botonesV.add(btnV20);
        botonesV.add(btnV21); botonesV.add(btnV22); botonesV.add(btnV23); botonesV.add(btnV24); botonesV.add(btnV25);
        botonesV.add(btnV26); botonesV.add(btnV27);

        // inicializar vector del orden de compra
        for(int i=0; i<NUM_PROPIEDADES; i++)
        {
            orden_compra[i] = -1;
        }

    }

    public void agnadirPropiedad(int casilla)
    {
        if(numPropiedades == 0)
        {
            Platform.runLater(() -> {
                lblVacia.setVisible(false);
                lblVacia.setManaged(false);
            });
        }
        Platform.runLater(() -> {
            // mostrar la nueva propiedad
            propiedades.getChildren().get(casilla_propiedad[casilla]).setVisible(true);
            propiedades.getChildren().get(casilla_propiedad[casilla]).setManaged(true);

            orden_compra[casilla_propiedad[casilla]-1] = numPropiedades;

            numPropiedades++;
        });
    }

    public void eliminarPropiedad(int casilla)
    {
        Platform.runLater(() -> {
            numPropiedades--;

            // ocultar la propiedad que ya no tenemos
            propiedades.getChildren().get(casilla_propiedad[casilla]).setVisible(false);
            propiedades.getChildren().get(casilla_propiedad[casilla]).setManaged(false);

            if(numPropiedades == 0)
            {
                lblVacia.setVisible(true);
                lblVacia.setManaged(true);
            }

            orden_compra[casilla_propiedad[casilla]-1] = -1;
        });
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

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            if(evt.equals(botonesV.get(i-1)))
            {
                System.out.println("El pulsado es " +  Integer.toString(i));

                // abrir la pantalla que permite vender la propiedad
                tableroController.mostrarVentanaVenta(orden_compra[i-1], i);
            }
        }
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
