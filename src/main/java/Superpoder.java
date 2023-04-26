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

public class Superpoder implements Initializable{

    @FXML
    private TextField txtCasilla;

    @FXML
    private Button btnAceptar;

    @FXML
    public Label lblSuperpoder, lblError;

    @FXML
    private ImageView imgRule;

    public static Semaphore semaphoreSuper = new Semaphore(0);

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();
    
        if (evt.equals(btnAceptar)){
            semaphoreSuper.release();
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

        // ocultamos el mensaje de error y del dinero obtenido/perdido
        lblError.setVisible(false);
    }
    
}
