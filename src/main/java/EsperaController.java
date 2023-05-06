import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class EsperaController implements Initializable
{
    @FXML
    private Label lblNombre, lblGemas;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));

        Thread thread = new Thread() {
            public void run()
            {
                try {
                    esperar();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private void esperar() throws IOException
    {
        ConexionServidor.esperar();
        App.setRoot("Tablero");
    }
}
