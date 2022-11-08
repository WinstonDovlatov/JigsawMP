package hse.ce.jameskok.jigsawmultiplayer.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Server GUI.
 */
public final class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 250);
        stage.setResizable(false);
        stage.setTitle("Jigsaw Game Server");
        stage.setScene(scene);
        stage.show();
    }

    public void startServerApplication(){
        launch();
    }
}
