package com.codingame.game.custom.data;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("minedCell")
public class MinedCell {

    // Parameters
    @Param(0) private final int row;
    @Param(1) private final int column;

    // Constructors
    public MinedCell(int row, int col) {
        this.row = row;
        this.column = col;
    }

    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }

    @Override
    public String toString() {
        return String.format("minedCell(%d, %d)", this.row, this.column);
    }

}
