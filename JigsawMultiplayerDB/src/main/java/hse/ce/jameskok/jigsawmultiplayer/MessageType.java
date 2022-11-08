package hse.ce.jameskok.jigsawmultiplayer;

/**
 * Type of message from server or from client.
 */
public enum MessageType {
    GET_FIGURE, FIGURE, SEND_NAME, TIME_LIMIT, ENEMY_NAME, START_GAME, SEND_RESULT, RESULT, AUTO_WIN,
    CLOSE, RESTART, GET_RATING, RATING
}
