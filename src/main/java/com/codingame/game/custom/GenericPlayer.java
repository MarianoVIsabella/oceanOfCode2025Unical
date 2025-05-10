package com.codingame.game.custom;

import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class GenericPlayer {

    protected class Statistics {
        // Current Player Stats
        public int positionX = -1, positionY = -1;
        public int life = 6;

        // Opponent Player Stats
        public int opponentLife;
        public int opponentOrders;

        // Powers' Cooldown Values
        public int torpedoCooldown, sonarCooldown, silenceCooldown, mineCooldown;

        // Powers' Results
        public int sonarResult;

    }

    protected static final String infoBaseString =  "----- [%s]";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Read Game infos about Board and Player Id
        int gridWidth = in.nextInt();
        int gridHeight = in.nextInt();
        int id = in.nextInt();
        if (in.hasNextLine()) in.nextLine();

        // Read Board from Console
        boolean[][] gridCells = new boolean[gridWidth][gridHeight];
        for (int i = 0; i < gridHeight; i++) {
            String line = in.nextLine();

            for (int j = 0; j < gridWidth; j++) gridCells[i][j] = line.charAt(j) != 'X';
        }

        // Choose Player's Initial Position
        chooseAndPrintInitialPosition(gridWidth, gridHeight, gridCells);

        // game loop
        while (true) {

            // Read info about the Current Game Round
            int[] myPosition = new int[]{ in.nextInt(), in.nextInt() };
            int myLifeValue = in.nextInt();

            int myOpponentLifeValue = in.nextInt();

            // Powers' Cooldown Values
            int torpedoCooldown = in.nextInt();
            int sonarCooldown = in.nextInt();
            int silenceCooldown = in.nextInt();
            int mineCooldown = in.nextInt();
            String sonarResult = in.next();

            if (in.hasNextLine()) in.nextLine();

            // Opponent's Last Message
            String opponentOrders = in.nextLine();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("MOVE N TORPEDO");
        }
    }

    // By Default, it prints the first valid cell it finds looking row by row
    protected static void chooseAndPrintInitialPosition(int gridWidth, int gridHeight, boolean[][] positionCells) {
        for (int i = 0; i < gridHeight; i++)
            for (int j = 0; j < gridWidth; j++)
                if (positionCells[j][i]) {
                    System.out.print(i + " " + j);
                    System.err.println(String.format(infoBaseString, "Generic Game Player") + "Chosen Position by default Chooser");
                    return;
                }
    }
}

