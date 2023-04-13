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
    private Label lblJ1, lblJ2, lblJ3, lblJ4,
            lblD1, lblD2, lblD3, lblD4;

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

    public void actualizarInfo(int numJugador, String nombre, int dinero) {
        switch (numJugador) {
            case 1:
                lblJ1.setText(Sesion.nombre);
                lblD1.setText(Integer.toString(GestionPartida.dineroJugadores[0]));
            break;

            case 2:
                lblJ2.setText(Sesion.nombre);
                lblD2.setText(Integer.toString(GestionPartida.dineroJugadores[1]));
                break;

            case 3:
                lblJ3.setText(Sesion.nombre);
                lblD3.setText(Integer.toString(GestionPartida.dineroJugadores[2]));
                break;

            case 4:
                lblJ4.setText(Sesion.nombre);
                lblD4.setText(Integer.toString(GestionPartida.dineroJugadores[3]));
                break;

            default:
                System.out.println("No existen tantos jugadores");
                break;
        }
    }

}
