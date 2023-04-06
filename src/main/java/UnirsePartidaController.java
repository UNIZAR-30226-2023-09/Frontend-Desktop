import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UnirsePartidaController implements Initializable {

    @FXML
    private Button btnUnirse, btnVolver;

    @FXML
    private Label lblNombre, lblGemas;

    @FXML
    private TextField txtCodigo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(Sesion.nombre);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnUnirse.equals(evt)) {
            // abrir el tablero
            App.setRoot("Tablero");
        } else if (btnVolver.equals(evt)) {
            if (btnUnirse.equals(evt)) {
                // enviamos el codigo al servidor
                GestionPartida.unirsePartida(txtCodigo.toString());

                // esperamos la respuesta
                ConexionServidor.esperar();

                if (GestionPartida.enPartida) {
                    // abrir el tablero
                    App.setRoot("Tablero");
                } else {
                    JOptionPane.showMessageDialog(null, "EL codigo introducido no es valido", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (btnVolver.equals(evt)) {
                // volver al menu
                App.setRoot("MenuPrincipal");
            }
        }
    }
}
