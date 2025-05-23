package com.codingame.game.custom.ASPclasses;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("sonar")
public class Sonar implements ASPCommand {

    // Parameters
    @Param(0) private int timeOrder;
    @Param(1) private int sectorAsked;

    // Constructors
    public Sonar() {}
    public Sonar(int timeOrder, int sector) {
        this.timeOrder = timeOrder;
        this.sectorAsked = sector;
    }

    public int getSectorAsked() { return this.sectorAsked; }
    public void setSectorAsked(int sectorAsked) { this.sectorAsked = sectorAsked; }

    // Time Ordering
    @Override
    public int getTimeOrder() { return this.timeOrder; }
    @Override
    public void setTimeOrder(int time) { this.timeOrder = time; }

    @Override
    public String toString() {
        return String.format("sonar(%d, %d)", this.timeOrder, this.sectorAsked);
    }

    // To Print Command in Game Format
    @Override
    public String toUpperString() {
        return String.format("SONAR %d", this.sectorAsked);
    }
}
