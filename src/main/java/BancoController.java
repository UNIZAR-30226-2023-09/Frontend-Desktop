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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.ImageView;

public class BancoController implements Initializable{
    @FXML
    private TextField txtBanco;

    @FXML
    private Button btnIngresar, btnRetirar, btnCancelar;

    @FXML
    private Label lblText, lblError;

    @FXML
    private ImageView imgBanco;

    public static Semaphore semaphoreBanco = new Semaphore(0);

    private boolean ing_ret = false;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException{
        Object evt = e.getSource();

        ing_ret = false;
        
        // ingresar dinero
        if(evt.equals(btnIngresar))
        {
            if (txtBanco.getText().equals("")){
                // indicar que tiene que introducir una cantidad no nula
                lblError.setVisible(true);
                lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
            }
            else if (Integer.parseInt(txtBanco.getText()) > GestionPartida.dineroJugadores[GestionPartida.indiceJugador])
            {
                // indicar que tiene que introducir una cantidad no nula
                lblError.setVisible(true);
                lblError.setText("No tienes suficinete dinero");
            }
            else
            {
                GestionPartida.depositarDinero(Integer.parseInt(txtBanco.getText()));
                ing_ret = true;
                semaphoreBanco.release();
            }
        }
        // retirar dinero
        else if(evt.equals(btnRetirar))
        {
            if (txtBanco.getText().equals("")){
                lblError.setVisible(true);
                lblError.setText("Debes introducir una cantidad estrictamente superior a 0$");
            }
            else if(Integer.parseInt(txtBanco.getText()) > GestionPartida.dineroEnBanco)
            {
                lblError.setVisible(true);
                lblError.setText("No tienes suficinete dinero en el banco");
            }
            else
            {
                GestionPartida.retirarDinero(Integer.parseInt(txtBanco.getText()));
                ing_ret = true;
                semaphoreBanco.release();
            }
        }
        // no hacer nada
        else if(evt.equals(btnCancelar))
        {
            semaphoreBanco.release();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Crea un TextFormatter para valores enteros y establece un Filter para aceptar solo valores numéricos
        TextFormatter<Integer> textFormatter = new TextFormatter<Integer>((Change c) -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            } else {
                return null;
            }
        });
        // Establece el TextFormatter en el TextField
        txtBanco.setTextFormatter(textFormatter);

        lblError.setVisible(false);
    }

    public Boolean gestionBanco()
    {
        // dejamos la vista limpia de posibles datos de un uso anterior
        lblError.setVisible(false);
        txtBanco.clear();

        try {
            semaphoreBanco.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ing_ret;
    }
}
