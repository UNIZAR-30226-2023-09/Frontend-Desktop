import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CasinoController {

    @FXML
    private TextField txtDinero;

    @FXML
    private Button btnApostar;

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();
        /*
        Thread threadL = new Thread() {
            public void run() {
                
                try {
                    for (int i = 0; i < 15; i++) {
                        File file = new File("src/main/resources/Dice" + (random.nextInt(6) + 1) + ".png");
                        dado1.setImage(new Image(file.toURI().toString()));
                        Thread.sleep(50);
                    }
                    File file = new File("src/main/resources/Dice" + GestionPartida.dados[0] + ".png");
                    dado1.setImage(new Image(file.toURI().toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread threadR = new Thread() {
            public void run() {
                
                try {
                    for (int i = 0; i < 15; i++) {
                        File file = new File("src/main/resources/Dice" + (random.nextInt(6) + 1) + ".png");
                        dado2.setImage(new Image(file.toURI().toString()));
                        Thread.sleep(50);
                    }
                    File file = new File("src/main/resources/Dice"+ GestionPartida.dados[1] + ".png");
                    dado2.setImage(new Image(file.toURI().toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        threadL.start();
        threadR.start();
        */
    }
    
}
