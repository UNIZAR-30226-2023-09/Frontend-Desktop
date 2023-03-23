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

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignUpFormController implements Initializable{

    public static boolean cuentaRegistrada = false;
    
    @FXML
    private TextField txtNameSignUp, txtEmailSignUp, txtUserSignUp;

    @FXML
    private PasswordField txtPassword, txtConfirmPassword;

    @FXML
    private Button btnSignUp;

    private boolean verificarTipoEmail(String email)
    {
        return email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {    
        Object evt = e.getSource();

        if(btnSignUp.equals(evt)){

            if(!txtNameSignUp.getText().isEmpty() && !txtEmailSignUp.getText().isEmpty() && !txtUserSignUp.getText().isEmpty() && !txtPassword.getText().isEmpty()&& !txtConfirmPassword.getText().isEmpty())
            {
                if(txtConfirmPassword.getText().equals(txtPassword.getText())){

                    if(verificarTipoEmail(txtEmailSignUp.getText()))
                    {
                        //enviamos el mensaje
                        GestionPartida.registrarse(txtEmailSignUp.getText(), txtPassword.getText(), txtNameSignUp.getText());

                        //recibir respuesta
                        ConexionServidor.esperar();

                        if(cuentaRegistrada)
                        {
                            // Relenar los datos de la sesion
                            Sesion.nombre = txtUserSignUp.getText();
                            Sesion.gemas = 0;

                            // Ir al menu principal
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            
                                Parent root = loader.load();
                    
                                Scene scene = new Scene(root);
                                Stage stage = (Stage) btnSignUp.getScene().getWindow();
            
                                stage.setScene(scene);
                                stage.show();
            
                                Stage old = (Stage) btnSignUp.getScene().getWindow();
                                old.close();
            
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                System.err.println(String.format("Error creando ventana: %s", e1.getMessage()));
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Datos introducidos no validos", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "En correo debe introducir un email valido", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
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

