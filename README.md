
# GameOfLife (Java)

A minimal **Conway's Game of Life** implementation with a simple Swing UI.

---

## 📂 Project Structure
```
GameOfLife/
├── src/
│   └── com/
│       └── studely/
│           └── gameoflife/
│               ├── GameOfLife.java     # Entry point (main method)
│               ├── Grid.java           # Handles grid logic (rules of the game)
│               ├── CellState.java      # Enum for cell state (ALIVE or DEAD)
│               └── GameOfLifeUI.java   # Swing UI (window, controls, drawing)
├── README.md                          # Instructions on building/running
└── .gitignore
```

---

## ⚙️ Explanation of Each File

### 1. `CellState.java`
- An enum that defines whether a cell is **ALIVE** or **DEAD**.
- Contains helper methods like `isAlive()`.

### 2. `Grid.java`
- The **core logic of the Game of Life**.
- Maintains two 2D boolean arrays:
  - `current` → the current generation.
  - `next` → the next generation (calculated from rules).
- Rules of Conway’s Game of Life are applied:
  1. Any alive cell with **2 or 3 neighbors** survives.
  2. Any dead cell with **exactly 3 neighbors** becomes alive.
  3. Otherwise, the cell dies or stays dead.

Also includes utility methods:
- `clear()` → wipes grid empty.
- `randomize(probability)` → fills grid randomly.
- `toggle(r, c)` → flips a single cell alive/dead.
- `step()` → advances one generation.

### 3. `GameOfLifeUI.java`
- Builds a **Swing graphical interface** (`JFrame`).
- Contains:
  - A **grid panel** (custom `JPanel`) that **draws cells as squares**.
  - Buttons:
    - **Start/Pause** → starts or pauses simulation.
    - **Step** → moves forward one generation.
    - **Clear** → clears grid.
    - **Random** → randomizes cells.
  - **Speed slider** → controls how fast the game updates.

👉 You can **click and drag with the mouse** on the grid to toggle cells alive/dead.

### 4. `GameOfLife.java`
- The entry point (`main` class).
- Starts the app with default size `40 x 60` grid, or you can pass custom rows and cols.

---

## 🖥️ What the UI Looks Like
When you run the program:
1. A window opens titled **"Game of Life"**.
2. You see a **grid of squares**:
   - Black filled = alive cells.
   - White/outlined = dead cells.
3. You can:
   - **Click/drag** to toggle cells.
   - Hit **Start** → cells evolve by rules every tick.
   - Use **Pause/Step** to manually advance.
   - Hit **Clear** or **Random** to reset.

---

## ▶️ How to Build & Run

### Requirements
- Java 8+

### Compile
From the project root (`GameOfLife/`), run:
```bash
javac -d out $(find src -name "*.java")
```

### Run
```bash
java -cp out com.studely.gameoflife.GameOfLife
```

Or pass custom rows/cols:
```bash
java -cp out com.studely.gameoflife.GameOfLife 30 30
```

---

✅ That’s it! You now have a working Conway’s Game of Life with an interactive UI in Java Swing.
