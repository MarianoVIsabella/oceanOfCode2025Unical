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
    protected boolean[][] gridCells;

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
        this.gridCells = new boolean[gridWidth][gridHeight];
        for (int i = 0; i < gridHeight; i++) {
            String line = in.nextLine();

            for (int j = 0; j < gridWidth; j++) gridCells[i][j] = line.charAt(j) != 'X';
        }

        // Choose Player's Initial Position
        this.chooseAndPrintInitialPosition();
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

            // Choose where to move next
            this.chooseAndPrintNextAction();
        }
    }

    // By Default, it prints the first valid cell it finds looking row by row
    protected void chooseAndPrintInitialPosition() {
        System.out.println("7 7"); return;
//        for (int i = 0; i < this.gridHeight; i++)
//            for (int j = 0; j < this.gridWidth; j++)
//                if (this.gridCells[i][j]) {
//                    System.out.print(i + " " + j);
//                    System.err.println(String.format(infoBaseString, playerName) +
//                            "Chosen Position by default Chooser");
//                    return;
//                }
    }

    // By Default, it tells to move north and not to use any powers
    protected void chooseAndPrintNextAction() {
        System.out.println("MOVE S");
        System.err.println(String.format(infoBaseString, playerName) + "Chosen Position by default Chooser");
        System.err.println(gridWidth + "x" + gridHeight + " grid");
    }
}

