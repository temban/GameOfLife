package com.studely.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

public class GameOfLifeUI extends JFrame {
    private final GameOfLife game;
    private final GridPanel gridPanel;
    private final JLabel generationLabel;
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton stepButton;
    private final JButton clearButton;
    private final JButton randomButton;
    private final JSlider speedSlider;
    
    /**
     * Creates a new Game of Life UI.
     * 
     * @param game the Game of Life simulation to visualize
     */
    public GameOfLifeUI(GameOfLife game) {
        this.game = game;
        
        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        gridPanel = new GridPanel(game);
        generationLabel = new JLabel("Generation: 0");
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        stepButton = new JButton("Step");
        clearButton = new JButton("Clear");
        randomButton = new JButton("Random");
        
        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 100);
        speedSlider.setMajorTickSpacing(100);
        speedSlider.setMinorTickSpacing(50);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Fast"));
        labelTable.put(250, new JLabel("Medium"));
        labelTable.put(500, new JLabel("Slow"));
        speedSlider.setLabelTable(labelTable);
        
        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(stepButton);
        controlPanel.add(clearButton);
        controlPanel.add(randomButton);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(generationLabel);
        
        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.NORTH);
        
        setupEventHandlers();
        
        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void setupEventHandlers() {
        startButton.addActionListener(e -> {
            game.start();
            updateButtons();
        });
        
        stopButton.addActionListener(e -> {
            game.stop();
            updateButtons();
        });
        
        stepButton.addActionListener(e -> {
            game.step();
            updateGenerationLabel();
            gridPanel.repaint();
        });
        
        clearButton.addActionListener(e -> {
            game.stop();
            game.clear();
            updateGenerationLabel();
            gridPanel.repaint();
            updateButtons();
        });
        
        randomButton.addActionListener(e -> {
            game.stop();
            game.randomize();
            updateGenerationLabel();
            gridPanel.repaint();
            updateButtons();
        });
        
        speedSlider.addChangeListener(e -> {
            int delay = 500 - speedSlider.getValue();
            game.setDelay(delay);
        });
        
        // Add pattern menu
        JMenuBar menuBar = new JMenuBar();
        JMenu patternsMenu = new JMenu("Patterns");
        
        JMenuItem gliderItem = new JMenuItem("Glider");
        gliderItem.addActionListener(e -> {
            game.stop();
            game.clear();
            game.getGrid().createGlider(10, 10);
            updateGenerationLabel();
            gridPanel.repaint();
            updateButtons();
        });
        
        JMenuItem blinkerItem = new JMenuItem("Blinker");
        blinkerItem.addActionListener(e -> {
            game.stop();
            game.clear();
            game.getGrid().createBlinker(10, 10);
            updateGenerationLabel();
            gridPanel.repaint();
            updateButtons();
        });
        
        JMenuItem pulsarItem = new JMenuItem("Pulsar");
        pulsarItem.addActionListener(e -> {
            game.stop();
            game.clear();
            game.getGrid().createPulsar(10, 10);
            updateGenerationLabel();
            gridPanel.repaint();
            updateButtons();
        });
        
        patternsMenu.add(gliderItem);
        patternsMenu.add(blinkerItem);
        patternsMenu.add(pulsarItem);
        menuBar.add(patternsMenu);
        setJMenuBar(menuBar);
        
        Timer uiUpdateTimer = new Timer(100, e -> {
            updateGenerationLabel();
            gridPanel.repaint();
        });
        uiUpdateTimer.start();
    }
    
    private void updateGenerationLabel() {
        generationLabel.setText("Generation: " + game.getGeneration());
    }
    
 
    private void updateButtons() {
        boolean isRunning = game.getGeneration() > 0;
        startButton.setEnabled(!isRunning);
        stopButton.setEnabled(isRunning);
        stepButton.setEnabled(!isRunning);
    }
    
    private class GridPanel extends JPanel {
        private final GameOfLife game;
        private static final int CELL_SIZE = 15;
        
        public GridPanel(GameOfLife game) {
            this.game = game;
            setPreferredSize(new Dimension(
                game.getGrid().getWidth() * CELL_SIZE,
                game.getGrid().getHeight() * CELL_SIZE
            ));
            setBackground(Color.WHITE);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    toggleCell(e.getX(), e.getY());
                }
            });
            
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    toggleCell(e.getX(), e.getY());
                }
            });
        }
        
        /**
         * Toggles the state of the cell at the specified screen coordinates.
         * @param x
         * @param y
         */
        private void toggleCell(int x, int y) {
            int gridX = x / CELL_SIZE;
            int gridY = y / CELL_SIZE;
            
            if (gridX >= 0 && gridX < game.getGrid().getWidth() && 
                gridY >= 0 && gridY < game.getGrid().getHeight()) {
                
                CellState currentState = game.getGrid().getCell(gridX, gridY);
                game.getGrid().setCell(gridX, gridY, 
                    currentState == CellState.ALIVE ? CellState.DEAD : CellState.ALIVE);
                repaint();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Grid grid = game.getGrid();
            
            // Draw cells
            for (int y = 0; y < grid.getHeight(); y++) {
                for (int x = 0; x < grid.getWidth(); x++) {
                    if (grid.getCell(x, y) == CellState.ALIVE) {
                        g.setColor(Color.BLUE);
                        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }                    
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}