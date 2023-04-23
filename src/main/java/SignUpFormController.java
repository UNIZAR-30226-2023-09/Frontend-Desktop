
/*
 -----------------------------------------------------------------------
 * Fichero: SignUpFormController.java
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpFormController implements Initializable {

    public static boolean cuentaRegistrada = false;

    @FXML
    private TextField txtNameSignUp, txtEmailSignUp, txtUserSignUp;

    @FXML
    private PasswordField txtPassword, txtConfirmPassword;

    @FXML
    private Button btnSignUp;

    @FXML
    private Label lblError;

    private boolean verificarTipoEmail(String email) {
        return email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnSignUp.equals(evt)) {

            if (!txtNameSignUp.getText().isEmpty() && !txtEmailSignUp.getText().isEmpty()
                    && !txtUserSignUp.getText().isEmpty() && !txtPassword.getText().isEmpty()
                    && !txtConfirmPassword.getText().isEmpty()) {
                if (txtConfirmPassword.getText().equals(txtPassword.getText())) {

                    if (verificarTipoEmail(txtEmailSignUp.getText())) {
                        // enviamos el mensaje
                        GestionPartida.registrarse(txtEmailSignUp.getText(), txtPassword.getText(),
                                txtNameSignUp.getText());

                        // recibir respuesta
                        ConexionServidor.esperar();

                        if (cuentaRegistrada) {
                            // iniciamos sesion tras estar registrados para que el servidor cuente con nuestra informacion
                            GestionPartida.iniciarSesion(txtEmailSignUp.getText(), txtPassword.getText());
                            ConexionServidor.esperar();
                            // Ir al menu principal
                            App.setRoot("MenuPrincipal");
                        } else {
                            lblError.setText("Datos introducidos no validos");
                        }
                    } else {
                        lblError.setText("Introduzca un correo valido");
                    }
                } else {
                    lblError.setText("Las contraseñas no coinciden");
                }

            } else {
                // o el campo nombre de usuario o la contraseña estan vacios
                lblError.setText("Debe rellernar todos los campos");
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub

    }
}
