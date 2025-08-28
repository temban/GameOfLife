package com.studely.gameoflife;

/**
 * Launches the Game of Life UI.
 * Optional args: rows cols (e.g., 40 60)
 */
public class GameOfLife {
    public static void main(String[] args) {
        int rows = 40;
        int cols = 60;
        if (args.length >= 2) {
            try {
                rows = Integer.parseInt(args[0]);
                cols = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) { }
        }
        final int fRows = rows;
        final int fCols = cols;
        javax.swing.SwingUtilities.invokeLater(() -> {
            Grid grid = new Grid(fRows, fCols);
            new GameOfLifeUI(grid);
        });
    }
}
