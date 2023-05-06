import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UnirsePartidaController implements Initializable {

    @FXML
    private Button btnUnirse;

    @FXML
    private Label lblNombre, lblGemas, lblError, lblTitulo;

    @FXML
    private TextField txtCodigo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
        lblError.setVisible(false);
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnUnirse.equals(evt)) {
            System.out.println("EL codigo introducido por el jugador es: " + txtCodigo.getText());
            // enviamos el codigo al servidor
            GestionPartida.unirsePartida(txtCodigo.getText());

            // esperamos la respuesta
            ConexionServidor.esperar();

            if (GestionPartida.enPartida)
            {
                System.out.println("Esperando al anfitrion");
                txtCodigo.setVisible(false);
                lblError.setVisible(false);
                btnUnirse.setVisible(false);
                Platform.runLater(() -> {
                    lblTitulo.setText("Esperando al anfitrion");
                });

                // espero a que me llegue el empezar ok
                ConexionServidor.esperar();
                

                // abrir el tablero
                App.setRoot("Tablero");
            } else {
                lblError.setVisible(true);
                lblError.setText("El codigo introducido no es valido");
            }
        }
    }
}
