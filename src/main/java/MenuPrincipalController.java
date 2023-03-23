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
    private Button btnSignOut;

    @FXML
    public void actionEvent(ActionEvent e)
    {
        Object evt = e.getSource();

        if(btnSignOut.equals(evt))
        {
            // cerrar la sesion
            // Ir al menu principal
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));

                Parent root = loader.load();
    
                Scene scene = new Scene(root);
                Stage stage = (Stage) btnSignOut.getScene().getWindow();

                stage.setScene(scene);
                stage.show();

                Stage old = (Stage) btnSignOut.getScene().getWindow();
                old.close();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.err.println(String.format("Error creando ventana: %s", e1.getMessage()));
            }
        }
    }
    
}

