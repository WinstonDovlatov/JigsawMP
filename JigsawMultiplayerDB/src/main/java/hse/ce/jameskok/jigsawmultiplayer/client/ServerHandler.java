package hse.ce.jameskok.jigsawmultiplayer.client;

import hse.ce.jameskok.jigsawmultiplayer.GameResult;
import hse.ce.jameskok.jigsawmultiplayer.Message;
import hse.ce.jameskok.jigsawmultiplayer.ResultType;
import hse.ce.jameskok.jigsawmultiplayer.model.Shape;

import java.util.ArrayList;

/**
 * Server message handler.
 */
final class ServerHandler extends Thread {
    private final Client client;
    private final GameController gameController;

    ServerHandler(Client client, GameController controller) {
        this.client = client;
        this.gameController = controller;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                processMessage(client.readMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Processes received message.
     *
     * @param message message from server
     */
    void processMessage(Message message) {
        switch (message.getType()) {
            case ENEMY_NAME -> gameController.setEnemyName((String) message.getData());
            case TIME_LIMIT -> gameController.setTimeLimit((int) message.getData());
            case START_GAME -> gameController.startGame();
            case FIGURE -> gameController.createGameFigure((Shape) message.getData());
            case RESULT -> gameController.showResult((ResultType) message.getData());
            case CLOSE -> Client.getClient().closeClient();
            case AUTO_WIN -> gameController.autoWin();
            case RATING -> Client.getClient().fillRatingTable((ArrayList<GameResult>) message.getData());
        }
    }

}
