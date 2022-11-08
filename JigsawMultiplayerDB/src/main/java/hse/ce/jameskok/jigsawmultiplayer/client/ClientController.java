package hse.ce.jameskok.jigsawmultiplayer.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Welcome-window controller.
 */
public final class ClientController implements Initializable {
    private Stage stage;
    @FXML
    Button startButton;

    @FXML
    TextField nameField, addressField, portField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.textProperty().addListener(
                (observable, oldValue, newValue) -> validateName());
        portField.textProperty().addListener(
                (observable, oldValue, newValue) -> validateName());
        addressField.textProperty().addListener(
                (observable, oldValue, newValue) -> validateName());
        nameField.textProperty().addListener(
                (observable,oldValue,newValue)-> {
                    if(newValue.length() > 20) nameField.setText(oldValue);
                }
        );
    }

    @FXML
    private void initGame() throws IOException {
        try {
            int port = Integer.parseInt(portField.getText());
            Client.createClient(addressField.getText(), port, nameField.getText());
        } catch (NumberFormatException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText("Укажите корректный порт сервера");
                alert.setContentText("Натуральное число.");
                alert.showAndWait();
                portField.setText("5000");
            });
            return;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Server is not available now");
            alert.setContentText("""
                    В настоящее время сервер не доступен для подключения.
                    Попробуйте позже, проверьте корректность адреса и порта.
                    """);
            alert.setHeaderText("Не удалось подключиться к серверу");
            alert.showAndWait();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("client-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 640);
        stage.setResizable(false);
        stage.setTitle("Jigsaw Game");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(we -> Client.getClient().closeClient());
        Client.getClient().setGameController(fxmlLoader.getController());
        Client.getClient().startCommunication();
    }

    /**
     * Check that the fields are not empty.
     */
    void validateName() {
        startButton.setDisable(nameField.getText().equals("") ||
                portField.getText().equals("") || addressField.getText().equals(""));
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
