package hse.ce.jameskok.jigsawmultiplayer.server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for server GUI.
 */
public final class Controller implements Initializable {
    private Server server;
    @FXML
    private Spinner<Integer> timeLimitSpinner;

    @FXML
    private Label statusLabel;

    @FXML
    private CheckBox playersCheckBox;

    @FXML
    private Button stopButton;

    @FXML
    private Button startButton;

    @FXML
    private TextField portField;

    @FXML
    private void onStartButtonClick() {
        startServer();
    }

    @FXML
    private void onStopButtonClick() {
        stopServer();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 300, 120, 5);
        timeLimitSpinner.setValueFactory(valueFactory);
    }

    /**
     * Start server.
     */
    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText());
            server = new Server(port, playersCheckBox.isSelected(), timeLimitSpinner.getValue());
        } catch (NumberFormatException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText("Укажите корректный порт");
                alert.setContentText("Натуральное число.");
                alert.show();
                portField.setText("5000");
            });
            return;
        } catch (Exception e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setContentText("Не удалось открыть сервер на указанном порту");
                alert.setHeaderText("Выберете другой порт");
                alert.show();
                portField.setText("5000");
            });
            return;
        }
        timeLimitSpinner.setDisable(true);
        playersCheckBox.setDisable(true);
        startButton.setDisable(true);
        stopButton.setDisable(false);
        portField.setDisable(true);
        statusLabel.setText("Статус сервера: работает");
    }

    /**
     * Stop server.
     */
    void stopServer() {
        timeLimitSpinner.setDisable(false);
        playersCheckBox.setDisable(false);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        portField.setDisable(false);
        statusLabel.setText("Статус сервера: выключен");
        server.closeServer();
    }
}
