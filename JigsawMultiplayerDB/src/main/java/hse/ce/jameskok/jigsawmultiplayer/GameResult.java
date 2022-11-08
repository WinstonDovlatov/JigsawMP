package hse.ce.jameskok.jigsawmultiplayer;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Container for game results from client.
 */
public record GameResult(String username, LocalDateTime date, int duration, int score) implements Serializable {

    public String getFormattedString(){
        return String.format("'%s', '%s', %d, %d", username, Timestamp.valueOf(date), score, duration);
    }

    public String getUsername(){
        return username;
    }

    public LocalDateTime getDate(){
        return date.truncatedTo(ChronoUnit.SECONDS);
    }

    public int getScore(){
        return score;
    }

    public int getDuration(){
        return duration;
    }
}
