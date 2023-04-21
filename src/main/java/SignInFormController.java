/*
 -----------------------------------------------------------------------
 * Fichero: SignInFormController.java
 * Autor: Marcos Pérez, Alejandro Sanz
 * NIP: 820532, 816104
 * Descripción: Fichero controlador de la vista del formulario para hacer sign in
 -----------------------------------------------------------------------
*/

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInFormController implements Initializable {

    @FXML
    private TextField txtUserSignIn, txtPasswordSignInMask;

    @FXML
    private PasswordField txtPasswordSignIn;

    @FXML
    private CheckBox checkViewPassSignIn;

    @FXML
    private Button btnSignIn;

    @FXML
    private Label lblError;

    private boolean verificarTipoEmail(String email) {
        return email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnSignIn.equals(evt)) {
            if (!txtUserSignIn.getText().isEmpty() && !txtPasswordSignIn.getText().isEmpty()) {
                if (verificarTipoEmail(txtUserSignIn.getText())) {
                    // enviamos un mensaje al servidor con los datos necesarios para iniciar sesion
                    GestionPartida.iniciarSesion(txtUserSignIn.getText(), txtPasswordSignIn.getText());

                    // recibir respuesta
                    ConexionServidor.esperar();

                    if (GestionPartida.sesionIniciada) {
                        // Ir al menu principal
                        App.setRoot("MenuPrincipal");
                    } else {
                        lblError.setText("Datos introducidos no validos");
                    }
                } else {
                    lblError.setText("Introduzca un correo valido");
                }
            } else {
                // o el campo nombre de usuario o la contraseña estan vacios
                lblError.setText("Debe rellernar todos los campos");            
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maskPassword(txtPasswordSignIn, txtPasswordSignInMask, checkViewPassSignIn);
    }

    public void maskPassword(PasswordField pass, TextField text, CheckBox check) {
        text.setVisible(false);
        text.setManaged(false);

        text.managedProperty().bind(check.selectedProperty());
        text.visibleProperty().bind(check.selectedProperty());

        text.textProperty().bindBidirectional(pass.textProperty());
    }

}
