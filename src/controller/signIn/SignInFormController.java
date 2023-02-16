/*
 -----------------------------------------------------------------------
 * Fichero: SignInFormController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: Fichero controlador de la vista del formulario para hacer sign in
 -----------------------------------------------------------------------
*/

package controller.signIn;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SignInFormController implements Initializable{

    @FXML
    private TextField txtUserSignIn, txtPasswordSignInMask;

    @FXML
    private PasswordField txtPasswordSignIn;

    @FXML
    private CheckBox checkViewPassSignIn;

    @FXML
    private Button btnSignIn;
    
    @FXML
    public void eventKey(KeyEvent e)
    {
        String c = e.getCharacter();

        if(c.equalsIgnoreCase(" ")) 
        {
            e.consume();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maskPassword(txtPasswordSignIn, txtPasswordSignInMask, checkViewPassSignIn);
        /*
        txtUserSignIn.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(event.getCharacter().equals(" ")) event.consume();
            }
            
        });
        */
    }

    public void maskPassword(PasswordField pass, TextField text, CheckBox check)
    {
        text.setVisible(false);
        text.setManaged(false);

        text.managedProperty().bind(check.selectedProperty());
        text.visibleProperty().bind(check.selectedProperty());

        text.textProperty().bindBidirectional(pass.textProperty());
    }
    
}
