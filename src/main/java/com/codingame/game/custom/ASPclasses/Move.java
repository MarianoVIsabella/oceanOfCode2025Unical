package com.codingame.game.custom.ASPclasses;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("move")
public class Move implements ASPCommand {

    // Parameters
    @Param(0) private int timeOrder;
    @Param(1) private String dir;
    @Param(2) private String power = "nil";

    // Constructors
    public Move() {}
    public Move(int timeOrder, String dir, String power) {
        this.timeOrder = timeOrder;
        this.dir = dir;
        this.power = power;
    }

    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }

    public String getPower() { return this.power; }
    public void setPower(String power) { this.power = power; }

    // Time Ordering
    @Override
    public int getTimeOrder() { return timeOrder; }
    @Override
    public void setTimeOrder(int time) { this.timeOrder = time; }

    @Override
    public String toString() {
        return "move(" + this.timeOrder + ", " + this.dir + ", " + this.power +  ")";
    }

    // To Print Command in Game Format
    @Override
    public String toUpperString(){
        return "MOVE " + dir.toUpperCase() + (this.power.equals("nil") ? "" : " " + this.power.toUpperCase());
    }

}
