package hse.ce.jameskok.jigsawmultiplayer.model;

import hse.ce.jameskok.jigsawmultiplayer.client.GameController;
import hse.ce.jameskok.jigsawmultiplayer.model.cell.*;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Game figure that can be placed on the field.
 */
public final class GameFigure extends Group {
    static class Shift {
        double xShift, yShift;
    }

    static class Point {
        int i, j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private final Shift shift = new Shift();
    GameController controller;

    /**
     * Create instance.
     *
     * @param shape      Shape of figure
     * @param controller game controller
     */
    public GameFigure(Shape shape, GameController controller) {
        this.controller = controller;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (shape.getShape()[i][j] == 1) {
                    this.getChildren().add(new Cell(i, j));
                }
            }
        }

        setOnMousePressed(e -> {
            shift.xShift = getLayoutX() - e.getSceneX();
            shift.yShift = getLayoutY() - e.getSceneY();
        });

        setOnMouseDragged(mouseEvent -> {
            setLayoutX(mouseEvent.getSceneX() + shift.xShift);
            setLayoutY(mouseEvent.getSceneY() + shift.yShift);
        });

        setOnMouseReleased(mouseEvent -> placeFigure());
    }

    /**
     * Checks if the current Сeil can be placed somewhere. if possible, saves the coordinates of the field cell.
     *
     * @param cellCenterBounds figure detection limits. The boundaries of its center
     * @param points           list of points where figure cells can be placed
     * @return true if there is a cell to place, zero otherwise
     */
    boolean isCorrectCeil(Bounds cellCenterBounds, ArrayList<Point> points) {
        Field field = controller.getGameField();
        Pane[][] paneArr = controller.getPaneArr();
        for (int i = 0; i < field.getXSize(); ++i) {
            for (int j = 0; j < field.getYSize(); ++j) {
                Bounds fieldBounds = paneArr[i][j].localToScene(paneArr[i][j].getLayoutBounds());
                if (cellCenterBounds.intersects(fieldBounds)) {
                    if (field.isCellFree(i, j)) {
                        points.add(new Point(i, j));
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Place Figure in field if it is possible.
     */
    void placeFigure() {
        ArrayList<Point> points = new ArrayList<>();
        for (var ceil : this.getChildren()) {
            Cell curCell = (Cell) ceil;
            Bounds curCeilBounds = curCell.getCenterBounds();
            if (!isCorrectCeil(curCeilBounds, points)) {
                return;
            }
        }

        for (Point pt : points) {
            controller.getGameField().insertCell(pt.i, pt.j);
            controller.getPaneArr()[pt.i][pt.j].setStyle("-fx-background-color: #4646ec; -fx-border-color: black");
        }
        controller.nextTurn();
    }
}
