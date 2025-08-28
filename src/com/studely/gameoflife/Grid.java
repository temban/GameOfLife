// File: Grid.java
package com.studely.gameoflife;

import java.util.Random;

/**
 * Represents the grid of cells for the Game of Life simulation.
 * Handles the core logic and rules of the game.
 */
public class Grid {
    private final int width;
    private final int height;
    private CellState[][] currentGrid;
    private CellState[][] nextGrid;
    private final Random random;
    
    /**
     * Creates a new grid with the specified dimensions.
     * 
     * @param width the width of the grid
     * @param height the height of the grid
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.currentGrid = new CellState[height][width];
        this.nextGrid = new CellState[height][width];
        this.random = new Random();
        
        // Initialize grids with dead cells
        clear();
    }
    
    /**
     * Advances the simulation by one generation according to the rules of the Game of Life.
     */
    public void nextGeneration() {
        // Apply rules to each cell
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int liveNeighbors = countLiveNeighbors(x, y);
                
                // Apply Conway's Game of Life rules
                if (currentGrid[y][x] == CellState.ALIVE) {
                    // Rule 1 & 2: Survival or Death
                    nextGrid[y][x] = (liveNeighbors == 2 || liveNeighbors == 3) 
                            ? CellState.ALIVE : CellState.DEAD;
                } else {
                    // Rule 4: Birth
                    nextGrid[y][x] = (liveNeighbors == 3) 
                            ? CellState.ALIVE : CellState.DEAD;
                }
            }
        }
        
        // Swap grids for the next iteration
        CellState[][] temp = currentGrid;
        currentGrid = nextGrid;
        nextGrid = temp;
    }
    
    /**
     * Counts the number of live neighbors for a given cell.
     * 
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the number of live neighbors
     */
    private int countLiveNeighbors(int x, int y) {
        int count = 0;
        
        // Check all 8 neighbors with toroidal (wrapping) boundaries
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue; // Skip the cell itself
                
                int nx = (x + dx + width) % width;
                int ny = (y + dy + height) % height;
                
                if (currentGrid[ny][nx] == CellState.ALIVE) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * Sets the state of a specific cell.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param state the new state of the cell
     */
    public void setCell(int x, int y, CellState state) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            currentGrid[y][x] = state;
        }
    }
    
    /**
     * Gets the state of a specific cell.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the state of the cell
     */
    public CellState getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return currentGrid[y][x];
        }
        return CellState.DEAD;
    }
    
    /**
     * Clears the grid (sets all cells to dead).
     */
    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                currentGrid[y][x] = CellState.DEAD;
                nextGrid[y][x] = CellState.DEAD;
            }
        }
    }
    
    /**
     * Randomizes the grid with the specified probability of cells being alive.
     * 
     * @param aliveProbability the probability that a cell will be alive (0.0 to 1.0)
     */
    public void randomize(double aliveProbability) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                currentGrid[y][x] = random.nextDouble() < aliveProbability ? CellState.ALIVE : CellState.DEAD;
            }
        }
    }
    
    /**
     * Gets the width of the grid.
     * 
     * @return the width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the grid.
     * 
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Creates a glider pattern at the specified position.
     * 
     * @param x the x-coordinate of the top-left of the pattern
     * @param y the y-coordinate of the top-left of the pattern
     */
    public void createGlider(int x, int y) {
        setCell(x + 1, y, CellState.ALIVE);
        setCell(x + 2, y + 1, CellState.ALIVE);
        setCell(x, y + 2, CellState.ALIVE);
        setCell(x + 1, y + 2, CellState.ALIVE);
        setCell(x + 2, y + 2, CellState.ALIVE);
    }
    
    /**
     * Creates a blinker pattern at the specified position.
     * 
     * @param x the x-coordinate of the top-left of the pattern
     * @param y the y-coordinate of the top-left of the pattern
     */
    public void createBlinker(int x, int y) {
        setCell(x + 1, y, CellState.ALIVE);
        setCell(x + 1, y + 1, CellState.ALIVE);
        setCell(x + 1, y + 2, CellState.ALIVE);
    }
    
    /**
     * Creates a pulsar pattern at the specified position.
     * 
     * @param x the x-coordinate of the top-left of the pattern
     * @param y the y-coordinate of the top-left of the pattern
     */
    public void createPulsar(int x, int y) {
        // Top left block
        setCell(x + 2, y, CellState.ALIVE);
        setCell(x + 3, y, CellState.ALIVE);
        setCell(x + 4, y, CellState.ALIVE);
        setCell(x, y + 2, CellState.ALIVE);
        setCell(x, y + 3, CellState.ALIVE);
        setCell(x, y + 4, CellState.ALIVE);
        
        // Top right block
        setCell(x + 8, y + 2, CellState.ALIVE);
        setCell(x + 8, y + 3, CellState.ALIVE);
        setCell(x + 8, y + 4, CellState.ALIVE);
        setCell(x + 10, y, CellState.ALIVE);
        setCell(x + 11, y, CellState.ALIVE);
        setCell(x + 12, y, CellState.ALIVE);
        
        // Bottom left block
        setCell(x + 2, y + 6, CellState.ALIVE);
        setCell(x + 3, y + 6, CellState.ALIVE);
        setCell(x + 4, y + 6, CellState.ALIVE);
        setCell(x, y + 8, CellState.ALIVE);
        setCell(x, y + 9, CellState.ALIVE);
        setCell(x, y + 10, CellState.ALIVE);
        
        // Bottom right block
        setCell(x + 8, y + 8, CellState.ALIVE);
        setCell(x + 8, y + 9, CellState.ALIVE);
        setCell(x + 8, y + 10, CellState.ALIVE);
        setCell(x + 10, y + 6, CellState.ALIVE);
        setCell(x + 11, y + 6, CellState.ALIVE);
        setCell(x + 12, y + 6, CellState.ALIVE);
        
        // Additional pulsar cells
        setCell(x + 6, y + 2, CellState.ALIVE);
        setCell(x + 6, y + 3, CellState.ALIVE);
        setCell(x + 6, y + 4, CellState.ALIVE);
        setCell(x + 6, y + 8, CellState.ALIVE);
        setCell(x + 6, y + 9, CellState.ALIVE);
        setCell(x + 6, y + 10, CellState.ALIVE);
    }
}