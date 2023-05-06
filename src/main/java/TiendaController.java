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
    private Button btnSkin1, btnSkin2, btnSkin3, btnSkin4, btnSkin5, btnSkin6, btnSkin7, btnSkin8, btnSkin9, btnSkin10, btnSkin11, btnSkin12, btnVolver;

    @FXML
    private Label lblNombre, lblGemas,
            lblSkin1, lblSkin2, lblSkin3, lblSkin4, lblSkin5, lblSkin6, lblSkin7, lblSkin8, lblSkin9, lblSkin10, lblSkin11, lblSkin12,
            lblP1, lblP2, lblP3, lblP4, lblP5, lblP6, lblP7, lblP8, lblP9, lblP10, lblP11, lblP12,
            lblE1, lblE2, lblE3, lblE4, lblE5, lblE6, lblE7, lblE8, lblE9, lblE10, lblE11, lblE12;

    private Boolean compradaSkin1 = false,
            compradaSkin2 = false,
            compradaSkin3 = false,
            compradaSkin4 = false,
            compradaSkin5 = false,
            compradaSkin6 = false,
            compradaSkin7 = false,
            compradaSkin8 = false,
            compradaSkin9 = false,
            compradaSkin10 = false,
            compradaSkin11 = false,
            compradaSkin12 = false;

    private int precioSkin1, precioSkin2, precioSkin3, precioSkin4, precioSkin5, precioSkin6, precioSkin7, precioSkin8, precioSkin9, precioSkin10, precioSkin11, precioSkin12;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(GestionPartida.nombreUser);
        lblGemas.setText(Integer.toString(GestionPartida.gemas));

        // ponemos el dinero de cada skin
        actualizarPrecios();
    }

    private void actualizarPrecios()
    {
        GestionPartida.mostrarSkins();

        while (!GestionPartida.skinsObtenidas) {
            ConexionServidor.esperar();
        }
        GestionPartida.skinsObtenidas = false;

        Platform.runLater(() -> {
            for (String skin : GestionPartida.listaSkins) {
                String[] partes = skin.split(":");
                String nombre = partes[0].trim();
                String precio = partes[1].trim();

                System.out.println(skin);

                if (nombre.equals("BAXTER")) {
                    if (precio.equals("0")) {
                        lblP1.setVisible(false);
                        lblP1.setManaged(false);
                        btnSkin1.setText("Equipar");
                        compradaSkin1 = true;
                    } else {
                        lblP1.setText(precio);
                        precioSkin1 = Integer.parseInt(precio);
                    }

                    lblE1.setVisible(false);
                } else if (nombre.equals("BERTA")) {
                    if (precio.equals("0")) {
                        lblP2.setVisible(false);
                        lblP2.setManaged(false);
                        btnSkin2.setText("Equipar");
                        compradaSkin2 = true;
                    } else {
                        lblP2.setText(precio);
                        precioSkin2 = Integer.parseInt(precio);
                    }
                    lblE2.setVisible(false);
                } else if (nombre.equals("DIONIX")) {
                    if (precio.equals("0")) {
                        lblP3.setVisible(false);
                        lblP3.setManaged(false);
                        btnSkin3.setText("Equipar");
                        compradaSkin3 = true;
                    } else {
                        lblP3.setText(precio);
                        precioSkin3 = Integer.parseInt(precio);
                    }
                    lblE3.setVisible(false);
                } else if (nombre.equals("JEANCARLO")) {
                    if (precio.equals("0")) {
                        lblP4.setVisible(false);
                        lblP4.setManaged(false);
                        btnSkin4.setText("Equipar");
                        compradaSkin4 = true;
                    } else {
                        lblP4.setText(precio);
                        precioSkin4 = Integer.parseInt(precio);
                    }
                    lblE4.setVisible(false);
                } else if (nombre.equals("JULS")) {
                    if (precio.equals("0")) {
                        lblP5.setVisible(false);
                        lblP5.setManaged(false);
                        btnSkin5.setText("Equipar");
                        compradaSkin5 = true;
                    } else {
                        lblP5.setText(precio);
                        precioSkin5 = Integer.parseInt(precio);
                    }
                    lblE5.setVisible(false);
                } else if (nombre.equals("LUCAS")) {
                    if (precio.equals("0")) {
                        lblP6.setVisible(false);
                        lblP6.setManaged(false);
                        btnSkin6.setText("Equipar");
                        compradaSkin6 = true;
                    } else {
                        lblP6.setText(precio);
                        precioSkin6 = Integer.parseInt(precio);
                    }
                    lblE6.setVisible(false);
                } else if (nombre.equals("PLEX")) {
                    if (precio.equals("0")) {
                        lblP7.setVisible(false);
                        lblP7.setManaged(false);
                        btnSkin7.setText("Equipar");
                        compradaSkin7 = true;
                    } else {
                        lblP7.setText(precio);
                        precioSkin7 = Integer.parseInt(precio);
                    }
                    lblE7.setVisible(false);
                } else if (nombre.equals("TITE")) {
                    if (precio.equals("0")) {
                        lblP8.setVisible(false);
                        lblP8.setManaged(false);
                        btnSkin8.setText("Equipar");
                        compradaSkin8 = true;
                    } else {
                        lblP8.setText(precio);
                        precioSkin8 = Integer.parseInt(precio);
                    }
                    lblE8.setVisible(false);
                } else if (nombre.equals("TABLERO1")) {
                    if (precio.equals("0")) {
                        lblP9.setVisible(false);
                        lblP9.setManaged(false);
                        btnSkin9.setText("Equipar");
                        compradaSkin9 = true;
                    } else {
                        lblP9.setText(precio);
                        precioSkin9 = Integer.parseInt(precio);
                    }
                    lblE9.setVisible(false);
                } else if (nombre.equals("TABLERO2")) {
                    if (precio.equals("0")) {
                        lblP10.setVisible(false);
                        lblP10.setManaged(false);
                        btnSkin10.setText("Equipar");
                        compradaSkin10 = true;
                    } else {
                        lblP10.setText(precio);
                        precioSkin10 = Integer.parseInt(precio);
                    }
                    lblE10.setVisible(false);
                } else if (nombre.equals("TABLERO3")) {
                    if (precio.equals("0")) {
                        lblP11.setVisible(false);
                        lblP11.setManaged(false);
                        btnSkin11.setText("Equipar");
                        compradaSkin11 = true;
                    } else {
                        lblP11.setText(precio);
                        precioSkin11 = Integer.parseInt(precio);
                    }
                    lblE11.setVisible(false);
                } else if (nombre.equals("TABLERO4")) {
                    if (precio.equals("0")) {
                        lblP12.setVisible(false);
                        lblP12.setManaged(false);
                        btnSkin12.setText("Equipar");
                        compradaSkin12 = true;
                    } else {
                        lblP12.setText(precio);
                        precioSkin12 = Integer.parseInt(precio);
                    }
                    lblE12.setVisible(false);
                }
            }
        });
    }

    private void comprarSkin(String IDskin, int precio) {
        // comprobar que tengamos las suficientes gemas
        if (GestionPartida.gemas >= precio) {
            // enviamos el mensaje
            GestionPartida.comprarSkin(IDskin);

            System.out.println("skin comprada: " + IDskin);

            // actualizamos la tienda
            actualizarPrecios();

            // actualizamos las gemas del jugador
            Platform.runLater(() -> {
                lblGemas.setText(Integer.toString(GestionPartida.gemas));
            });
        } else {
            if (IDskin.equals("BAXTER")) {
                lblE1.setVisible(true);
            } else if (IDskin.equals("BERTA")) {
                lblE2.setVisible(true);
            } else if (IDskin.equals("DIONIX")) {
                lblE3.setVisible(true);
            } else if (IDskin.equals("JEANCARLO")) {
                lblE4.setVisible(true);
            } else if (IDskin.equals("JULS")) {
                lblE5.setVisible(true);
            } else if (IDskin.equals("LUCAS")) {
                lblE6.setVisible(true);
            } else if (IDskin.equals("PLEX")) {
                lblE7.setVisible(true);
            } else if (IDskin.equals("TITE")) {
                lblE8.setVisible(true);
            } else if (IDskin.equals("TABLERO1")) {
                lblE9.setVisible(true);
            } else if (IDskin.equals("TABLERO2")) {
                lblE10.setVisible(true);
            } else if (IDskin.equals("TABLERO3")) {
                lblE11.setVisible(true);
            } else if (IDskin.equals("TABLERO4")) {
                lblE12.setVisible(true);
            }

        }
    }

    private void equiparSkin(String IDskin) {
        GestionPartida.equiparSkin(IDskin);
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        if (btnSkin1.equals(evt)) {
            if (!compradaSkin1)
                comprarSkin("BAXTER", precioSkin1);
            else
                equiparSkin("BAXTER");
        } else if (btnSkin2.equals(evt)) {
            if (!compradaSkin2)
                comprarSkin("BERTA", precioSkin2);
            else
                equiparSkin("BERTA");
        } else if (btnSkin3.equals(evt)) {
            if (!compradaSkin3)
                comprarSkin("DIONIX", precioSkin3);
            else
                equiparSkin("DIONIX");
        } else if (btnSkin4.equals(evt)) {
            if (!compradaSkin4)
                comprarSkin("JEANCARLO", precioSkin4);
            else
                equiparSkin("JEANCARLO");
        } else if (btnSkin5.equals(evt)) {
            if (!compradaSkin5)
                comprarSkin("JULS", precioSkin5);
            else
                equiparSkin("JULS");
        } else if (btnSkin6.equals(evt)) {
            if (!compradaSkin6)
                comprarSkin("LUCAS", precioSkin6);
            else
                equiparSkin("LUCAS");
        } else if (btnSkin7.equals(evt)) {
            if (!compradaSkin7)
                comprarSkin("PLEX", precioSkin7);
            else
                equiparSkin("PLEX");
        } else if (btnSkin8.equals(evt)) {
            if (!compradaSkin8)
                comprarSkin("TITE", precioSkin8);
            else
                equiparSkin("TITE");
        } else if (btnSkin9.equals(evt)) {
            if (!compradaSkin9)
                comprarSkin("TABLERO1", precioSkin9);
            else
                equiparSkin("TABLERO1");
        } else if (btnSkin10.equals(evt)) {
            if (!compradaSkin10)
                comprarSkin("TABLERO2", precioSkin10);
            else
                equiparSkin("TABLERO2");
        } else if (btnSkin11.equals(evt)) {
            if (!compradaSkin11)
                comprarSkin("TABLERO3", precioSkin11);
            else
                equiparSkin("TABLERO3");
        } else if (btnSkin12.equals(evt)) {
            if (!compradaSkin12)
                comprarSkin("TABLERO4", precioSkin12);
            else
                equiparSkin("TABLERO4");
        } else if (btnVolver.equals(evt)) {
            App.setRoot("MenuPrincipal");
        }
    }
}
