package hse.ce.jameskok.jigsawmultiplayer.model;

import java.io.Serializable;

import static java.lang.System.arraycopy;

/**
 * Shape of figure.
 */
public final class Shape implements Serializable {
    private final int[][] shape;

    Shape(int[][] input) {
        shape = new int[3][3];
        for (int i = 0; i < 3; ++i) {
            arraycopy(input[i], 0, shape[i], 0, 3);
        }
    }

    public int[][] getShape(){
        return shape;
    }
}
