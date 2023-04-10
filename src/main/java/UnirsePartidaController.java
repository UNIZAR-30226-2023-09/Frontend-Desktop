import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private Label lblNombre, lblGemas, lblError;

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
            // enviamos el codigo al servidor
            GestionPartida.unirsePartida(txtCodigo.toString());

            // esperamos la respuesta
            ConexionServidor.esperar();

            if (GestionPartida.enPartida) {
                // abrir el tablero
                App.setRoot("Tablero");
            } else {
                lblError.setText("El codigo introducido no es valido");
            }
        } else if (btnVolver.equals(evt)) {
            App.setRoot("MenuPrincipal");
        }
    }
}
