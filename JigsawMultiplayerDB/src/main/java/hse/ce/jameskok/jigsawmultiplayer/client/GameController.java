package hse.ce.jameskok.jigsawmultiplayer.client;

import hse.ce.jameskok.jigsawmultiplayer.ResultType;
import hse.ce.jameskok.jigsawmultiplayer.model.GameFigure;
import hse.ce.jameskok.jigsawmultiplayer.model.Field;
import hse.ce.jameskok.jigsawmultiplayer.model.Shape;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Game controller.
 */
public final class GameController implements Initializable {
    private final Pane[][] paneArr = new Pane[9][9];
    private Field gameField;
    private GameFigure currentFigure;
    private int turnsCounter;
    private Timer timer;
    private int timeLimit;
    private boolean isCooperative;
    @FXML
    private Pane figurePane;

    @FXML
    private GridPane field;

    @FXML
    private Label turnsLabel, timerLabel, enemyLabel, myNameLabel, statusLabel;

    @FXML
    private Button playAgainButton, endButton;

    /**
     * Perform realization of restarting the game.
     */
    @FXML
    private void playAgain() {
        playAgainButton.setDisable(true);
        clearGame();
        Client.getClient().sendRestart();
    }

    /**
     * Finish the game.
     */
    @FXML
    private void endGame() {
        endButton.setDisable(true);
        timer.stop();
        deleteGameFigure();
        Client.getClient().sendResult(timer.getTime(), turnsCounter);
        statusLabel.setText("Ваша игра окончена." + (isCooperative ? " Ожидание второго игока." : ""));
        if (!isCooperative) {
            playAgainButton.setDisable(false);
        }
    }

    /**
     * Display rating window.
     *
     * @throws IOException occurs when can't read resource file
     */
    @FXML
    private void showRating() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("rating-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Rating");
        stage.setScene(scene);
        stage.show();
        Client.getClient().setRatingController(fxmlLoader.getController());
        Client.getClient().requestRating();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameField = new Field();
        turnsCounter = 0;
        for (int i = 0; i < gameField.getXSize(); ++i) {
            for (int j = 0; j < gameField.getYSize(); ++j) {
                Pane pane = new Pane();
                pane.setMaxSize(60, 60);
                field.add(pane, i, j);
                paneArr[i][j] = pane;
            }
        }
        myNameLabel.setText("Мое имя: " + Client.getClient().getName());
        playAgainButton.setDisable(true);
    }

    public Field getGameField() {
        return gameField;
    }

    public Pane[][] getPaneArr() {
        return paneArr;
    }

    /**
     * Produce next turn.
     */
    public void nextTurn() {
        turnsCounter++;
        deleteGameFigure();
        Client.getClient().requestFigure();
        setTurns();
    }

    /**
     * Clear game field and timer.
     */
    void clearGame() {
        gameField.clear();
        deleteGameFigure();
        turnsCounter = 0;
        setTurns();
        for (int i = 0; i < gameField.getXSize(); ++i) {
            for (int j = 0; j < gameField.getYSize(); ++j) {
                paneArr[i][j].setStyle("");
                field.setGridLinesVisible(true);
            }
        }
    }

    /**
     * Spawn figure in GUI.
     *
     * @param shape shape of figure
     */
    void createGameFigure(Shape shape) {
        Platform.runLater(() -> {
            GameFigure figure = new GameFigure(shape, this);
            figurePane.getChildren().add(figure);
            currentFigure = figure;
        });
    }

    /**
     * Set enemy name on game GUI.
     *
     * @param name enemy name
     */
    void setEnemyName(String name) {
        if (name == null) {
            Platform.runLater(() -> enemyLabel.setText("Одиночный режим."));
            isCooperative = false;
        } else {
            Platform.runLater(() -> enemyLabel.setText("Имя противника: " + name));
            isCooperative = true;
        }

    }

    /**
     * Save time limit for one game.
     *
     * @param seconds time limit in seconds
     */
    void setTimeLimit(int seconds) {
        timeLimit = seconds;
    }

    /**
     * Start game.
     */
    void startGame() {
        Platform.runLater(() -> {
            statusLabel.setText("");
            endButton.setDisable(false);
            timer = new Timer(this::setTime);
            Client.getClient().requestFigure();
        });
    }

    /**
     * Show user his result(win/lose/tie).
     *
     * @param result outcome
     */
    void showResult(ResultType result) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Result");
            alert.setContentText(null);
            switch (result) {
                case WIN -> alert.setHeaderText("Вы одержали победу!");
                case LOSE -> alert.setHeaderText("Вы проиграли в этот раз.");
                case TIE -> alert.setHeaderText("Ничья!");
            }
            alert.show();
            playAgainButton.setDisable(false);
        });
    }

    /**
     * Perform disconnecting from server.
     */
    void disconnect() {
        Platform.runLater(() -> {
            timer.stop();
            deleteGameFigure();
            statusLabel.setText("Вы были отключены от сервера.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Disconnect");
            alert.setContentText("Нет соединения с сервером");
            alert.setHeaderText(null);
            alert.show();
        });
    }

    /**
     * Perform realization of disconnecting enemy.
     */
    void autoWin() {
        Platform.runLater(() -> {
            endGame();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Auto win");
            alert.setContentText("Победа!");
            alert.setHeaderText("Ваш противник отключился. Вы победили!");
            alert.show();
            playAgainButton.setDisable(false);
        });

    }

    /**
     * Update turns counter.
     */
    private void setTurns() {
        turnsLabel.setText("Сделано ходов " + turnsCounter);
    }

    /**
     * Perform realization of time out.
     */
    private void outOfTime() {
        endGame();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Out of time");
        alert.setContentText("Время вышло!");
        alert.setHeaderText(null);
        alert.show();
    }

    /**
     * Update timer.
     *
     * @param seconds turn duration at this moment in seconds
     */
    private void setTime(int seconds) {
        int deltaSeconds = timeLimit - seconds;
        if (deltaSeconds <= 0) {
            outOfTime();
        }
        int minutesLeft = deltaSeconds / 60;
        int secondsLeft = deltaSeconds % 60;

        timerLabel.setText(String.format("Времени осталось: %dм %dc", minutesLeft, secondsLeft));
    }

    /**
     * Delete figure from GUI.
     */
    private void deleteGameFigure() {
        if (currentFigure != null) {
            figurePane.getChildren().remove(currentFigure);
        }
    }
}


