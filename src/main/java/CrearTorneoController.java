import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CrearTorneoController implements Initializable {

    @FXML
    private Button btnEmpezar;

    @FXML
    private Label lblNombre, lblGemas, lblCodigo, lblError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
        lblCodigo.setText(GestionPartida.IDTorneo);
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnEmpezar.equals(evt)) {
            GestionPartida.empezarPartidaTorneo(GestionPartida.IDTorneo);

            ConexionServidor.esperar();

            if (GestionPartida.enPartida) {
                // abrir el tablero
                App.setRoot("Tablero");
            } else {
                lblError.setText("No se ha podido iniciar el torneo");
            }
        }
    }
}