/*
 -----------------------------------------------------------------------
 * Fichero: SignInFormController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: Fichero controlador de la vista del formulario para hacer sign in
 -----------------------------------------------------------------------
*/

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
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

    @FXML
    public void actionEvent(ActionEvent e)
    {
        Object evt = e.getSource();

        if(btnSignIn.equals(evt))
        {
            if(!txtUserSignIn.getText().isEmpty() && !txtPasswordSignIn.getText().isEmpty())
            {
                String infoSignIn = txtUserSignIn.getText() + ";" + txtPasswordSignIn.getText();

                /* Aqui iria todo lo de enviar la informacion por el canal */
                //de momento simplemete lo mostramos por pantalla
                JOptionPane.showMessageDialog(null, infoSignIn, "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                
                //de momento nos conectamos aqui, luego ya venemos si el connect en el main o donde
                // WebSocketClient client = new WebSocketClient();
                // client.connect("ws://localhost:8080/endpoint");
                // client.sendMessage(infoSignIn);
            }
            else
            {
                //o el campo nombre de usuario o la contraseña estan vacios
                JOptionPane.showMessageDialog(null, "Debe llenar los campos obligatorios", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
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
