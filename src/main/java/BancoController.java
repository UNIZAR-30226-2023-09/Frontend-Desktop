import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class BancoController implements Initializable{
    @FXML
    private TextField txtBanco;

    @FXML
    private Button btnIngresar, btnRetirar, btnConfirmar, btnRechazar;

    @FXML
    private Label lblText, lblError2;

    @FXML
    private ImageView imgBanco;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException{
        Object evt = e.getSource();
        boolean ingresar = false;

        if (evt.equals(btnIngresar)){
            ingresar = true;
            //MOSTRAR EL TEXTO CORRECTO
            //PONER EL BOTON MAS OSCURO PARA QUE PAREZCA QUE ESTA PULSADO
        }
        else if (evt.equals(btnRetirar)) {
            ingresar = false;
            //MOSTRAR EL TEXTO CORRECTO
            //PONER EL BOTON MAS OSCURO PARA QUE PAREZCA QUE ESTA PULSADO
        }
        else if (evt.equals(btnConfirmar)) {
            if (txtBanco.getText().equals("")){
                // indicar que tiene que introducir una cantidad no nula
                lblError2.setVisible(true);
                lblError2.setText("Debes introducir una cantidad estrictamente superior a 0$");
            }
            else{
                if(ingresar){
                    int dinero = Integer.parseInt(txtBanco.getText());
                    if (dinero > GestionPartida.dineroJugadores[GestionPartida.indiceJugador]){ //REVISAR QUE DINERO ES, dineroJugador[indiceJugador]
                        // indicar que no tienes suficiente saldo
                        lblError2.setVisible(true);
                        lblError2.setText("No tienes el suficiente dinero");
                    }
                    else if (dinero <= 0){
                        // indicar que tiene que introducir una cantidad no nula
                        lblError2.setVisible(true);
                        lblError2.setText("Debes introducir una cantidad estrictamente superior a 0$");
                    }
                    else{
                        GestionPartida.depositarDinero(dinero);
                        ConexionServidor.esperar(); //  REVISAR SI ESTO TIENE QUE IR AQUI
                    }
                }
                else{//es para retirar
                    int dinero = Integer.parseInt(txtBanco.getText());
                    if (dinero > GestionPartida.dineroEnBanco){ //REVISAR QUE DINERO ES, dineroJugador[indiceJugador]
                        // indicar que no tienes suficiente saldo
                        lblError2.setVisible(true);
                        lblError2.setText("No tienes el suficiente dinero");
                    }
                    else if (dinero <= 0){
                        // indicar que tiene que introducir una cantidad no nula
                        lblError2.setVisible(true);
                        lblError2.setText("Debes introducir una cantidad estrictamente superior a 0$");
                    }
                    else{
                        GestionPartida.retirarDinero(dinero);
                        ConexionServidor.esperar();
                    }
                }
                
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }
}
