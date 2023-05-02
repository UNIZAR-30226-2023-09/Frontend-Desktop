import java.io.File;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
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
    private ImageView imgSuperpoder;

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

        rellenarCampos.setVisible(false);
        rellenarCampos.setManaged(false);
        
        // ocultamos el mensaje de error y del dinero obtenido/perdido
        lblError.setVisible(false);
    }

    public void gestionarSuperpoder()
    {
        Platform.runLater(() -> {

            int i = Integer.parseInt(GestionPartida.superPoder);
            
            File file = new File("");   // lo inicializamos vacio

            switch (i) {
                case 1:
                    lblSuperpoder.setText("Elija la casilla a la que quiere ir");
                    file = new File("src/main/resources/SUPERPODERES/SP1.png");
                    rellenarCampos.setVisible(true);
                    rellenarCampos.setManaged(true);
                    break;
                case 2:
                    lblSuperpoder.setText("Acudes corriendo al banco");
                    file = new File("src/main/resources/SUPERPODERES/SP2.png");
                    break;
                case 3:
                    lblSuperpoder.setText("Acudes corriendo al casino");
                    file = new File("src/main/resources/SUPERPODERES/SP3.png");
                    break;
                case 4:
                    lblSuperpoder.setText("Acudes corriendo a la casilla de salida");
                    file = new File("src/main/resources/SUPERPODERES/SP4.png");
                    break;
                case 5:
                    lblSuperpoder.setText("Retrocedes 3 casillas");
                    file = new File("src/main/resources/SUPERPODERES/SP5.png");
                    break;
                case 6:
                    lblSuperpoder.setText("Aumenta su suerte en el casino");
                    file = new File("src/main/resources/SUPERPODERES/SP6.png");
                    break;
                case 7:
                    file = new File("src/main/resources/SUPERPODERES/SP7.png");
                    break;
                case 8:
                    file = new File("src/main/resources/SUPERPODERES/SP8.png");
                    break;
                case 9:
                    file = new File("src/main/resources/SUPERPODERES/SP9.png");
                    break;
                case 10:
                    file = new File("src/main/resources/SUPERPODERES/SP10.png");
                    break;
                case 11:
                    file = new File("src/main/resources/SUPERPODERES/SP11.png");
                    break;
                case 12:
                    file = new File("src/main/resources/SUPERPODERES/SP12.png");
                    break;
                default:
                    System.out.println("ERROR SUPERPODER");
                    break;
            }

            imgSuperpoder.setImage(new Image(file.toURI().toString()));

        });

        try {
            semaphoreSuper.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
