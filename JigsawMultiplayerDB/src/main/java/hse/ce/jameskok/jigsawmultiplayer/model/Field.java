package hse.ce.jameskok.jigsawmultiplayer.model;

/**
 * Game field.
 */
public final class Field {
    private static final int xSize = 9;
    private static final int ySize = 9;
    private final boolean[][] field = new boolean[xSize][ySize];

    /**
     * Clear field.
     */
    public void clear() {
        for (int i = 0; i < xSize; ++i) {
            for (int j = 0; j < ySize; ++j) {
                field[i][j] = false;
            }
        }
    }

    /**
     * Make cell played(painted over).
     *
     * @param x x-position [0, 9)
     * @param y y-position [0, 9)
     */
    public void insertCell(int x, int y) {
        field[x][y] = true;
    }

    /**
     * Checks if cell available for placement.
     *
     * @param x x-position [0, 9)
     * @param y y-position [0, 9)
     * @return true if free, false otherwise
     */
    public boolean isCellFree(int x, int y) {
        return !field[x][y];
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }
}
