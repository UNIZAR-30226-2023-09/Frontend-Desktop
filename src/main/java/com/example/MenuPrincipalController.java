/*
 -----------------------------------------------------------------------
 * Fichero: MenuPrincipalController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: Fichero controlador de la vista principal para hacer sign in o sign up
 -----------------------------------------------------------------------
*/


package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/MainView.fxml"));
    
                Parent root = loader.load();
    
                //MainViewController controlador = loader.getController();
    
                Scene scene = new Scene(root);
                //Stage stage = new Stage();
                Stage stage = (Stage) btnSignIn.getScene().getWindow();
    
                stage.setScene(scene);
                stage.show();

                //Stage old = (Stage) btnSignIn.getScene().getWindow();
                //old.close();

                //stage.close();
    
                //stage.setOnCloseRequest(e -> controlador.closeWindow());

                //Stage myStage = (Stage) this.btnSignIn.getScene().getWindow();
                //myStage.close();
    
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    
}

