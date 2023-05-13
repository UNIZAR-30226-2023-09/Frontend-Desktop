import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ClasificacionController implements Initializable{
    
    @FXML
    private Button btnEmpezar, btnSalir;

    @FXML
    private Label lblEsperar,
                    lblP1, lblP2, lblP3, lblP4,
                    lblJ1, lblJ2, lblJ3, lblJ4;

    private  ObservableList<Label> lblPuntos, lblJugadores;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnEmpezar.setVisible(false);
        btnSalir.setVisible(false);
        lblEsperar.setVisible(false);

        lblPuntos = FXCollections.observableArrayList();
        lblPuntos.add(lblP1); lblPuntos.add(lblP2);
        lblPuntos.add(lblP3); lblPuntos.add(lblP4);

        lblJugadores = FXCollections.observableArrayList();
        lblJugadores.add(lblJ1); lblJugadores.add(lblJ2);
        lblJugadores.add(lblJ3); lblJugadores.add(lblJ4);

        // esperamos a que terminen todos
        Thread thread = new Thread()
        {
            public void run()
            {
                mostrarClasificacion();
            }
        };

        thread.start();
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException 
    {
        Object evt = e.getSource();
        if(evt.equals(btnEmpezar))
        {
            // empezar la siguiente partida
        }
        else if(evt.equals(btnSalir))
        {
            // volvemos al menu principal
            App.setRoot("MenuPrincipal");
        }
    }

    private void mostrarClasificacion()
    {
        while(!GestionPartida.resultadosTorneo)
        {
            ConexionServidor.esperar();
        }

        // actualizar los resultados del torneo
        for(int i=0; i<4; i++)
        {
            lblJugadores.get(i).setText(GestionPartida.ordenJugadores[i]);
            lblPuntos.get(i).setText(Integer.toString(GestionPartida.clasificacionTorneo[i]));
        }

        if(!GestionPartida.enTorneo)
        {
            // mostrar el boton de salir de la partida y quitar el de empezar
            btnSalir.setVisible(true);
        }
        else
        {
            // mostrar el boton de empezar (solo si es mi partida)
            if(GestionPartida.dueñoPartida)
            {
                btnEmpezar.setVisible(true);
            }
            else
            {
                lblEsperar.setVisible(true);
            }
        }
    }
}
