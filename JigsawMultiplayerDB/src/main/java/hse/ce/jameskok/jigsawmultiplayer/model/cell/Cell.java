package hse.ce.jameskok.jigsawmultiplayer.model.cell;

import javafx.geometry.Bounds;
import javafx.scene.Group;

/**
 * One cell of Figure.
 */
public final class Cell extends Group {
    private static final int xSize = 60, ySize = 60;
    private final CellCenter center;

    /**
     * Create instance of Cell.
     *
     * @param x x-position in Figure [0, 9)
     * @param y y-position in Figure [0, 9)
     */
    public Cell(int x, int y) {
        CellBody body = new CellBody(x, y, xSize, ySize);
        center = new CellCenter(x, y, xSize, ySize);
        this.getChildren().add(body);
        this.getChildren().add(center);
    }

    /**
     * Method to detect position of Ceil. Bounds of center of figure taken for Ceil position.
     *
     * @return bounds of figure center
     */
    public Bounds getCenterBounds() {
        return center.localToScene(center.getLayoutBounds());
    }
}







