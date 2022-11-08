package hse.ce.jameskok.jigsawmultiplayer.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Figure shape generator.
 * Note: all possible shape shapes are stored in the json-file: figures.json
 */
public final class FigureGenerator {
    private static final int figuresNumber = 31;
    private static final String pathToFigures = "src/main/resources/figures.json";
    private static final Random rnd = new Random();
    private static int[][][] figures;

    static {
        try {
            figures = (new ObjectMapper()).readValue(new File(pathToFigures), int[][][].class);
        } catch (IOException e) {
            System.out.print("Failed to read file with figures! \n" +
                    "Check project integrity");
            System.exit(-1);
        }
    }

    /**
     * Generate random figure.
     *
     * @return generated figure
     */
    public static Shape getFigure() {
        int idx = rnd.nextInt(0, figuresNumber);
        return new Shape(figures[idx]);
    }

    private FigureGenerator() {
    }
}

