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

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignInFormController implements Initializable{

    // Variables control estado
    public static boolean sesionIniciada = false;


    @FXML
    private TextField txtUserSignIn, txtPasswordSignInMask;

    @FXML
    private PasswordField txtPasswordSignIn;

    @FXML
    private CheckBox checkViewPassSignIn;

    @FXML
    private Button btnSignIn;

    private boolean verificarTipoEmail(String email)
    {
        return email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }
    
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
                if(verificarTipoEmail(txtUserSignIn.getText()))
                {
                    //enviamos un mensaje al servidor con los datos necesarios para iniciar sesion
                    GestionPartida.iniciarSesion(txtUserSignIn.getText(), txtPasswordSignIn.getText());

                    //recibir respuesta
                    ConexionServidor.esperar();

                    if (sesionIniciada) {
                        // Relenar los datos de la sesion
                        Sesion.nombre = txtUserSignIn.getText();
                        Sesion.gemas = 0;

                        // Ir al menu principal
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
        
                            Parent root = loader.load();
                
                            Scene scene = new Scene(root);
                            Stage stage = (Stage) btnSignIn.getScene().getWindow();
        
                            stage.setScene(scene);
                            stage.show();
        
                            Stage old = (Stage) btnSignIn.getScene().getWindow();
                            old.close();
        
                            /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/MenuPrincipal.fxml"));
                            Parent root = loader.load();
        
                            MenuPrincipalController segundoControlador = loader.getController();
                            segundoControlador.setSesion(sesion);
        
                            Scene scene = new Scene(root);
                            Stage stage = (Stage) btnSignIn.getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
        
                            Stage old = (Stage) btnSignIn.getScene().getWindow();
                            old.close();*/
        
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            System.err.println(String.format("Error creando ventana: %s", e1.getMessage()));
                        }
                    }
                    else
                    {
                        // Mostrar mensaje de error
                        JOptionPane.showMessageDialog(null, "Datos introducidos no validos", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "En correo debe introducir un email valido", "ERROR", JOptionPane.ERROR_MESSAGE);
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
