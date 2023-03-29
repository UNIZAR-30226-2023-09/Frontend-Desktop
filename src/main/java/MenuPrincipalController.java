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

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuPrincipalController {

    @FXML
    private Button btnSignOut, btnUnirse;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();

        if(btnSignOut.equals(evt))
        {
            // cerrar la sesion
            App.setRoot("MainView");
        }
        else if(btnUnirse.equals(evt))
        {
            // abrir el tablero
            App.setRoot("Tablero");
        }
    }
    
}

