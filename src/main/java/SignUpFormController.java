/*
 -----------------------------------------------------------------------
 * Fichero: SignUpFormController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: Fichero controlador de la vista del formulario para hacer sign in
 -----------------------------------------------------------------------
*/

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SignUpFormController implements Initializable{
    
    @FXML
    private TextField txtNameSignUp, txtEmailSignUp, txtUserSignUp;

    @FXML
    private PasswordField txtPassword, txtConfirmPassword;

    @FXML
    private Button btnSignUp; 

    @FXML
    public void keyEvent(KeyEvent e){
        String c = e.getCharacter();

        if(c.equalsIgnoreCase(" ")){
            e.consume();
        }
    }

    @FXML
    public void actionEvent(ActionEvent e){
        
        Object evt = e.getSource();

        if(btnSignUp.equals(evt)){

            if(!txtNameSignUp.getText().isEmpty() && !txtEmailSignUp.getText().isEmpty() && !txtUserSignUp.getText().isEmpty() && !txtPassword.getText().isEmpty()&& !txtConfirmPassword.getText().isEmpty())
            {
                if(txtConfirmPassword.getText().equals(txtPassword.getText())){
                    
                    String infoSignIn = txtEmailSignUp.getText(); //anañdir cosas a enviar

                    // Aqui iria todo lo de enviar la informacion por el canal
                    //de momento simplemete lo mostramos por pantalla
                    JOptionPane.showMessageDialog(null, infoSignIn, "Bienvenido", JOptionPane.INFORMATION_MESSAGE);

                }
                else{
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                    
            }
            else
            {
                //o el campo nombre de usuario o la contraseña estan vacios
                JOptionPane.showMessageDialog(null, "Debe llenar los campos obligatorios", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
}

