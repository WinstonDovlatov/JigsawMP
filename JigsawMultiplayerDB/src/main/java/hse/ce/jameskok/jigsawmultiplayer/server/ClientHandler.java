package hse.ce.jameskok.jigsawmultiplayer.server;


import hse.ce.jameskok.jigsawmultiplayer.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client message handler on server
 */
final class ClientHandler extends Thread {
    private final Socket socket;
    private final Server server;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private int turn = 0;
    private GameResult result;
    private String name;
    private PlayerStatus status;

    /**
     * Create client handler.
     *
     * @param socket current socket
     * @param server current server
     * @throws IOException occurs when a connection cannot be established
     */
    ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        while (!interrupted()) {
            try {
                processRequest((Message) inputStream.readObject());
            } catch (Exception e) {
                break;
            }
        }
        disconnectClient();
    }

    /**
     * Report client about game start.
     *
     * @param enemyName name of enemy of this client
     */
    void startGame(String enemyName) {
        status = PlayerStatus.IN_GAME;
        try {
            outputStream.writeObject(new Message(MessageType.ENEMY_NAME, enemyName));
            outputStream.writeObject(new Message(MessageType.START_GAME, null));
        } catch (Exception e) {
            disconnectClient();
        }
    }

    /**
     * Report client about his auto win.
     */
    void sendAutoWin() {
        status = PlayerStatus.WAIT;
        try {
            outputStream.writeObject(new Message(MessageType.AUTO_WIN, null));
        } catch (Exception e) {
            disconnectClient();
        }
    }

    /**
     * Report client about his result.
     *
     * @param result result
     */
    void sendResult(ResultType result) {
        try {
            outputStream.writeObject(new Message(MessageType.RESULT, result));
        } catch (Exception e) {
            disconnectClient();
        }
    }

    String getPlayerName() {
        return name;
    }

    GameResult getResult(){
        return result;
    }

    PlayerStatus getStatus() {
        return status;
    }

    /**
     * Disconnect client from server.
     */
    private void disconnectClient() {
        server.getClientHandlers().remove(this);
        server.setAutoWin();
        try {
            outputStream.writeObject(new Message(MessageType.CLOSE, null));
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception ignored) {

        }
    }

    /**
     * Handle message from client.
     *
     * @param message message from client
     */
    private void processRequest(Message message) {
        try {
            switch (message.getType()) {
                case SEND_NAME -> {
                    this.name = (String) message.getData();
                    outputStream.writeObject(new Message(MessageType.TIME_LIMIT, server.getTimeLimit()));
                    turn = 0;
                    status = PlayerStatus.READY;
                    server.startGame();
                }
                case GET_FIGURE -> outputStream.writeObject(new Message(MessageType.FIGURE, server.getShape(turn++)));
                case SEND_RESULT -> {
                    result = (GameResult) message.getData();
                    status = PlayerStatus.WAIT;
                    server.dumbToDB(result);
                    server.processResults();
                }
                case RESTART -> {
                    turn = 0;
                    status = PlayerStatus.READY;
                    server.restartGame();
                }
                case GET_RATING -> outputStream.writeObject(new Message(MessageType.RATING, server.getBestPlayers()));
            }
        } catch (Exception e) {
            disconnectClient();
        }
    }
}
