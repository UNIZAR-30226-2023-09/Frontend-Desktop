/*
 -----------------------------------------------------------------------
 * Fichero: MainViewController.java
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{

    @FXML
    private Button btnSignIn, btnSignUp;

    @FXML
    private StackPane containerForm;

    private VBox signInForm, signUpForm; 

    @FXML
    public void actionEvent(ActionEvent e)
    {
        Object evt = e.getSource();
        
        if(evt.equals(btnSignIn))
        {
            signInForm.setVisible(true);
            signUpForm.setVisible(false);
        }
        else if(evt.equals(btnSignUp))
        {
            signUpForm.setVisible(true);
            signInForm.setVisible(false);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        try {
            signInForm = loadForm("SignInForm.fxml");
            signUpForm = loadForm("SignUpForm.fxml");
            containerForm.getChildren().addAll(signInForm,signUpForm);
            //containerForm.getChildren().addAll(signUpForm);
            signInForm.setVisible(true);
            signUpForm.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
            //Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private VBox loadForm(String ur1) throws IOException
    {
        return (VBox) FXMLLoader.load(getClass().getResource(ur1));
    }
    
}
