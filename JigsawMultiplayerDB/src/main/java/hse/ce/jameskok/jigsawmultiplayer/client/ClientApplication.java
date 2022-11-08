package hse.ce.jameskok.jigsawmultiplayer.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Start GUI.
 */
public final class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 250);
        stage.setResizable(false);
        stage.setTitle("Welcome in Jigsaw Game!");
        stage.setScene(scene);
        ((ClientController) fxmlLoader.getController()).setStage(stage);
        stage.show();
    }

    public void startClientApplication(){
        launch();
    }
}
