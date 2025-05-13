package com.codingame.game.custom.ASPclasses;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("move")
public class Move {

    @Param(0)
    private String dir;

    public Move() {}

    public Move(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "move(" + dir + ")";
    }

    public String toUpperString(){
        return "MOVE " + dir.toUpperCase();
    }
}
