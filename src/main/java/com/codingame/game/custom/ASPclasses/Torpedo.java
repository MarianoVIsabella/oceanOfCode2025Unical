package com.codingame.game.custom.ASPclasses;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("torpedo")
public class Torpedo implements ASPCommand{

    // Parameters
    @Param(0) private int timeOrder;
    @Param(1) private int X;
    @Param(2) private int Y;

    // Constructors
    public Torpedo() {}
    public Torpedo(int timeOrder, int y, int x) {
        this.timeOrder = timeOrder;
        this.Y = y;
        this.X = x;
    }

    public int getX() { return X; }
    public void setX(int x) { X = x; }

    public int getY() { return Y; }
    public void setY(int y) { Y = y; }

    // Time Ordering
    @Override
    public int getTimeOrder() { return this.timeOrder; }
    @Override
    public void setTimeOrder(int time) { this.timeOrder = time; }

    @Override
    public String toString() {
        return "torpedo(" + this.timeOrder + ", " + X + "," + Y + ')';
    }

    // To Print Command in Game Format
    @Override
    public String toUpperString(){
        return "TORPEDO " + Y + " " + X;
    }
}
