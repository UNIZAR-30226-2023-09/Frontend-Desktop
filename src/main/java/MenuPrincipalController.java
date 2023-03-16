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
    private Button btnSignIn;

    @FXML
    public void actionEvent(ActionEvent e)
    {
        Object evt = e.getSource();

        if(btnSignIn.equals(evt))
        {
            // Muestra los datos que contiene la clase sesion
            JOptionPane.showMessageDialog(null, Sesion.nombre, "Bienvenido, eres:", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
}

