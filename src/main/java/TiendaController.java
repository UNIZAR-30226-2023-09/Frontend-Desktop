import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TiendaController implements Initializable {

    @FXML
    private Button btnSkin1, btnSkin2, btnSkin3, btnSkin4, btnVolver;

    @FXML
    private Label lblNombre, lblGemas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(Sesion.nombre);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
    }

    private void comprarSkin(String skinName, int precio) {
        // comprobar que tengamos las suficientes gemas
        if (GestionPartida.gemas >= precio) {
            // restamos las gemas al usuario QUIEN HACE ESTO???? NOSOTROS O SSE ENCARGAN LOS
            // DE LA BASE

            // indicamos que todo ha salido bien
            JOptionPane.showMessageDialog(null, "Compra realizada con exito", "OK", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No tienes suficientes gemas", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnSkin1.equals(evt)) {
            comprarSkin("Skin1", 30);
        } else if (btnSkin2.equals(evt)) {
            comprarSkin("Skin2", 30);
        } else if (btnSkin3.equals(evt)) {
            comprarSkin("Skin3", 30);
        } else if (btnSkin4.equals(evt)) {
            comprarSkin("Skin4", 30);
        } else if (btnVolver.equals(evt)) {
            App.setRoot("MenuPrincipal");
        }
    }
}
