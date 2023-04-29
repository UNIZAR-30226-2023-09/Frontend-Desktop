import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TiendaController implements Initializable {

    @FXML
    private Button btnSkin1, btnSkin2, btnSkin3, btnSkin4, btnSkin5, btnSkin6, btnSkin7, btnSkin8, btnVolver;

    @FXML
    private Label lblNombre, lblGemas,
        lblSkin1, lblSkin2, lblSkin3, lblSkin4, lblSkin5, lblSkin6, lblSkin7, lblSkin8,
        lblP1, lblP2, lblP3, lblP4, lblP5, lblP6, lblP7, lblP8,
        lblE1, lblE2, lblE3, lblE4, lblE5, lblE6, lblE7, lblE8;

    private Boolean compradaSkin1 = false,
                    compradaSkin2 = false,
                    compradaSkin3 = false,
                    compradaSkin4 = false,
                    compradaSkin5 = false,
                    compradaSkin6 = false,
                    compradaSkin7 = false,
                    compradaSkin8 = false;

    private int precioSkin1, precioSkin2, precioSkin3, precioSkin4, precioSkin5, precioSkin6, precioSkin7, precioSkin8;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));

        // ponemos el dinero de cada skin
        GestionPartida.mostrarSkins();

        while (!GestionPartida.skinsObtenidas) {
            ConexionServidor.esperar();
        }
        GestionPartida.skinsObtenidas = false;

        actualizarPrecios();
    }

    private void actualizarPrecios()
    {
        Platform.runLater(() -> {
            for (String skin : GestionPartida.listaSkins)
            {
                String[] partes = skin.split(":");
                String nombre = partes[0];
                String precio = partes[1];

                if(nombre.equals("BAXTER"))
                {
                    if(precio.equals("0"))
                    {
                        lblP1.setVisible(false);
                        lblP1.setManaged(false);
                        btnSkin1.setText("Equipar");
                        compradaSkin1 = true;
                    }
                    else
                    {
                        lblP1.setText(precio);
                        precioSkin1 = Integer.parseInt(precio);
                    }

                    lblE1.setVisible(false);
                }
                else if(nombre.equals("BERTA"))
                {
                    if(precio.equals("0"))
                    {
                        lblP2.setVisible(false);
                        lblP2.setManaged(false);
                        btnSkin2.setText("Equipar");
                        compradaSkin2 = true;
                    }
                    else
                    {
                        lblP2.setText(precio);
                        precioSkin2 = Integer.parseInt(precio);
                    }
                    lblE2.setVisible(false);
                }
                else if(nombre.equals("DIONIX"))
                {
                    if(precio.equals("0"))
                    {
                        lblP3.setVisible(false);
                        lblP3.setManaged(false);
                        btnSkin3.setText("Equipar");
                        compradaSkin3 = true;
                    }
                    else
                    {
                        lblP3.setText(precio);
                        precioSkin3 = Integer.parseInt(precio);
                    }
                    lblE3.setVisible(false);
                }
                else if(nombre.equals("JEANCARLO"))
                {
                    if(precio.equals("0"))
                    {
                        lblP4.setVisible(false);
                        lblP4.setManaged(false);
                        btnSkin4.setText("Equipar");
                        compradaSkin4 = true;
                    }
                    else
                    {
                        lblP4.setText(precio);
                        precioSkin4 = Integer.parseInt(precio);
                    }
                    lblE4.setVisible(false);
                }
                else if(nombre.equals("JULS"))
                {
                    if(precio.equals("0"))
                    {
                        lblP5.setVisible(false);
                        lblP5.setManaged(false);
                        btnSkin5.setText("Equipar");
                        compradaSkin5 = true;
                    }
                    else
                    {
                        lblP5.setText(precio);
                        precioSkin5 = Integer.parseInt(precio);
                    }
                    lblE5.setVisible(false);
                }
                else if(nombre.equals("LUCAS"))
                {
                    if(precio.equals("0"))
                    {
                        lblP6.setVisible(false);
                        lblP6.setManaged(false);
                        btnSkin6.setText("Equipar");
                        compradaSkin6 = true;
                    }
                    else
                    {
                        lblP6.setText(precio);
                        precioSkin6 = Integer.parseInt(precio);
                    }
                    lblE6.setVisible(false);
                }
                else if(nombre.equals("PLEX"))
                {
                    if(precio.equals("0"))
                    {
                        lblP7.setVisible(false);
                        lblP7.setManaged(false);
                        btnSkin7.setText("Equipar");
                        compradaSkin7 = true;
                    }
                    else
                    {
                        lblP7.setText(precio);
                        precioSkin7 = Integer.parseInt(precio);
                    }
                    lblE7.setVisible(false);
                }
                else if(nombre.equals("TITE"))
                {
                    if(precio.equals("0"))
                    {
                        lblP8.setVisible(false);
                        lblP8.setManaged(false);
                        btnSkin8.setText("Equipar");
                        compradaSkin8 = true;
                    }
                    else
                    {
                        lblP8.setText(precio);
                        precioSkin8 = Integer.parseInt(precio);
                    }
                    lblE8.setVisible(false);
                }
            }
        });
    }

    private void comprarSkin(String IDskin, int precio) {
        // comprobar que tengamos las suficientes gemas
        if (GestionPartida.gemas >= precio) {
            // enviamos el mensaje
            GestionPartida.comprarSkin(IDskin);

            // actualizamos la tienda
            actualizarPrecios();
        } else {
            if(IDskin.equals("BAXTER"))
            {
                lblE1.setVisible(true);
            }
            else if(IDskin.equals("BERTA"))
            {
                lblE2.setVisible(true);
            }
            else if(IDskin.equals("DIONIX"))
            {
                lblE3.setVisible(true);
            }
            else if(IDskin.equals("JEANCARLO"))
            {
                lblE4.setVisible(true);
            }
            else if(IDskin.equals("JULS"))
            {
                lblE5.setVisible(true);
            }
            else if(IDskin.equals("LUCAS"))
            {
                lblE6.setVisible(true);
            }
            else if(IDskin.equals("PLEX"))
            {
                lblE7.setVisible(true);
            }
            else if(IDskin.equals("TITE"))
            {
                lblE8.setVisible(true);
            }

        }
    }

    private void equiparSkin(String IDskin)
    {
        GestionPartida.equiparSkin(IDskin);
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnSkin1.equals(evt))
        {
            if(!compradaSkin1) comprarSkin("BAXTER", precioSkin1);
            else equiparSkin("BAXTER");
        }
        else if (btnSkin2.equals(evt))
        {
            if(!compradaSkin2) comprarSkin("BERTA", precioSkin2);
            else equiparSkin("BERTA");
        }
        else if (btnSkin3.equals(evt))
        {
            if(!compradaSkin3) comprarSkin("DIONIX", precioSkin3);
            else equiparSkin("DIONIX");
        }
        else if (btnSkin4.equals(evt))
        {
            if(!compradaSkin4) comprarSkin("JEANCARLO", precioSkin4);
            else equiparSkin("JEANCARLO");
        }
        else if (btnSkin5.equals(evt))
        {
            if(!compradaSkin5) comprarSkin("JULS", precioSkin5);
            else equiparSkin("JULS");
        }
        else if (btnSkin6.equals(evt))
        {
            if(!compradaSkin6) comprarSkin("LUCAS", precioSkin6);
            else equiparSkin("LUCAS");
        }
        else if (btnSkin7.equals(evt))
        {
            if(!compradaSkin7) comprarSkin("PLEX", precioSkin7);
            else equiparSkin("PLEX");
        }
        else if (btnSkin8.equals(evt))
        {
            if(!compradaSkin8) comprarSkin("TITE", precioSkin8);
            else equiparSkin("TITE");
        }
        else if (btnVolver.equals(evt))
        {
            App.setRoot("MenuPrincipal");
        }
    }
}
