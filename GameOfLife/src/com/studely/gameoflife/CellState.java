package com.studely.gameoflife;

public enum CellState {
    ALIVE, DEAD;

    public static CellState fromBoolean(boolean v) {
        return v ? ALIVE : DEAD;
    }

    public boolean isAlive() {
        return this == ALIVE;
    }
}
