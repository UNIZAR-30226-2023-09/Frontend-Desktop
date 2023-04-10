import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    private static boolean grafico = false;
    private static boolean verbose = false;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // True para utilizar interfaz grafica
        ConexionServidor.iniciar(grafico, verbose);

        scene = new Scene(loadFXML("MainView"), 1500, 900);

        // Agrega un evento para cuando se cierre cualquier ventana
        // Agrega un evento para cuando se cierre cualquier ventana
        // scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
        // public void handle(WindowEvent we) {
        // System.out.println("Se ha cerrado una ventana");
        // ConexionServidor.cerrarConexion();
        // System.exit(0);
        // }
        // });

        stage.setScene(scene);
        // stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        ConexionServidor.cerrarConexion();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}