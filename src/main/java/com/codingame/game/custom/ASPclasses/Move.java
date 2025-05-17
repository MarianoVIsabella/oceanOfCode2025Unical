package com.codingame.game.custom.ASPclasses;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("move")
public class Move {

    @Param(0)
    private String dir;

    @Param(1)
    private String charge;

    public Move() {}

    public Move(String dir, String charge) {
        this.dir = dir;
        this.charge = charge;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "move(" + dir + ", " + charge + ")";
    }

    public String toUpperString(){
        return "MOVE " + dir.toUpperCase() + " " + charge.toUpperCase();
    }
}
