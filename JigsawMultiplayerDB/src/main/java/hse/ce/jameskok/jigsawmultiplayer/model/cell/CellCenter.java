package hse.ce.jameskok.jigsawmultiplayer.model.cell;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Center of Figure. Required for position detection.
 */
final class CellCenter extends Rectangle {
    private static final int xSize = 1, ySize = 1;

    /**
     * Create instance.
     *
     * @param x         x-position in Figure [0, 9)
     * @param y         y-position in Figure [0, 9)
     * @param xCeilSize x-size of Cell
     * @param yCeilSize y-size of Cell
     */
    CellCenter(int x, int y, int xCeilSize, int yCeilSize) {
        super(xSize, ySize);
        setFill(Paint.valueOf("CORNFLOWERBLUE"));
        setLayoutX((x + 0.5) * xCeilSize);
        setLayoutY((y + 0.5) * yCeilSize);
    }
}