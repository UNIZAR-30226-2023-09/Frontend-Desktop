import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClasificacionController implements Initializable {

    @FXML
    private Button btnEmpezar, btnSalir;

    @FXML
    private Label lblEsperar,
            lblP1, lblP2, lblP3, lblP4,
            lblJ1, lblJ2, lblJ3, lblJ4;

    @FXML
    private ImageView img1, img2, img3, img4;

    private ObservableList<Label> lblPuntos, lblJugadores;

    private ObservableList<ImageView> imgJugadores;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEmpezar.setVisible(false);
        btnSalir.setVisible(false);
        lblEsperar.setVisible(false);

        lblPuntos = FXCollections.observableArrayList();
        lblPuntos.add(lblP1);
        lblPuntos.add(lblP2);
        lblPuntos.add(lblP3);
        lblPuntos.add(lblP4);

        lblJugadores = FXCollections.observableArrayList();
        lblJugadores.add(lblJ1);
        lblJugadores.add(lblJ2);
        lblJugadores.add(lblJ3);
        lblJugadores.add(lblJ4);

        imgJugadores = FXCollections.observableArrayList();
        imgJugadores.add(img1);
        imgJugadores.add(img2);
        imgJugadores.add(img3);
        imgJugadores.add(img4);

        // quitamos los valores por defecto
        for(int i=0; i<4; i++)
        {
            lblJugadores.get(i).setText(GestionPartida.ordenJugadores[i]);
            lblPuntos.get(i).setText(Integer.toString(GestionPartida.clasificacionTorneo[i]));
            File file = new File("src/main/resources/skins/" + GestionPartida.skinsJugadores[i] + ".png");
            imgJugadores.get(i).setImage(new Image(file.toURI().toString()));
        }

        // esperamos a que terminen todos
        Thread thread = new Thread() {
            public void run() {
                try {
                    mostrarClasificacion();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();
        if (evt.equals(btnEmpezar)) {
            // empezar la siguiente partida
            GestionPartida.empezarPartidaTorneo(GestionPartida.IDTorneo);

            ConexionServidor.esperar();

            App.setRoot("Tablero");
        } else if (evt.equals(btnSalir)) {
            // volvemos al menu principal
            App.setRoot("MenuPrincipal");
        }
    }

    private void mostrarClasificacion() throws IOException {
        while (!GestionPartida.resultadosTorneo) {
            ConexionServidor.esperar();
        }
        GestionPartida.resultadosTorneo = false;

        // actualizar los resultados del torneo
        Platform.runLater(() -> {
            for (int i = 0; i < 4; i++) {

                lblJugadores.get(i).setText(GestionPartida.ordenJugadores[i]);
                lblPuntos.get(i).setText(Integer.toString(GestionPartida.clasificacionTorneo[i]));
                File file = new File("src/main/resources/skins/" + GestionPartida.skinsJugadores[i] + ".png");
                imgJugadores.get(i).setImage(new Image(file.toURI().toString()));
            }
        });

        if (GestionPartida.torneoFinalizado) {
            // mostrar el boton de salir de la partida y quitar el de empezar
            btnSalir.setVisible(true);
            GestionPartida.torneoFinalizado = false;
        } else {
            // mostrar el boton de empezar (solo si es mi partida)
            if (GestionPartida.dueñoPartida) {
                btnEmpezar.setVisible(true);
            } else {
                // todo lo relacionado con no ser el dueño
                lblEsperar.setVisible(true);

                while (!GestionPartida.enPartida) {
                    ConexionServidor.esperar();
                }

                App.setRoot("Tablero");
            }
        }
    }
}
