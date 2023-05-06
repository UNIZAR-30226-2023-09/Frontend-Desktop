import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

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
    private Label lblText, lblError;

    @FXML
    private ImageView imgBanco;

    public static Semaphore semaphoreBanco = new Semaphore(0);

    private boolean ingresar = false;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException{
        Object evt = e.getSource();       

        if (evt.equals(btnIngresar)){
            ingresar = true;
            //MOSTRAR EL TEXTO CORRECTO
            //PONER EL BOTON MAS OSCURO PARA QUE PAREZCA QUE ESTA PULSADO
            System.out.println("ingresar1");
            System.out.println(ingresar);
        }
        else if (evt.equals(btnRetirar)) {
            ingresar = false;
            //MOSTRAR EL TEXTO CORRECTO
            //PONER EL BOTON MAS OSCURO PARA QUE PAREZCA QUE ESTA PULSADO
            System.out.println("retirar1");
            System.out.println(ingresar);
        }
        else if (evt.equals(btnConfirmar)) {
            if (txtBanco.getText().equals("")){
                // indicar que tiene que introducir una cantidad no nula
                lblError.setVisible(true);
                lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
            }
            else{
                if(ingresar){
                    Integer dinero = Integer.parseInt(txtBanco.getText());
                    if (dinero > GestionPartida.dineroJugadores[GestionPartida.indiceJugador]){ //REVISAR QUE DINERO ES, dineroJugador[indiceJugador]
                        // indicar que no tienes suficiente saldo
                        lblError.setVisible(true);
                        lblError.setText("No tienes el suficiente dinero");

                        System.out.println("ingresar2");
                        System.out.println(dinero);
                        System.out.println(" ");
                        System.out.println(GestionPartida.dineroJugadores[GestionPartida.indiceJugador]);
                    }
                    else if (dinero <= 0){
                        // indicar que tiene que introducir una cantidad no nula
                        lblError.setVisible(true);
                        lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
                    }
                    else{
                        GestionPartida.depositarDinero(dinero);
                        System.out.println("Hecho el ingreso");
                        ConexionServidor.esperar(); //  REVISAR SI ESTO TIENE QUE IR AQUI
                        semaphoreBanco.release();
                    }
                }
                else{//es para retirar
                    Integer dinero = Integer.parseInt(txtBanco.getText());
                    if (dinero > GestionPartida.dineroEnBanco){ //REVISAR QUE DINERO ES, dineroJugador[indiceJugador]
                        // indicar que no tienes suficiente saldo
                        lblError.setVisible(true);
                        lblError.setText("No tienes el suficiente dinero");

                        System.out.println("retirar2");
                        System.out.println(dinero);
                        System.out.println(" ");
                        System.out.println(GestionPartida.dineroEnBanco);
                    }
                    else if (dinero <= 0){
                        // indicar que tiene que introducir una cantidad no nula
                        lblError.setVisible(true);
                        lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
                    }
                    else{
                        System.out.println("Hecho la retirada");
                        GestionPartida.retirarDinero(dinero);
                        ConexionServidor.esperar();
                        semaphoreBanco.release();
                    }
                }
                
            }
        }
        else if(evt.equals(btnRechazar)){
            semaphoreBanco.release();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblError.setVisible(false);
    }
}
