package com.codingame.game.custom.ASPclasses;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("move")
public class Move {

    @Param(0)
    private String dir;

    @Param(1)
    private String power = "nil";

    public Move() {}

    public Move(String dir, String power) {
        this.dir = dir;
        this.power = power;
    }

    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }

    public String getPower() { return this.power; }
    public void setPower(String power) { this.power = power; }

    @Override
    public String toString() {
        return "move(" + this.dir + ", " + this.power +  ")";
    }

    public String toUpperString(){
        return "MOVE " + dir.toUpperCase() + (this.power.equals("nil") ? "" : " " + this.power.toUpperCase());
    }

}
