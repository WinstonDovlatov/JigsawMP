package hse.ce.jameskok.jigsawmultiplayer.model.cell;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Displayed part of Cell.
 */
final class CellBody extends Rectangle {

    /**
     * Create instance.
     *
     * @param x      x-position in Figure [0, 9)
     * @param y      y-position in Figure [0, 9)
     * @param x_size x-size of Cell
     * @param y_size y-size of Cell
     */
    CellBody(int x, int y, int x_size, int y_size) {
        super(x_size, y_size);
        setFill(Paint.valueOf("CORNFLOWERBLUE"));
        setStroke(Paint.valueOf("black"));
        setStrokeWidth(1);

        setLayoutX(x * x_size);
        setLayoutY(y * y_size);
    }
}