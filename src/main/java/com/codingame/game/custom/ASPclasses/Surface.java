package com.codingame.game.custom.ASPclasses;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("surface")
public class Surface implements ASPCommand{

    // Parameters
    @Param(0) private int timeOrder;

    // Constructors
    public Surface() {}
    public Surface(int timeOrder) {
        this.timeOrder = timeOrder;
    }

    // Time Ordering
    @Override
    public int getTimeOrder() { return timeOrder; }
    @Override
    public void setTimeOrder(int time) { this.timeOrder = time; }

    @Override
    public String toString() {
        return "surface(" + this.timeOrder + ")";
    }

    // To Print Command in Game Format
    @Override
    public String toUpperString(){
        return "SURFACE";
    }
}
