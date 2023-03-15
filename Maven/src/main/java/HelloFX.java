// Ejecutar con mvn clean javafx:run
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URISyntaxException;

public class HelloFX extends Application {
    private String text1;
    private String text2;

    @Override
    public void start(Stage stage) throws InterruptedException {

        // Iniciar la conexion
            try {
                ConexionServidor.partida();
            } catch (URISyntaxException e1) {
                System.err.println("URI no válida: " + e1.getMessage());
            } catch (InterruptedException ex) {
                System.err.println("Interrumpida excepcion (no se que es): " + ex.getMessage());
            }
        
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Button b = new Button("Enviar mensaje al servidor WebSocket");
        stage.setTitle("Ejemplo de JavaFX");

        // Crear los campos de texto y el botón
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        b.setOnAction(e -> {
        //     // Que hace el boton
            text1 = textField1.getText();
            text2 = textField2.getText();
            System.out.println("Text1: " + text1);
            System.out.println("Text2: " + text2);
            ConexionServidor.enviarMensaje("registrarse," + text1 + "," + text2);
        // Parte correspondiente a crear la conexion con webSockets        
            // try {
            //     ConexionServidor.partida();
            // } catch (URISyntaxException e1) {
            //     System.err.println("URI no válida: " + e1.getMessage());
            // } catch (InterruptedException ex) {
            //     System.err.println("Interrumpida excepcion (no se que es): " + ex.getMessage());
            // }
        //     // Esto esta mal 
        //     // WebSocketClient client = new WebSocketClient();
        //     // client.sendMessage("Hola, servidor WebSocket!");
        });
        
        // Menu para guardar campos

        // Crear un grid para organizar los elementos
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        // Añadir los elementos al grid
        gridPane.add(textField1, 0, 0);
        gridPane.add(textField2, 0, 1);
        gridPane.add(b, 1, 0);

        // Crear una escena y añadir el grid
        Scene scene = new Scene(gridPane, 300, 200);
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) {
        launch();
    }

}
