/*
 -----------------------------------------------------------------------
 * Fichero: SignInFormController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: 
 -----------------------------------------------------------------------
*/
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    @FXML
    public ImageView imgJ1, imgJ2, imgJ3, imgJ4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lblJ1.setText(GestionPartida.ordenJugadores[0]);
        lblD1.setText(Integer.toString(GestionPartida.dineroJugadores[0]));

        File file1 = new File("src/main/resources/skins/" + GestionPartida.skinsJugadores[0] +".png");
        imgJ1.setImage(new Image(file1.toURI().toString()));

        lblJ2.setText(GestionPartida.ordenJugadores[1]);
        lblD2.setText(Integer.toString(GestionPartida.dineroJugadores[1]));

        File file2 = new File("src/main/resources/skins/" + GestionPartida.skinsJugadores[1] +".png");
        imgJ2.setImage(new Image(file2.toURI().toString()));

        lblJ3.setText(GestionPartida.ordenJugadores[2]);
        lblD3.setText(Integer.toString(GestionPartida.dineroJugadores[2]));

        File file3 = new File("src/main/resources/skins/" + GestionPartida.skinsJugadores[2] +".png");
        imgJ3.setImage(new Image(file3.toURI().toString()));

        lblJ4.setText(GestionPartida.ordenJugadores[3]);
        lblD4.setText(Integer.toString(GestionPartida.dineroJugadores[3]));

        File file4 = new File("src/main/resources/skins/" + GestionPartida.skinsJugadores[3] +".png");
        imgJ4.setImage(new Image(file4.toURI().toString()));
    }

    public void actualizarDinero() {
        Platform.runLater(() -> {
            lblD1.setText(Integer.toString(GestionPartida.dineroJugadores[0]));
            lblD2.setText(Integer.toString(GestionPartida.dineroJugadores[1]));
            lblD3.setText(Integer.toString(GestionPartida.dineroJugadores[2]));
            lblD4.setText(Integer.toString(GestionPartida.dineroJugadores[3]));
        });
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
