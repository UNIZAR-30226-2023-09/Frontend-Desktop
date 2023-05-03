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
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaPropiedadesController implements Initializable {

    private TableroController tableroController;

    @FXML
    private VBox propiedades;

    @FXML
    private Label lblVacia;

    private int numPropiedades = 0;

    private final int NUM_PROPIEDADES = 27;

    private int[] casilla_propiedad;  //este vector guarda la relacion entre la casilla que estoy y la propiedad en la lista que es

    private String[] precio_edificar; // almacenamos el precio por el que se podra edificar en esa propiedad

    @FXML
    private Button  btnV1, btnV2, btnV3, btnV4, btnV5, btnV6, btnV7, btnV8, btnV9, btnV10, btnV11, btnV12, btnV13, btnV14, btnV15, btnV16, btnV17, btnV18, btnV19, btnV20, btnV21, btnV22, btnV23, btnV24, btnV25, btnV26, btnV27,
        btnE1, btnE2, btnE3, btnE4, btnE5, btnE6, btnE7, btnE8, btnE9, btnE10, btnE11, btnE12, btnE13, btnE14, btnE15, btnE16, btnE17, btnE18, btnE19, btnE20, btnE21, btnE22, btnE23,
        btnS1, btnS2, btnS3, btnS4, btnS5, btnS6, btnS7, btnS8, btnS9, btnS10, btnS11, btnS12, btnS13, btnS14, btnS15, btnS16, btnS17, btnS18, btnS19, btnS20, btnS21, btnS22, btnS23, btnS24, btnS25, btnS26, btnS27;

    private  ObservableList<Button> botonesV, botonesE, botonesS;

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
            
        // inicializamos el vector de lso precios de edificar cada propiedad
        precio_edificar = new String[] 
            {"0","0","1","2","0","0",
                "24","3","4","0","5","0","6","7","0","8",
                "25","9","0","10","11","0","12",
                "13","0","14","26","15","0","16",
                "17","0","18","19","0","20","27",
                "21","22","0","23"};

        // lista de botones de vender
        botonesV = FXCollections.observableArrayList();

        botonesV.add(btnV1); botonesV.add(btnV2); botonesV.add(btnV3); botonesV.add(btnV4); botonesV.add(btnV5);
        botonesV.add(btnV6); botonesV.add(btnV7); botonesV.add(btnV8); botonesV.add(btnV9); botonesV.add(btnV10);
        botonesV.add(btnV11); botonesV.add(btnV12); botonesV.add(btnV13); botonesV.add(btnV14); botonesV.add(btnV15);
        botonesV.add(btnV16); botonesV.add(btnV17); botonesV.add(btnV18); botonesV.add(btnV19); botonesV.add(btnV20);
        botonesV.add(btnV21); botonesV.add(btnV22); botonesV.add(btnV23); botonesV.add(btnV24); botonesV.add(btnV25);
        botonesV.add(btnV26); botonesV.add(btnV27);
            
        //lista de botones edificar
        botonesE = FXCollections.observableArrayList();

        botonesE.add(btnE1); botonesE.add(btnE2); botonesE.add(btnE3); botonesE.add(btnE4); botonesE.add(btnE5);
        botonesE.add(btnE6); botonesE.add(btnE7); botonesE.add(btnE8); botonesE.add(btnE9); botonesE.add(btnE10);
        botonesE.add(btnE11); botonesE.add(btnE12); botonesE.add(btnE13); botonesE.add(btnE14); botonesE.add(btnE15);
        botonesE.add(btnE16); botonesE.add(btnE17); botonesE.add(btnE18); botonesE.add(btnE19); botonesE.add(btnE20);
        botonesE.add(btnE21); botonesE.add(btnE22); botonesE.add(btnE23);

        //lista de botones subastar
        botonesS = FXCollections.observableArrayList();

        botonesS.add(btnS1); botonesS.add(btnS2); botonesS.add(btnS3); botonesS.add(btnS4); botonesS.add(btnS5);
        botonesS.add(btnS6); botonesS.add(btnS7); botonesS.add(btnS8); botonesS.add(btnS9); botonesS.add(btnS10);
        botonesS.add(btnS11); botonesS.add(btnS12); botonesS.add(btnS13); botonesS.add(btnS14); botonesS.add(btnS15);
        botonesS.add(btnS16); botonesS.add(btnS17); botonesS.add(btnS18); botonesS.add(btnS19); botonesS.add(btnS20);
        botonesS.add(btnS21); botonesS.add(btnS22); botonesS.add(btnS23); botonesS.add(btnS24); botonesS.add(btnS25);
        botonesS.add(btnS26); botonesS.add(btnS27);
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

            numPropiedades++;
        });
    }

    public void eliminarPropiedad(int casilla)
    {
        Platform.runLater(() -> {
            numPropiedades--;

            // ocultar la propiedad que ya no tenemos
            propiedades.getChildren().get(casilla).setVisible(false);
            propiedades.getChildren().get(casilla).setManaged(false);

            if(numPropiedades == 0)
            {
                lblVacia.setVisible(true);
                lblVacia.setManaged(true);
            }
        });
    }

    /*
     * Esta funcion permite mostrar/ocultar todos los botones de la lista de propiedades de vender.
     * Pero solo de aquella propiedad que se pueda ver en pantalla.
     */
    public void visibilidadBotonesVenta(Boolean b)
    {
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            HBox hbox = (HBox) propiedades.getChildren().get(i);

            // solo si la propiedad se puede ver mostraremos sus botones
            if(hbox.isVisible())
            {
               botonesV.get(i-1).setVisible(b);
            }

        }
    }

    /*
     * Esta funcion permite mostrar/ocultar todos los botones de la lista de propiedades de edificar.
     * Pero solo de aquella propiedad que se pueda ver en pantalla.
     */
    public void visibilidadBotonesEdificar(Boolean b)
    {
        if(b == true)
        {
            // mostramos los botones de edificar de aquellas propiedades que tengamos todo el pais
            GestionPartida.quieroEdificar();
            while (!GestionPartida.esperarListaEdificar) {
                ConexionServidor.esperar();
            }
            GestionPartida.esperarListaEdificar = false;

            for(int i=0; i < GestionPartida.nombresPropiedadesEdificar.size(); i++)
            {
                int numPropiedad = Integer.parseInt(GestionPartida.nombresPropiedadesEdificar.get(i));
                System.out.println("Trato de mostrar el boton de la propieda: " + numPropiedad + " que es " + casilla_propiedad[numPropiedad]);
                botonesE.get(casilla_propiedad[numPropiedad]-1).setVisible(true);
                precio_edificar[casilla_propiedad[numPropiedad]] = GestionPartida.preciosPropiedadesEdificar.get(i);
            }
        }
        else
        {
            // ocultamos los botones
            for(int i=1; i<=NUM_PROPIEDADES-4; i++)
            {
                HBox hbox = (HBox) propiedades.getChildren().get(i);
    
                // solo si la propiedad se puede ver mostraremos sus botones
                if(hbox.isVisible())
                {
                   botonesE.get(i-1).setVisible(false);
                }
    
            }
        }
    }

    /*
     * Esta funcion permite mostrar/ocultar todos los botones de la lista de propiedades de subastar.
     * Pero solo de aquella propiedad que se pueda ver en pantalla.
     */
    public void visibilidadBotonesSubastar(Boolean b)
    {
        // solo mostraremos los botones si no hay ninguna subasta activa
        if((b == true && !GestionPartida.subastaOcupada) || b == false)
        {
            for(int i=1; i<=NUM_PROPIEDADES; i++)
            {
                HBox hbox = (HBox) propiedades.getChildren().get(i);

                // solo si la propiedad se puede ver mostraremos sus botones
                if(hbox.isVisible())
                {
                botonesS.get(i-1).setVisible(b);
                }

            }
        }
    }

    public void ocultarBotonesPropiedadSubastada(int numPropiedad)
    {
        botonesV.get(numPropiedad-1).setVisible(false);
        botonesE.get(numPropiedad-1).setVisible(false);
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        // comprobamos si han pulsado algun boton de vender
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            if(evt.equals(botonesV.get(i-1)))
            {
                System.out.println("La propiedad pulsada es la posicion " + i);
                // abrir la pantalla que permite vender la propiedad
                tableroController.mostrarVentanaVenta(i);
            }
        }

        for(int i=1; i<=NUM_PROPIEDADES-4; i++)
        {
            if(evt.equals(botonesE.get(i-1)))
            {
                tableroController.mostrarVentanaEdificar(i, Integer.parseInt(precio_edificar[i]));
            }
        }

        // comprobamos si han pulsado algun boton de vender
        for(int i=1; i<=NUM_PROPIEDADES; i++)
        {
            if(evt.equals(botonesS.get(i-1)))
            {
                System.out.println("La propiedad pulsada es la posicion " + i);
                // abrir la pantalla que permite vender la propiedad
                tableroController.mostrarVentanaSubastar(i);
            }
        }
    }

    /*
     * Cuando se produzca un cambio de dispositivo llamaremos a esta funcion para que me vuelva a mostrar
     * todas las propiedades.
     */
    public void actualizarPropiedades()
    {
        // recorremos la lista de las propiedades del jugador y vamos aladiendolas
        ArrayList<GestionPartida.Propiedad> propiedades = GestionPartida.getPropiedades();

        for( int i=0; i<propiedades.size(); i++)
        {
            GestionPartida.Propiedad prop = propiedades.get(i);
            agnadirPropiedad(prop.id);
        }
    }

    public void setTableroController(TableroController tableroController)
    {
        this.tableroController = tableroController;
    }
}
