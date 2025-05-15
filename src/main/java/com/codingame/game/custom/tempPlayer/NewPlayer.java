package com.codingame.game.custom.tempPlayer;

import com.codingame.game.custom.ASPclasses.Move;
import com.codingame.game.custom.ASPclasses.Torpedo;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.util.*;

/**
 * Base Class for Input Parsing and Game Cycles Handling.
 * It contains all the basic information to successfully compute a valid position to play the game
 **/
public class NewPlayer {

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

    public static class ASPHelper {
        // Path to ASP File to be excecuted
        public String aspProgramPath = "encodings/prova";

        // Types of Facts to be submitted to ASP Program
        public String immutableFacts;
        public String mutableFacts;

        // ASP Handler
        public Handler handler;

        // Input Container for ASP Program
        public InputProgram aspInputProgram;

        public StringBuilder sb;

    }

    // Grid Dimensions & Cells' info
    protected int gridWidth, gridHeight;
    protected int[][] gridCells;

    // Player Id
    protected int id;

    // Game Statitics
    protected Statistics stats;

    // ASP Helper
    protected ASPHelper aspHelper;

    protected static final String infoBaseString =  " ----- [%s] %s%n";
    protected String playerName = "New Game Player";

    public static void main(String[] args) { new NewPlayer().handleGameCycles(); }

    public NewPlayer() { this.init(); }

    protected void init() {
        this.aspHelper = new ASPHelper();
        this.stats = new Statistics();

        // Declaration of ASP Handler
        this.aspHelper.handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

        // Inserting Predicate Classes into ASP Mapper
        try {
            ASPMapper.getInstance().registerClass(Move.class);
            ASPMapper.getInstance().registerClass(Torpedo.class);
        }
        catch (ObjectNotValidException | IllegalAnnotationException e1) {
            System.err.println(Arrays.toString(e1.getStackTrace()));
        }

        // Declaring Container for Program Inputs
        this.aspHelper.aspInputProgram = new ASPInputProgram();
        this.aspHelper.aspInputProgram.addFilesPath(this.aspHelper.aspProgramPath);

        // Declaring String Builder in order to collect Data to create ASP Predicates
        this.aspHelper.sb = new StringBuilder();

    }

    protected void handleGameCycles() {
        Scanner in = new Scanner(System.in);

        // Read Game infos about Board and Player Id
        this.gridWidth = in.nextInt();
        this.gridHeight = in.nextInt();
        this.id = in.nextInt();
        if (in.hasNextLine()) in.nextLine();

        // Read Board from Console
        this.gridCells = new int[gridWidth][gridHeight];

        for (int row = 0; row < this.gridHeight; row++) {
            String line = in.nextLine();

            for (int col = 0; col < this.gridWidth; col++)
                gridCells[row][col] = (line.charAt(col) == 'x') ? 2 : 0;
        }

        // Prepare Initial Internal State
        this.prepareInitialInternalState();

        // Choose Player's Initial Position and Print it
        List<Integer> initPos =  this.chooseInitialPosition();
        System.out.printf("%d %d%n", initPos.get(0), initPos.get(1));

        // Update Initial Internal State
        this.updateInitialInternalState(initPos);

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

            // Prepare Current Internal State
            this.prepareCurrentInternalState();

            // Choose where to move next
            List<String> actions = this.chooseNextAction();
            printNextAction(actions);

            this.updateCurrentInternalState(actions);
        }
    }


    // By Default, it prints the first valid cell it finds looking row by row
    protected List<Integer> chooseInitialPosition() {
        List<Integer> toRet = new ArrayList<>();

        for (int row = 0; row < this.gridHeight; row++) {
            for (int col = 0; col < this.gridWidth; col++)
                if (this.gridCells[row][col] == 0) {
                    toRet.add(col); toRet.add(row);
                    return toRet;
                }
        }

//        toRet.add(7);
//        toRet.add(7);

        return toRet;
    }

    // By Default, it tells to move north and not to use any powers
    protected List<String> chooseNextAction() {
        System.err.printf(infoBaseString, this.playerName, "Choosing which action to execute");

        System.err.printf(infoBaseString, this.playerName, "ASP Facts:");
        System.err.printf(infoBaseString, this.playerName, "Immutable: " + this.aspHelper.immutableFacts);
        System.err.printf(infoBaseString, this.playerName, "Mutable: "+ this.aspHelper.mutableFacts);

        List<String> toRet = new ArrayList<>();
        toRet.add("MOVE S");

        return toRet;
    }

    // It helps to print out the next Aciton
    protected void printNextAction(List<String> actions) {
        System.err.printf(infoBaseString, this.playerName, "Executing this set of actions: " + actions);
        StringBuilder nextAction = new StringBuilder();

        for (String a : actions) {

            if(a.equals("SURFACE")) {
                nextAction.append(a);
                break;
            }

            if (a.startsWith("MOVE")) {
                nextAction.insert(0, a);
                continue;
            }
            nextAction.append(" | ").append(a);
        }

        System.out.println(nextAction);
    }

    protected void prepareInitialInternalState() {

        // Refreshing StringBuilder if not empty
        if (this.aspHelper.sb.length() > 0) this.aspHelper.sb.setLength(0);

        // Defining Water Cells
        for (int i = 0; i < this.gridWidth; i++)
            for (int j = 0; j < this.gridHeight; j++)
                if (this.gridCells[i][j] != 2)
                    this.aspHelper.sb.append("watercell(").append(i)
                            .append(", ").append(j).append("). ");

        this.aspHelper.immutableFacts = this.aspHelper.sb.toString();

        // Setting Immutable Facts as Part of the ASP Program
        if (!this.aspHelper.immutableFacts.isEmpty())
            this.aspHelper.aspInputProgram.addProgram(this.aspHelper.immutableFacts);

        this.aspHelper.sb.setLength(0);
    }

    protected void prepareCurrentInternalState() {
        // Refreshing StringBuilder if not empty
        if (this.aspHelper.sb.length() > 0) this.aspHelper.sb.setLength(0);

        // Defining Visited Cells
        for (int row = 0; row < this.gridHeight; row++)
            for (int col = 0; col < this.gridWidth; col++)
                if (this.gridCells[row][col] == 1)
                    this.aspHelper.sb.append("visitedCell(").append(row).append(", ").append(col).append("). ");

//        // Player's Position as Visited
//        this.aspHelper.sb.append("visitedCell(").append(this.stats.positionY).append(", ").append(this.stats.positionX).append("). ");

        // Defining Statistics of Current Round
        this.aspHelper.sb.append("myPos(").append(this.stats.positionY).append(", ").append(this.stats.positionX).append("). ");
        this.aspHelper.sb.append("myLife(").append(this.stats.myLifeValue).append("). ");

        this.aspHelper.sb.append("oppLife(").append(this.stats.opponentLifeValue).append("). ");

        this.aspHelper.sb.append("torpedoCooldown(").append(this.stats.torpedoCooldown).append("). ");
        this.aspHelper.sb.append("sonarCooldown(").append(this.stats.sonarCooldown).append("). ");
        this.aspHelper.sb.append("silenceCooldown(").append(this.stats.silenceCooldown).append("). ");
        this.aspHelper.sb.append("mineCooldown(").append(this.stats.mineCooldown).append("). ");

        // Detecting of Sonar Scan of Opponent Sector
        if (!this.stats.sonarResult.equals("NA"))
            this.aspHelper.sb.append("oppSector(").append(this.stats.sonarResult).append("). ");

        // Detecting of Opponent Action
        if (!this.stats.opponentOrders.equals("NA"))
            this.aspHelper.sb.append("oppCommand(\"").append(this.stats.opponentOrders).append("\"). ");

        this.aspHelper.mutableFacts = this.aspHelper.sb.toString();
        this.aspHelper.sb.setLength(0);
    }

    protected void updateCurrentInternalState(List<String> actions) {

        // Update Position Based on Chosen Moving Directions
        actions.stream()
                .filter(command -> command.startsWith("MOVE"))
                .map(command -> command.split(" ")[1]) // Chosen Direction
                .findFirst()
                .ifPresent(dir -> {
                    switch (dir) {
                        case "N" -> { this.gridCells[this.stats.positionY - 1][this.stats.positionX] = 1; }
                        case "S" -> { this.gridCells[this.stats.positionY + 1][this.stats.positionX] = 1; }
                        case "W" -> { this.gridCells[this.stats.positionY][this.stats.positionX - 1] = 1; }
                        case "E" -> { this.gridCells[this.stats.positionY][this.stats.positionX + 1] = 1; }
                    }
                });

    }

    protected void updateInitialInternalState(List<Integer> position) {

        // Update Initial Position as Visited
        this.gridCells[position.get(1)][position.get(0)] = 1;


    }
}


