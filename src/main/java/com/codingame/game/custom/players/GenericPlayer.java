package com.codingame.game.custom.players;

import java.util.*;

/**
    * Base Class for Input Parsing and Game Cycles Handling.
    * It contains all the basic information to successfully compute a valid position to play the game
**/
public class GenericPlayer {

    public static class Statistics {

        // Current Player Stats
        public int positionX = -1, positionY = -1;
        public int myLifeValue = 6;

        // Opponent Player Stats
        public int opponentLifeValue = 6;
        public String opponentOrders = "NA";

        // Powers' Cooldown Values
        public int torpedoCooldown = -1,
                    sonarCooldown = -1,
                    silenceCooldown = -1,
                    mineCooldown = -1;

        // Powers' Results
        public String sonarResult = "NA";

    }

    // Grid Dimensions & Cells' info
    protected int gridWidth, gridHeight;
    protected int[][] gridCells;

    // Player Id
    protected int id;

    // Game Statitics
    protected Statistics stats;

    protected static final String infoBaseString =  " ----- [%s]";
    protected String playerName = "Generic Game Player";

    public static void main(String[] args) { new GenericPlayer().handleGameCycles(); }

    protected void handleGameCycles() {
        Scanner in = new Scanner(System.in);

        // Read Game infos about Board and Player Id
        this.gridWidth = in.nextInt();
        this.gridHeight = in.nextInt();
        this.id = in.nextInt();
        if (in.hasNextLine()) in.nextLine();

        // Read Board from Console
        this.gridCells = new int[gridWidth][gridHeight];

        for (int i = 0; i < gridHeight; i++) {
            String line = in.nextLine();

            for (int j = 0; j < gridWidth; j++)
                gridCells[i][j] = (line.charAt(j) == 'X') ? 2 : 0;

        }

        // Update Initial Internal State
        this.updateInitialInternalState();

        // Choose Player's Initial Position and Print it
        List<Integer> initPos =  this.chooseInitialPosition();
        System.out.printf("%d %d%n", initPos.get(0), initPos.get(1));

        this.stats = new Statistics();

        // Game loop
        while (true) {

            // Read info about the Current Game Round
            this.stats.positionX = in.nextInt(); this.stats.positionY = in.nextInt();
            this.stats.myLifeValue = in.nextInt();

            this.stats.opponentLifeValue = in.nextInt();

            // Powers' Cooldown Values
            this.stats.torpedoCooldown = in.nextInt();
            this.stats.sonarCooldown = in.nextInt();
            this.stats.silenceCooldown = in.nextInt();
            this.stats.mineCooldown = in.nextInt();
            this.stats.sonarResult = in.next();

            if (in.hasNextLine()) in.nextLine();

            // Opponent's Last Message
            this.stats.opponentOrders = in.nextLine();

            // Update Current Internal State
            this.updateCurrentInternalState();

            // Choose where to move next
            List<String> action = this.chooseNextAction();
            printNextAction(action);
        }
    }

    // By Default, it prints the first valid cell it finds looking row by row
    protected List<Integer> chooseInitialPosition() {
        List<Integer> toRet = new ArrayList<>();

        toRet.add(7);
        toRet.add(7);

        return toRet;
    }

    // By Default, it tells to move north and not to use any powers
    protected List<String> chooseNextAction() {
        List<String> toRet = new ArrayList<>();

        toRet.add("MOVE S");

        System.err.println(String.format(infoBaseString, playerName) + "Chosen Position by default Chooser");
        System.err.println(gridWidth + "x" + gridHeight + " grid");

        return toRet;
    }

    // It helps to print out the next Aciton
    private void printNextAction(List<String> action) {
        StringBuilder nextAction = new StringBuilder();

        for (String a : action) {

            if(a.equals("SURFACE")) {
                nextAction.append(a);
            }

            if (a.startsWith("MOVE")) {
                nextAction.insert(0, a);
            }
            nextAction.append(" | ").append(a);
        }

        System.out.println(nextAction);
    }

    protected void updateInitialInternalState() {}
    protected void updateCurrentInternalState() {}
}

