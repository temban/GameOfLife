package com.studely.gameoflife;

import java.util.Random;

/**
 * Core grid logic for Conway's Game of Life.
 */
public class Grid {
    private final int rows;
    private final int cols;
    private boolean[][] current;
    private boolean[][] next;

    public Grid(int rows, int cols) {
        if (rows <= 0 || cols <= 0) throw new IllegalArgumentException("rows/cols must be > 0");
        this.rows = rows;
        this.cols = cols;
        this.current = new boolean[rows][cols];
        this.next = new boolean[rows][cols];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public boolean isAlive(int r, int c) {
        checkBounds(r, c);
        return current[r][c];
    }

    public void setAlive(int r, int c, boolean alive) {
        checkBounds(r, c);
        current[r][c] = alive;
    }

    public void toggle(int r, int c) {
        setAlive(r, c, !isAlive(r, c));
    }

    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                current[r][c] = false;
            }
        }
    }

    public void randomize(double aliveProbability) {
        Random rnd = new Random();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                current[r][c] = rnd.nextDouble() < aliveProbability;
            }
        }
    }

    /**
     * Advance one generation with standard Conway rules (no wrapping).
     */
    public void step() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int n = neighbors(r, c);
                if (current[r][c]) {
                    // Alive cell survives with 2 or 3 neighbors
                    next[r][c] = (n == 2 || n == 3);
                } else {
                    // Dead cell becomes alive with exactly 3 neighbors
                    next[r][c] = (n == 3);
                }
            }
        }
        // swap buffers
        boolean[][] tmp = current;
        current = next;
        next = tmp;
    }

    private int neighbors(int r, int c) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int rr = r + dr;
                int cc = c + dc;
                if (rr >= 0 && rr < rows && cc >= 0 && cc < cols && current[rr][cc]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void checkBounds(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            throw new IndexOutOfBoundsException("r=" + r + ", c=" + c + " outside " + rows + "x" + cols);
        }
    }
}
