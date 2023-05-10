import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventosController
{    
    @FXML
    private Button btnAceptar;
    
    @FXML
    private ImageView imgEvento;

    private Semaphore semaphore = new Semaphore(0);

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        // solo hay un boton asi que no hace falta mirar cual hemos pulsado

        semaphore.release();
    }

    public void mostrarEvento()
    {
        Platform.runLater(() -> {
            File file = new File("src/main/resources/EVENTOS/EV" + GestionPartida.evento +".png");
            imgEvento.setImage(new Image(file.toURI().toString()));
        });

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  Integer transformarEvento(String event){
        Integer numEvent = 0;
        switch (event) {
            case "ninguno":
                numEvent = 0;
                break;
            case "BancaRota ":
                numEvent = 1;
                break;
            case "BancoDispara":
                numEvent = 2;
                break;
            case "DadosDobles":
                numEvent = 3;
                break;
            case "DadosMitad":
                numEvent = 4;
                break;
            case "EconomiaInestable":
                numEvent = 5;
                break;
            default:
                System.out.println("ERROR EVENTO");
                break;
        }
        return numEvent;
    }
}
