package com.codingame.game.custom.ASPclasses;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("mine")
public class Mine implements ASPCommand {

    // Parameters
    @Param(0) private int timeOrder;
    @Param(1) private String dir;

    // Constructors
    public Mine() {}
    public Mine(int timeOrder, String dir) {
        this.timeOrder = timeOrder;
        this.dir = dir;
    }

    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }

    // Time Ordering
    @Override
    public int getTimeOrder() { return this.timeOrder; }
    @Override
    public void setTimeOrder(int time) { this.timeOrder = time; }

    @Override
    public String toString() {
        return String.format("mine(%d, %s)", this.timeOrder, this.dir);
    }

    // To Print Command in Game Format
    @Override
    public String toUpperString() {
        return String.format("MINE %s", this.dir.toUpperCase());
    }
}
