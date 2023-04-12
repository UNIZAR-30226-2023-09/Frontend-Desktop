import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.lang.model.util.ElementScanner14;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class CasinoController implements Initializable{

    @FXML
    private TextField txtDinero;

    @FXML
    private Button btnApostar, btnRetirarse;

    @FXML
    private Label lblGanancias, lblError;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        // comprobar si ha decidido apostar o es un cagon

        if(evt.equals(btnApostar))
        {
            // mirar que haya una cantidad valida

            if(Integer.parseInt(txtDinero.toString()) > GestionPartida.dineroEnBanco)
            {
                // indicar que no tienes suficiente saldo
                lblError.setVisible(true);
                lblError.setText("No tienes el suficiente saldo");
            }
            else if(Integer.parseInt(txtDinero.toString()) <= 0)
            {
                // indicar que tiene que introducir una cantidad no nula
                lblError.setVisible(true);
                lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
            }
            else 
            {
                int dineroAntes = GestionPartida.dineroEnBanco;
                // enviar msj de mirar si gana

                GestionPartida.apostarDinero(GestionPartida.client, txtDinero.toString());

                // esperar respuesta
                ConexionServidor.esperar();

                if(dineroAntes < GestionPartida.dineroEnBanco)
                {
                    // si ganamos mostramos el dinero obtenido
                    lblGanancias.setStyle("-fx-text-fill: green;");
                    lblGanancias.setText("+" + Integer.toString(Integer.parseInt(txtDinero.toString())*2) + "$");
                }
                else
                {
                    // si perdemos msotramos el dinero que se ha restado
                    lblGanancias.setStyle("-fx-text-fill: red;");
                    lblGanancias.setText("-" + txtDinero.toString() + "$");
                }
            }
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

        // ocultamos el mensaje de error
        lblError.setVisible(false);
    }
    
}
