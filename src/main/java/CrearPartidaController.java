import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.lang.model.util.ElementScanner14;
import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CrearPartidaController implements Initializable{

    @FXML
    private Button btnEmpezar, btnVolver;

    @FXML
    private Label lblNombre, lblGemas, lblCodigo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(Sesion.nombre);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
        lblCodigo.setText(GestionPartida.IDPartida);
    }
    
    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();

        if(btnEmpezar.equals(evt))
        {
            GestionPartida.empezarPartida(GestionPartida.IDPartida);

            ConexionServidor.esperar();

            if(GestionPartida.empezarPartida && GestionPartida.enPartida)
            {
                // abrir el tablero
                App.setRoot("Tablero");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No se ha podido iniciar la partida", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(btnVolver.equals(evt))
        {
            // volver al menu
            App.setRoot("MenuPrincipal");
        }
    }
      
}
