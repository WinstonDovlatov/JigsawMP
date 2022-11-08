package hse.ce.jameskok.jigsawmultiplayer.client;

import hse.ce.jameskok.jigsawmultiplayer.GameResult;
import hse.ce.jameskok.jigsawmultiplayer.Message;
import hse.ce.jameskok.jigsawmultiplayer.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Client side of game.
 */
public final class Client {
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final String name;
    private static Client client;
    private GameController gameController;
    private RatingController ratingController;

    /**
     * Create the only one instance of client.
     *
     * @param address address to connect
     * @param port    port to connect
     * @param name    name of user
     * @throws IOException occurs when a connection cannot be established
     */
    static void createClient(String address, int port, String name) throws IOException {
        client = new Client(address, port, name);
        client.sendName();
    }

    /**
     * Get the only ont instance of client.
     *
     * @return instance of client
     */
    static Client getClient() {
        return client;
    }

    /**
     * Request figure from server.
     */
    void requestFigure() {
        sendMessage(new Message(MessageType.GET_FIGURE, null));
    }

    /**
     * Request top-10 games from server.
     */
    void requestRating(){
        sendMessage(new Message(MessageType.GET_RATING, null));
    }

    /**
     * Send match result to server.
     *
     * @param duration game duration in seconds
     * @param score    quantity of turns
     */
    void sendResult(int duration, int score) {
        sendMessage(new Message(MessageType.SEND_RESULT, new GameResult(name, LocalDateTime.now(), duration, score)));
    }

    /**
     * Add games to rating table.
     * @param results array of results
     */
    void fillRatingTable(ArrayList<GameResult> results){
        ratingController.setData(results);
    }

    /**
     * Notify server about readiness to play again.
     */
    void sendRestart() {
        sendMessage(new Message(MessageType.RESTART, null));
    }

    /**
     * Close connection with server.
     */
    void closeClient() {
        gameController.disconnect();
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get message from server.
     *
     * @return message from server
     * @throws IOException            occurs when there are problems with server
     * @throws ClassNotFoundException occurs when server sent something weird
     */
    Message readMessage() throws IOException, ClassNotFoundException {
        return (Message) inputStream.readObject();
    }

    void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    void setRatingController(RatingController ratingController){
        this.ratingController = ratingController;
    }

    /**
     * Turn on client.
     */
    void startCommunication() {
        new ServerHandler(this, gameController).start();
    }

    /**
     * Get name of user.
     *
     * @return username
     */
    String getName() {
        return name;
    }


    /**
     * Construct client.
     *
     * @param address address to connect
     * @param port    port to connect
     * @param name    player name
     * @throws IOException occurs if can't connect by provided address and port.
     */
    private Client(String address, int port, String name) throws IOException {
        this.name = name;
        this.socket = new Socket(address, port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Send player name to server.
     */
    private void sendName() {
        sendMessage(new Message(MessageType.SEND_NAME, this.name));
    }

    /**
     * Send message to server.
     *
     * @param message message to server.
     */
    private void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (Exception e) {
            closeClient();
        }
    }
}
