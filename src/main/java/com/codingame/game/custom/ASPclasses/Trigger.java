package com.codingame.game.custom.ASPclasses;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("trigger")
public class Trigger implements ASPCommand {

    // Parameters
    @Param(0) private int timeOrder;
    @Param(1) private int row;
    @Param(2) private int column;

    // Constructors
    public Trigger() {}
    public Trigger(int timeOrder, int row, int col) {
        this.timeOrder = timeOrder;
        this.row = row;
        this.column = col;
    }

    public int getRow() { return this.row; }
    public void setRow(int row) { this.row = row; }

    public int getColumn() { return this.column; }
    public void setColumn(int col) { this.column = col; }

    // Time Ordering
    @Override
    public int getTimeOrder() { return this.timeOrder; }
    @Override
    public void setTimeOrder(int time) { this.timeOrder = time; }

    @Override
    public String toString() {
        return String.format("trigger(%d, %d, %d)", this.timeOrder, this.row, this.column);
    }

    // To Print Command in Game Format
    @Override
    public String toUpperString() {
        return String.format("TRIGGER %d %d", this.row, this.column);
    }
}
