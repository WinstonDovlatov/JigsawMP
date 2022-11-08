package hse.ce.jameskok.jigsawmultiplayer.client;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Game timer.
 */
final class Timer {
    private int seconds;
    Consumer<Integer> update;
    Timeline timeline;

    public Timer(Consumer<Integer> updateTime) {
        update = updateTime;
        seconds = -1;
        startTimer();
    }

    public int getTime() {
        return seconds;
    }

    public void stop() {
        timeline.stop();
    }

    private void startTimer() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    seconds++;
                    update.accept(seconds);
                }), new KeyFrame(Duration.millis(1000)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
