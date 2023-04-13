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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ListaJugadoresController implements Initializable {

    @FXML
    public Label lblJ1, lblJ2, lblJ3, lblJ4;
    @FXML
    public Label lblD1;
    @FXML
    public Label lblD2;
    @FXML
    public Label lblD3;
    @FXML
    public Label lblD4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lblJ1.setText(GestionPartida.ordenJugadores[0]);
        lblD1.setText(Integer.toString(GestionPartida.dineroJugadores[0]));

        lblJ2.setText(GestionPartida.ordenJugadores[1]);
        lblD2.setText(Integer.toString(GestionPartida.dineroJugadores[1]));

        lblJ3.setText(GestionPartida.ordenJugadores[2]);
        lblD3.setText(Integer.toString(GestionPartida.dineroJugadores[2]));

        lblJ4.setText(GestionPartida.ordenJugadores[3]);
        lblD4.setText(Integer.toString(GestionPartida.dineroJugadores[3]));
    }

    /* 
    public static void actualizarInfo(int numJugador, int dinero) {
        switch (numJugador) {
            case 0:
                //lblJ1.setText(Sesion.nombre);
                lblD1.setText(Integer.toString(dinero));
            break;

            case 1:
                //lblJ2.setText(Sesion.nombre);
                lblD2.setText(Integer.toString(dinero));
                break;

            case 2:
                //lblJ3.setText(Sesion.nombre);
                lblD3.setText(Integer.toString(dinero));
                break;

            case 3:
                //lblJ4.setText(Sesion.nombre);
                lblD4.setText(Integer.toString(dinero));
                break;

            default:
                System.out.println("No existen tantos jugadores");
                break;
        }
    }
    */

}
