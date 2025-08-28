package com.studely.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Simple Swing UI for Conway's Game of Life.
 */
public class GameOfLifeUI extends JFrame {
    private final Grid grid;
    private final GridPanel gridPanel;
    private final Timer timer;
    private boolean running = false;

    public GameOfLifeUI(Grid grid) {
        super("Game of Life");
        this.grid = grid;
        this.gridPanel = new GridPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);
        add(buildControls(), BorderLayout.NORTH);

        // 10 FPS default
        timer = new Timer(100, e -> {
            grid.step();
            gridPanel.repaint();
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel buildControls() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton startPause = new JButton("Start");
        JButton step = new JButton("Step");
        JButton clear = new JButton("Clear");
        JButton random = new JButton("Random");

        JSlider speed = new JSlider(20, 500, 100); // delay in ms
        speed.setToolTipText("Speed (lower = faster)");

        startPause.addActionListener(e -> {
            running = !running;
            if (running) {
                timer.setDelay(speed.getValue());
                timer.start();
                startPause.setText("Pause");
            } else {
                timer.stop();
                startPause.setText("Start");
            }
        });

        step.addActionListener(e -> {
            if (!running) {
                grid.step();
                gridPanel.repaint();
            }
        });

        clear.addActionListener(e -> {
            grid.clear();
            gridPanel.repaint();
        });

        random.addActionListener(e -> {
            grid.randomize(0.25);
            gridPanel.repaint();
        });

        speed.addChangeListener(e -> {
            timer.setDelay(speed.getValue());
        });

        controls.add(startPause);
        controls.add(step);
        controls.add(clear);
        controls.add(random);
        controls.add(new JLabel("Speed:"));
        controls.add(speed);
        return controls;
    }

    private class GridPanel extends JPanel {
        private final int preferredCellSize = 16;

        public GridPanel() {
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    toggleAt(e.getX(), e.getY());
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    toggleAt(e.getX(), e.getY());
                }
            });
        }

        private void toggleAt(int x, int y) {
            int cellSize = cellSize();
            int c = x / cellSize;
            int r = y / cellSize;
            if (r >= 0 && r < grid.getRows() && c >= 0 && c < grid.getCols()) {
                grid.toggle(r, c);
                repaint();
            }
        }

        private int cellSize() {
            int w = Math.max(1, getWidth());
            int h = Math.max(1, getHeight());
            int cw = Math.max(4, w / grid.getCols());
            int ch = Math.max(4, h / grid.getRows());
            return Math.max(4, Math.min(cw, ch));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(grid.getCols() * preferredCellSize, grid.getRows() * preferredCellSize);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cs = cellSize();
            // draw grid
            for (int r = 0; r < grid.getRows(); r++) {
                for (int c = 0; c < grid.getCols(); c++) {
                    int x = c * cs;
                    int y = r * cs;
                    if (grid.isAlive(r, c)) {
                        g.fillRect(x, y, cs, cs);
                    } else {
                        g.drawRect(x, y, cs, cs);
                    }
                }
            }
        }
    }
}
