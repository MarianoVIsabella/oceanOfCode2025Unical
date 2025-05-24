package com.codingame.game.custom.data;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    // Current Player Stats
    public int positionX = -1, positionY = -1;
    public int myLifeValue = 6;
    public List<MinedCell> minedCells;

    // Opponent Player Stats
    public int opponentLifeValue = 6;
    public String opponentOrders = "NA";
    public int opponentVerticalOffset = 0;
    public int opponentHorizontalOffset = 0;

    // Powers' Cooldown Values
    public int torpedoCooldown = -1,
            sonarCooldown = -1,
            silenceCooldown = -1,
            mineCooldown = -1;

    // Powers' Results & Usage
    public String sonarResult = "NA";
    public int sectorAsked = -1;

    public Statistics() {
        this.minedCells = new ArrayList<>();
    }
}
