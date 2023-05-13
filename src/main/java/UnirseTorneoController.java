import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UnirseTorneoController implements Initializable {

    @FXML
    private Button btnUnirse;

    @FXML
    private Label lblNombre, lblGemas, lblError, lblTitulo;

    @FXML
    private TextField txtCodigo;

    private Semaphore semaphore = new Semaphore(0);

    private Boolean unirse = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));
        lblError.setVisible(false);

        Thread thread = new Thread() {
            public void run() {
                esperarComienzo();
            }
        };

        thread.start();
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnUnirse.equals(evt)) {
            System.out.println("EL codigo introducido por el jugador es: " + txtCodigo.getText());
            // enviamos el codigo al servidor
            GestionPartida.unirseTorneo(txtCodigo.getText());

            // esperamos la respuesta
            ConexionServidor.esperar();

            if (GestionPartida.enTorneo) {
                System.out.println("Esperando al anfitrion");
                /*
                 * Thread thread = new Thread() {
                 * public void run()
                 * {
                 */
                modificarVentana();
                // }
                // };

                // thread.start();

                // modificarVentana();

                // espero a que me llegue el empezar ok
                /*
                 * while(!GestionPartida.empezarPartida)
                 * {
                 * ConexionServidor.esperar();
                 * }
                 */

                // abrir el tablero
                // App.setRoot("Tablero");
                // App.setRoot("Espera");
                unirse = true;
            } else {
                lblError.setVisible(true);
                lblError.setText("El codigo introducido no es valido");
                unirse = false;
            }
        }

        semaphore.release();
    }

    private void modificarVentana() {
        Platform.runLater(() -> {
            txtCodigo.setVisible(false);
            lblError.setVisible(false);
            btnUnirse.setVisible(false);
            lblTitulo.setText("Esperando al anfitrion");
            System.out.println("los deberia haber cambiado ya");
        });
    }

    @FXML
    private void esperarComienzo() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (unirse) {
            while (!GestionPartida.empezarPartida) {
                ConexionServidor.esperar();
            }
            Platform.runLater(() -> {
                try {
                    App.setRoot("Tablero");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
    }
}
