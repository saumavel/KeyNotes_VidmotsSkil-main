package padman.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ApplicationUI extends Application {


    /**
     * The mainStage field holds the primary stage of the UI, which is initialized in the start() method.
     */
    private static Stage mainStage;
    /**
     * The getMainStage() method returns the primary stage of the UI.
     * @return the primary stage of the UI
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * The start() method initializes and displays the UI by loading the keys-view.fxml file, setting the scene, and showing the stage.
     * It also sets up event handlers for key presses and releases.
     * @param stage the primary stage of the UI
     * @throws IOException if the keys-view.fxml file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("keys-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 310);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("KeyNotes");
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        KeysUI controller = fxmlLoader.getController();
        scene.setOnKeyPressed(controller::onKeyPressed);
        scene.setOnKeyReleased(controller::onKeyReleased);
        stage.show();
    }

    /**
     * The main() method launches the JavaFX application by calling the launch() method of the Application class.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}