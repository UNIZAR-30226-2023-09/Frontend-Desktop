import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.ImageView;

public class CasinoController implements Initializable{

    @FXML
    private TextField txtDinero;

    @FXML
    private Button btnApostar, btnRetirarse;

    @FXML
    private Label lblGanancias, lblError;

    @FXML
    private ImageView imgRule;

    public static Semaphore semaphoreCasino = new Semaphore(0);

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();
    
        // comprobar si ha decidido apostar o es un cagon
    
        if (evt.equals(btnApostar))
        {
            // mirar que haya una cantidad valida
            if (txtDinero.getText().equals(""))
            {
                // indicar que tiene que introducir una cantidad no nula
                lblError.setVisible(true);
                lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
            }
            else 
            {
                int apuesta = Integer.parseInt(txtDinero.getText());
                if (apuesta > GestionPartida.dineroJugadores[GestionPartida.indiceJugador])
                {
                    // indicar que no tienes suficiente saldo
                    lblError.setVisible(true);
                    lblError.setText("No tienes el suficiente saldo");
                } 
                else if (apuesta <= 0)
                {
                    // indicar que tiene que introducir una cantidad no nula
                    lblError.setVisible(true);
                    lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
                }
                else
                {
                    int dineroAntes = GestionPartida.dineroJugadores[GestionPartida.indiceJugador];
                    // enviar msj de mirar si gana
                    GestionPartida.apostarDinero(txtDinero.toString());
        
                    // Inicia la animación de rotación
                    RotateTransition rotacion = new RotateTransition(Duration.seconds(2), imgRule);
                    rotacion.setByAngle(360);
                    rotacion.play();
        
                    // Espera un tiempo para que la ruleta gire
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e2 -> {
                        rotacion.pause();
        
                        // Esperar respuesta
                        System.out.println("ESPERANDO RESPUESTA");
                        ConexionServidor.esperar(); //REVISAR SI ESTO TIENE QUE IR AQUI
                        System.out.println("RESPUESTA RECIBIDA");
                        lblGanancias.setVisible(true);
        
                        if (dineroAntes < GestionPartida.dineroJugadores[GestionPartida.indiceJugador]) {
                            // si ganamos mostramos el dinero obtenido
                            lblGanancias.setStyle("-fx-text-fill: green;");
                            lblGanancias.setText("+" + Integer.toString(apuesta*2) + "$");
                        } else {
                            // si perdemos msotramos el dinero que se ha restado
                            lblGanancias.setStyle("-fx-text-fill: red;");
                            lblGanancias.setText("-" + txtDinero.getText() + "$");
                        }

                        semaphoreCasino.release();                       
                    }));
                    timeline.play();
                }
            }
        }
        else if(evt.equals(btnRetirarse))
        {
            // simplemente liberamos el semaforo si el jugador no quiere echarle a la caprichosa
            semaphoreCasino.release();
        }
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Crea un TextFormatter para valores enteros y establece un Filter para aceptar solo valores numéricos
        TextFormatter<Integer> textFormatter = new TextFormatter<Integer>((Change c) -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            } else {
                return null;
            }
        });
        // Establece el TextFormatter en el TextField
        txtDinero.setTextFormatter(textFormatter);

        // ocultamos el mensaje de error y del dinero obtenido/perdido
        lblError.setVisible(false);
        lblGanancias.setVisible(false);
    }
    
}
