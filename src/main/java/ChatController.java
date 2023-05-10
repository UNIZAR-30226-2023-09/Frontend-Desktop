import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChatController implements Initializable {
    private ObservableList<String> chatMessages; // Cambio en la declaración de la lista de mensajes
    private Timeline timeline;

    @FXML
    private TextField inputField;

    @FXML
    private Button btnEnviar;

    @FXML
    private VBox chatContainer; // Cambio en la declaración del contenedor del chat

    @FXML
    private ListView<String> chatListView = new ListView<>(chatMessages); // Cambio en la declaración del ListView del
    // chat

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Crear la lista de mensajes del chat
        chatMessages = FXCollections.observableArrayList();

        // Configurar el ListView con la lista de mensajes del chat
        chatListView.setItems(chatMessages);

        // Configurar la fábrica de celdas personalizada
        chatListView.setCellFactory(listView -> new ListCell<String>() {
            private HBox hbox = new HBox();
            private ImageView imageView = new ImageView();
            private Label messageLabel = new Label();

            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    String nombreJugador = item.split(":")[0];
                    String mensaje = item.split(":")[1];
                    String skin = GestionPartida.skinsJugadores[GestionPartida.obtenerIndiceJugador(nombreJugador)];
                    URL imageUrl = getClass().getResource("/skins/" + skin + ".png");
                    imageView.setImage(new Image(imageUrl.toString()));

                    messageLabel.setText(mensaje);
                    messageLabel.setStyle("-fx-font-weight: normal;"); // Estilo normal para el mensaje

                    Label nameLabel = new Label(nombreJugador + ":");
                    nameLabel.setStyle("-fx-font-weight: bold;"); // Estilo en negrita para el nombre del usuario

                    hbox.getChildren().clear();

                    if (nombreJugador.equals(GestionPartida.nombreUser)) {
                        hbox.getChildren().addAll(nameLabel, messageLabel, imageView);
                        hbox.setAlignment(Pos.TOP_RIGHT);
                    } else {
                        hbox.getChildren().addAll(imageView, nameLabel, messageLabel);
                        hbox.setAlignment(Pos.TOP_LEFT);
                    }

                    setGraphic(hbox);
                }
            }
        });

        // Agregar evento de teclado al campo de texto
        inputField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                enviarMensaje();
            }
        });

        // Crear un contenedor para el TextField y ajustar su tamaño
        HBox inputContainer = new HBox(inputField);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputContainer.setPadding(new Insets(10));

        // Crear un contenedor para el botón y el TextField
        HBox buttonContainer = new HBox(btnEnviar);
        buttonContainer.setPadding(new Insets(10));
        buttonContainer.setAlignment(Pos.CENTER);

        // Crear un Timeline para llamar a actualizarChat cada segundo
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> actualizarChat(GestionPartida.chat)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Crear un contenedor principal para el inputContainer y el buttonContainer
        HBox mainContainer = new HBox(inputContainer, buttonContainer);
        mainContainer.setAlignment(Pos.CENTER_LEFT);
        mainContainer.setSpacing(4);

        chatContainer.getStylesheets().add(getClass().getResource("chat.css").toExternalForm());

        // Configurar el contenedor principal y colocar el ScrollPane, el TextField y el
        // botón
        chatContainer.getChildren().add(mainContainer);
    }

    @FXML
    public void actionEvent(ActionEvent e) throws IOException {
        Object evt = e.getSource();

        // comprobar si ha decidido apostar o es un cagon

        if (evt.equals(btnEnviar)) {
            if (!inputField.getText().equals("")) {
                GestionPartida.enviarChat(inputField.getText());
                inputField.clear();
            }
        }
    }

    private void enviarMensaje() {
        if (!inputField.getText().equals("")) {
            GestionPartida.enviarChat(inputField.getText());
            inputField.clear();
        }
    }

    // Método para actualizar todos los mensajes almacenados hasta el momento
    // en el ListView de mensajes del chat
    public void actualizarChat(ArrayList<String> mensajes) {
        // Añadir a nuestro listView todo el contenido de mensajes
        chatMessages.clear();
        for (int i = 0; i < mensajes.size(); i++) {
            String nuevoMensaje = GestionPartida.Usuariochat.get(i) + ": " + mensajes.get(i);
            chatMessages.add(nuevoMensaje);
        }
    }
}
