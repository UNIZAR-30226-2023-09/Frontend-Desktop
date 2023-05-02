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
import javafx.scene.layout.VBox;

public class SuperpoderController implements Initializable{

    @FXML
    private TextField txtCasilla;

    @FXML
    private Button btnAceptar;

    @FXML
    public Label lblSuperpoder, lblError;

    @FXML
    private VBox rellenarCampos;

    @FXML
    private ImageView imgRule;

    public static Semaphore semaphoreSuper = new Semaphore(0);

    public static String casillaS;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();
    
        if (evt.equals(btnAceptar)){
            int i = Integer.parseInt(GestionPartida.superPoder);
            if(i==1){
                if (txtCasilla.getText().equals("")){
                    // indicar que tiene que introducir una cantidad no nula
                    lblError.setVisible(true);
                    lblError.setText("Debes introducir una casilla valida");
                }
                else{
                    casillaS = txtCasilla.getText();
                    int casilla = Integer.parseInt(txtCasilla.getText());
                    if(casilla < 1 || casilla >40){
                        lblError.setVisible(true);
                        lblError.setText("Debes introducir una casilla valida");
                    }
                    else{
                        GestionPartida.enviarCasilla(casillaS);
                        semaphoreSuper.release();
                    }
                }
            }
            else{
                semaphoreSuper.release();
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
        txtCasilla.setTextFormatter(textFormatter);
        //txtCasilla.setVisible(false);
        
        // ocultamos el mensaje de error y del dinero obtenido/perdido
        lblError.setVisible(false);
    }
    
}
