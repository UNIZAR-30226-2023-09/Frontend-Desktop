import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TiendaController {
    
    @FXML
    private Button btnSkin1, btnSkin2, btnSkin3, btnSkin4;

    private void comprarSkin(String skinName, int precio)
    {
        // comprobar que tengamos las suficientes gemas
        if(GestionPartida.gemas >= precio)
        {
            // restamos las gemas al usuario QUIEN HACE ESTO???? NOSOTROS O SSE ENCARGAN LOS DE LA BASE

            // indicamos que todo ha salido bien
            JOptionPane.showMessageDialog(null, "Compra realizada con exito", "OK", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "No tienes suficientes gemas", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException
    {
        Object evt = e.getSource();

        if(btnSkin1.equals(evt))
        {
            comprarSkin("Skin1", 30);
        }
        else if(btnSkin2.equals(evt))
        {
            comprarSkin("Skin2", 30);
        }
        else if(btnSkin3.equals(evt))
        {
            comprarSkin("Skin3", 30);
        }
        else if(btnSkin4.equals(evt))
        {
            comprarSkin("Skin4", 30);
        }
    }
}
