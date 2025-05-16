package com.codingame.game.custom.players;

import com.codingame.game.custom.ASPclasses.Move;
import com.codingame.game.custom.ASPclasses.Surface;
import com.codingame.game.custom.ASPclasses.Torpedo;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.io.File;
import java.util.*;

/**
 ** Base Class for Input Parsing and Game Cycles Handling, with a few Helper Classes.
 ** It contains all the basic information to successfully compute a valid position to play the game
 **/
public class ASPPlayer {

    public static class Statistics {

        // Current Player Stats
        public int positionX = -1, positionY = -1;
        public int myLifeValue = 6;

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

        // Powers' Results
        public String sonarResult = "NA";
    }

    public static class ASPHelper {
        // Path to ASP File to be executed
        public String aspProgramPath = "encodings/allASPFilePrograms/prova";

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

    // Player ID
    protected int id;

    // Player's Info
    protected static final String infoBaseString =  "[ %s ] %s%n";
    protected String playerName = "ASP Player";

    // Game Statistics
    protected Statistics stats;

    // ASP Helper
    protected ASPHelper aspHelper;

    protected Scanner in;

    // Usage Settings
    // ---- GAME MODE: To be use when testing two ASP Programs one versus the other
    protected static final boolean GAME_MODE_ON = false;
    // ---- DEBUG MODE: To be use ONLY when running the Bot.main() as Program
    protected static final boolean DEBUG_MODE_ON = false;

    public static void main(String[] args) { new ASPPlayer().handleGameCycles(); }

    public ASPPlayer() { this.init(); }

    protected void handleGameCycles() {

        // Read the Initial Information from the Game, such as GridBoard and Player ID
        this.readInitialGridInfo();

        // Prepare Initial Internal State
        this.prepareInitialInternalState();

        // Choose Player's Initial Position and Print it
        List<Integer> initPos =  this.chooseInitialPosition();
        System.out.printf("%d %d%n", initPos.get(0), initPos.get(1));

        // Update Initial Internal State
        this.updateInitialInternalState(initPos);

        // Game loop
        while (true) {

            this.readGameRoundInfo();

            // Prepare Current Internal State
            this.prepareCurrentInternalState();

            // Choose where to move next
            List<String> actions = this.chooseNextAction();
            printNextAction(actions);

            this.updateCurrentInternalState(actions);
        }
    }

    // Helping Methods, mainly for Clean Code and Readability
    protected void init() {

        // Instancing Helping Classes
        this.aspHelper = new ASPHelper();
        this.stats = new Statistics();

        // Declaration of ASP Handler
        this.aspHelper.handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

        // Inserting Predicate Classes into ASP Mapper
        try {
            ASPMapper.getInstance().registerClass(Move.class);
            ASPMapper.getInstance().registerClass(Torpedo.class);
            ASPMapper.getInstance().registerClass(Surface.class);
        }
        catch (ObjectNotValidException | IllegalAnnotationException e1) {
            System.err.println(Arrays.toString(e1.getStackTrace()));
        }

        // Declaring Container for Program Inputs
        this.aspHelper.aspInputProgram = new ASPInputProgram();

        // Declaring String Builder in order to collect Data to create ASP Predicates
        this.aspHelper.sb = new StringBuilder();

    }

    protected void setBotNameAndASPProgram() {
        // Given the Player ID:
        // - the Player can now dynamically choose with ASP Program to execute
        // - the Player Name can now be shown from the File Name

        // ----- GAME MODE OFF -----------------------------------------------------------------------------------------
        if (!GAME_MODE_ON) {
            this.playerName += " " + this.id;
            return;
        }

        // ----- GAME MODE ON ------------------------------------------------------------------------------------------
        String baseFilePath = "encodings/player" + this.id;
        File folder = new File(baseFilePath);

        // The First (and Only) file in the folder will be the file program to execute
        File aspProgram = Objects.requireNonNull(folder.listFiles(File::isFile))[0];

        // Setting Player Info based on which ASP Program is executed
        this.aspHelper.aspProgramPath = baseFilePath + '/' + aspProgram.getName();
        this.playerName = aspProgram.getName() + " Bot";
    }

    protected void readInitialGridInfo() {

        // Instancing new Scanner from Input
        if (this.in == null) this.in = new Scanner(System.in);

        // Read Game infos about Board and Player ID
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
    }

    protected void readGameRoundInfo() {

        // Read info about the Current Game Round
        this.stats.positionX = in.nextInt();
        this.stats.positionY = in.nextInt();
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
    }

    protected void printInfoMessage(String message) {
        if (DEBUG_MODE_ON) System.out.printf("\u001B[3m ----- " + infoBaseString + "\u001B[0m", this.playerName, message);
        else System.err.printf(infoBaseString, this.playerName, message);
    }

    // States' Handling
    protected void prepareInitialInternalState() {

        this.setBotNameAndASPProgram();

        this.aspHelper.aspInputProgram.addFilesPath(this.aspHelper.aspProgramPath);

        // Refreshing StringBuilder if not empty
        if (this.aspHelper.sb.length() > 0) this.aspHelper.sb.setLength(0);

        // Defining Water Cells
        for (int row = 0; row < this.gridHeight; row++) {
            for (int col = 0; col < this.gridWidth; col++) {
                if (this.gridCells[row][col] != 2)
                    this.aspHelper.sb.append("waterCell(").append(row).append(", ").append(col).append("). ");
            }

            this.aspHelper.sb.append("\n");
        }

        this.aspHelper.immutableFacts = this.aspHelper.sb.toString();

        // Setting Immutable Facts as Part of the ASP Program
        if (!this.aspHelper.immutableFacts.isEmpty())
            this.aspHelper.aspInputProgram.addProgram(this.aspHelper.immutableFacts);

        this.aspHelper.sb.setLength(0);
    }

    protected void updateInitialInternalState(List<Integer> position) {

        // Update Initial Position as Visited
        this.gridCells[position.get(1)][position.get(0)] = 1;

    }

    protected void prepareCurrentInternalState() {
        // Refreshing StringBuilder if not empty
        if (this.aspHelper.sb.length() > 0) this.aspHelper.sb.setLength(0);

        // Working out how to translate Opponent Command in ASP
        // Updating Offsets from Opponent's Initial Position
        String opponentCommand = this.stats.opponentOrders;
        if (opponentCommand.startsWith("MOVE")) {
            String dir = opponentCommand.split(" ")[1]; // Estrae la direzione
            switch (dir) {
                case "N" -> this.stats.opponentVerticalOffset--;
                case "S" -> this.stats.opponentVerticalOffset++;
                case "W" -> this.stats.opponentHorizontalOffset--;
                case "E" -> this.stats.opponentHorizontalOffset++;
            }
        }

        // Defining Visited Cells
        for (int row = 0; row < this.gridHeight; row++)
            for (int col = 0; col < this.gridWidth; col++)
                if (this.gridCells[row][col] == 1)
                    this.aspHelper.sb.append("visitedCell(").append(row).append(", ").append(col).append("). ");

        // Defining Statistics of Current Round
        this.aspHelper.sb.append("\nmyPos(").append(this.stats.positionY).append(", ").append(this.stats.positionX).append("). ");
        this.aspHelper.sb.append("myLife(").append(this.stats.myLifeValue).append("). ");

        this.aspHelper.sb.append("oppLife(").append(this.stats.opponentLifeValue).append(").\n");

        this.aspHelper.sb.append("torpedoCooldown(").append(this.stats.torpedoCooldown).append("). ");
        this.aspHelper.sb.append("sonarCooldown(").append(this.stats.sonarCooldown).append("). ");
        this.aspHelper.sb.append("silenceCooldown(").append(this.stats.silenceCooldown).append("). ");
        this.aspHelper.sb.append("mineCooldown(").append(this.stats.mineCooldown).append(").\n");

        // Detecting of Sonar Scan of Opponent Sector
        if (!this.stats.sonarResult.equals("NA"))
            this.aspHelper.sb.append("oppSector(").append(this.stats.sonarResult).append("). ");

        // Defining Opponent Offsets
        this.aspHelper.sb.append("oppVerticalOffset(").append(this.stats.opponentVerticalOffset).append("). ");
        this.aspHelper.sb.append("oppHorizontalOffset(").append(this.stats.opponentHorizontalOffset).append(").\n");

        // Detecting of Opponent Action
        if (!this.stats.opponentOrders.equals("NA"))
            this.aspHelper.sb.append("oppCommand(\"").append(this.stats.opponentOrders).append("\").\n");

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
                        case "N" -> this.gridCells[this.stats.positionY - 1][this.stats.positionX] = 1;
                        case "S" -> this.gridCells[this.stats.positionY + 1][this.stats.positionX] = 1;
                        case "W" -> this.gridCells[this.stats.positionY][this.stats.positionX - 1] = 1;
                        case "E" -> this.gridCells[this.stats.positionY][this.stats.positionX + 1] = 1;
                    }
                });

        // Update Already Visited Cells to 0 if Surface Action exectued
        if (actions.contains("SURFACE")) {

            for (int row = 0; row < this.gridHeight; row++)
                for (int col = 0; col < this.gridWidth; col++)
                    this.gridCells[row][col] = (this.gridCells[row][col] == 2) ? 2 : 0;

            // Current Position is now the only Visited Cell
            this.gridCells[this.stats.positionY][this.stats.positionX] = 1;
        }

    }


    // Actions' Handling
    // By Default, it gives back the first valid random cell it finds
    protected List<Integer> chooseInitialPosition() {

//        // By Default, it prints the first valid cell it finds looking row by row
//        for (int row = 0; row < this.gridHeight; row++) {
//            for (int col = 0; col < this.gridWidth; col++)
//                if (this.gridCells[row][col] == 0)
//                    return List.of(col, row);
//
//        }

        Random random = new Random();
        int possibleRow, possibleCol;

        while (true) {
            possibleRow = random.nextInt(this.gridHeight);
            possibleCol = random.nextInt(this.gridWidth);

            if (this.gridCells[possibleRow][possibleCol] == 0)
                return List.of(possibleCol, possibleRow);
        }

        // return List.of();
    }

    // By Default, it tells to Move South and not to use any powers
    protected List<String> chooseNextAction() {
        this.printInfoMessage("Choosing which action to execute");

        this.printInfoMessage("ASP Facts:");
        this.printInfoMessage("Immutable: " + this.aspHelper.immutableFacts);
        this.printInfoMessage("Mutable: " + this.aspHelper.mutableFacts);

        List<String> commands = new ArrayList<>();

        // Cleaning Data for ASP Program
        this.aspHelper.handler.removeAll();

        // Setting Options to get all possible AnswerSets
//        OptionDescriptor allAnsSetOption = new OptionDescriptor("-n 0");
//        this.aspHelper.handler.addOption(allAnsSetOption);

        // Adding Current Facts from Game Round
        if (!this.aspHelper.mutableFacts.isEmpty())
            this.aspHelper.handler.addProgram(new ASPInputProgram(this.aspHelper.mutableFacts));

        // Adding ASP Program and Immutable Facts
        this.aspHelper.handler.addProgram(this.aspHelper.aspInputProgram);

        // Getting the AnswerSets
        Output aspProgramOutput = this.aspHelper.handler.startSync();
        AnswerSets answerSets = (AnswerSets) aspProgramOutput;

        if (answerSets == null || answerSets.getAnswersets().isEmpty())
            return List.of("MOVE S");

        AnswerSet a = answerSets.getAnswersets().get(0);

        try {
            for (Object obj : a.getAtoms()) {
                if (obj instanceof Move) {
                    Move move = (Move) obj;
                    commands.add(move.toUpperString());
                }
                else if (obj instanceof Torpedo) {
                    Torpedo torpedo = (Torpedo) obj;
                    commands.add(torpedo.toUpperString());
                }
                else if (obj instanceof Surface) {
                    Surface surface = (Surface) obj;
                    commands.add(surface.toUpperString());
                }
            }
            this.printInfoMessage("Commands from ASP: " + commands);
        }
        catch (Exception e) {
            this.printInfoMessage("No command from ASP.");
        }

        if (!commands.isEmpty())
            return Collections.unmodifiableList(commands);

        return List.of("MOVE S");
    }

    // It helps to print out the next Actions in Standard Form
    protected void printNextAction(List<String> actions) {
        this.printInfoMessage("Executing this set of actions: " + actions);

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

}


