/*
 -----------------------------------------------------------------------
 * Fichero: MenuPrincipalController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: Fichero controlador de la vista principal para hacer sign in o sign up
 -----------------------------------------------------------------------
*/

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuPrincipalController implements Initializable {
    
    @FXML
    private Button btnSignOut, btnUnirse, btnTienda, btnCrear;

    @FXML
    private Label lblNombre, lblGemas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnSignOut.equals(evt)) {
            // cerrar la sesion
            App.setRoot("MainView");
        } else if (btnCrear.equals(evt)) {
            // poner codigo de la partida
            GestionPartida.crearPartida();

            // esperamos respuesta
            ConexionServidor.esperar();

            if (GestionPartida.enPartida && GestionPartida.dueñoPartida) {

                System.out.println(GestionPartida.IDPartida);


                // crear una partida
                App.setRoot("CrearPartida");
            } else {
                System.out.println("No se ha podido crear la partida");
            }
        } else if (btnUnirse.equals(evt)) {
            // unirse a una partida
            // ------------ App.setRoot("UnirsePartida");
            App.setRoot("Superpoder");
        } else if (btnTienda.equals(evt)) {
            // abrir la tienda
            App.setRoot("Tienda");
        }
    }
}
