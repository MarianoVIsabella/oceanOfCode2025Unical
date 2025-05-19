package com.codingame.game.custom.ASPclasses;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;


@Id("torpedo")
public class Torpedo {

    public Torpedo() {}

    @Param(0)
    private int X;

    @Param(1)
    private int Y;

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public Torpedo(int y, int x) {
        Y = y;
        X = x;
    }

    @Override
    public String toString() {
        return "torpedo(" +
                X + "," +
                Y +
                ')';
    }

    public String toUpperString(){
        return "TORPEDO " + Y + " " + X;
    }
}
