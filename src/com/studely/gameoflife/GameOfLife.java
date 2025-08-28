// File: GameOfLife.java
package com.studely.gameoflife;

/**
 * Main class for the Game of Life application.
 * Contains the entry point and manages the simulation loop.
 */
public class GameOfLife {
    private Grid grid;
    private volatile boolean running;
    private int generation;
    private int delayMs;
    
    /**
     * Creates a new Game of Life simulation with the specified grid dimensions.
     * 
     * @param width the width of the grid
     * @param height the height of the grid
     */
    public GameOfLife(int width, int height) {
        this.grid = new Grid(width, height);
        this.running = false;
        this.generation = 0;
        this.delayMs = 100; // Default delay
    }
    
    /**
     * Starts the simulation.
     */
    public void start() {
        if (!running) {
            running = true;
            runSimulation();
        }
    }
    
    /**
     * Stops the simulation.
     */
    public void stop() {
        running = false;
    }
    
    /**
     * Advances the simulation by one generation.
     */
    public void step() {
        grid.nextGeneration();
        generation++;
    }
    
    /**
     * The main simulation loop.
     */
    private void runSimulation() {
        Thread simulationThread = new Thread(() -> {
            while (running) {
                long startTime = System.currentTimeMillis();
                
                step();
                
                // Calculate time to wait to maintain consistent frame rate
                long processingTime = System.currentTimeMillis() - startTime;
                long sleepTime = Math.max(0, delayMs - processingTime);
                
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            }
        });
        
        simulationThread.setDaemon(true);
        simulationThread.start();
    }
    
    /**
     * Clears the grid.
     */
    public void clear() {
        grid.clear();
        generation = 0;
    }
    
    /**
     * Randomizes the grid with approximately 30% alive cells.
     */
    public void randomize() {
        grid.randomize(0.3);
        generation = 0;
    }
    
    /**
     * Gets the current grid.
     * 
     * @return the grid
     */
    public Grid getGrid() {
        return grid;
    }
    
    /**
     * Gets the current generation count.
     * 
     * @return the generation count
     */
    public int getGeneration() {
        return generation;
    }
    
    /**
     * Sets the delay between generations.
     * 
     * @param delayMs the delay in milliseconds
     */
    public void setDelay(int delayMs) {
        this.delayMs = delayMs;
    }
    
    /**
     * Gets the current delay between generations.
     * 
     * @return the delay in milliseconds
     */
    public int getDelay() {
        return delayMs;
    }
    
    /**
     * The main entry point of the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Create and display the UI on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameOfLife game = new GameOfLife(60, 40);
            GameOfLifeUI ui = new GameOfLifeUI(game);
            ui.setVisible(true);
        });
    }
}