package com.codingame.game.custom.players;

import com.codingame.game.custom.ASPclasses.*;

import com.codingame.game.custom.data.ASPHelper;
import com.codingame.game.custom.data.MinedCell;
import com.codingame.game.custom.data.Statistics;

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
import java.util.stream.Collectors;

/**
 ** Base Class for Input Parsing and Game Cycles Handling, with a few Helper Classes.
 ** It contains all the basic information to successfully compute a valid position to play the game
 **/
public class ASPPlayer {

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
    protected Random randomGenerator;

    // Temporary Variable to Block Program from going above Time Limit
    protected int moveUntilEndCounter = 100;

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
            // Temp Block
            if (this.moveUntilEndCounter > 0) {
                List<ASPCommand> aspCommands = this.chooseNextAction();
                List<String> actions = aspCommands.stream()
                            .map(ASPCommand::toUpperString)
                        .collect(Collectors.toList());

                //List<String> actions = this.chooseNextAction();
                printNextAction(aspCommands);

                this.moveUntilEndCounter--;
                this.updateCurrentInternalState(aspCommands);
            } else {
                printNextAction(List.of(new Surface(0)));
                this.updateCurrentInternalState(List.of(new Surface(0)));

            }
        }
    }

    // Helping Methods, mainly for Clean Code and Readability
    protected void init() {

        // Instancing Helping Classes
        this.aspHelper = new ASPHelper();
        this.stats = new Statistics();
        this.randomGenerator = new Random();


        // Declaration of ASP Handler
        this.aspHelper.handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

        // Inserting Predicate Classes into ASP Mapper
        try {
            // Basic Movement Classes
            ASPMapper.getInstance().registerClass(Move.class);
            ASPMapper.getInstance().registerClass(Surface.class);

            // Torpedo Power
            ASPMapper.getInstance().registerClass(Torpedo.class);

            // Mine Power
            ASPMapper.getInstance().registerClass(Mine.class);
            ASPMapper.getInstance().registerClass(Trigger.class);

            // Sonar Power
            ASPMapper.getInstance().registerClass(Sonar.class);
        }
        catch (ObjectNotValidException | IllegalAnnotationException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
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

        // Defining Water Cells of the Map
        for (int row = 0; row < this.gridHeight; row++) {
            for (int col = 0; col < this.gridWidth; col++) {
                if (this.gridCells[row][col] != 2)
                    this.aspHelper.sb.append("waterCell(")
                            .append(row).append(", ").append(col).append(", ")
                            // Sector Calculation
                            .append(3 * (row/5) + (col/5) + 1).append("). ");
            }

            this.aspHelper.sb.append("\n");
        }

        this.aspHelper.sb
                // Defining Actions' Slot that can be used
                .append("actionSlot(1..5).\n")

                // Defining Cardinal Directions
                .append("direction(n, -1,0). direction(s, 1,0). direction(w, 0,-1). direction(e, 0,1).\n")

                // Defining Sectors of the Map, with Center Positions
                .append("sector(1, 2,2). sector(2, 2,7). sector(3, 2,12). ")
                .append("sector(4, 7,2). sector(5, 7,7). sector(6, 7,12). ")
                .append("sector(7, 12,2). sector(8, 12,7). sector(9, 12,12).\n")

                // Defining Power that can be used during the Game
                .append("power(torpedo). power(mine). power(trigger). power(sonar). power(nil).\n");

//                // Defining Torpedo Range Offsets
//                .append("torpedoRangeOffset(-2..2, -2..2).")
//                .append("torpedoRangeOffset(-3, -1..1). torpedoRangeOffset(3, -1..1).")
//                .append("torpedoRangeOffset(-1..1, -3). torpedoRangeOffset(-1..1, 3).")
//                .append("torpedoRangeOffset(-4, 0). torpedoRangeOffset(4, 0).")
//                .append("torpedoRangeOffset(0, -4). torpedoRangeOffset(0, 4).\n");


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
        Arrays.stream(this.stats.opponentOrders.split("\\|"))
                .filter(command -> command.startsWith("MOVE"))
                .map(command -> command.split(" ")[1]) // Chosen Direction
                .findFirst()
                .ifPresent(dir -> {
                    switch (dir) {
                        case "N" -> this.stats.opponentVerticalOffset--;
                        case "S" -> this.stats.opponentVerticalOffset++;
                        case "W" -> this.stats.opponentHorizontalOffset--;
                        case "E" -> this.stats.opponentHorizontalOffset++;
                    }
                });


        // Defining Visited Cells
        for (int row = 0; row < this.gridHeight; row++)
            for (int col = 0; col < this.gridWidth; col++)
                if (this.gridCells[row][col] == 1)
                    this.aspHelper.sb.append("visitedCell(").append(row).append(", ").append(col).append("). ");

        this.aspHelper.sb.append("\n");

        // Defining Mined Cells
        for (MinedCell minedCell : this.stats.minedCells) this.aspHelper.sb.append(minedCell).append(". ");

        this.aspHelper.sb.append("\n");

        // Defining Statistics of Current Round
        this.aspHelper.sb.append("myPos(").append(this.stats.positionY).append(", ").append(this.stats.positionX).append("). ");
        this.aspHelper.sb.append("myLife(").append(this.stats.myLifeValue).append("). ");

        this.aspHelper.sb.append("oppLife(").append(this.stats.opponentLifeValue).append(").\n");

        this.aspHelper.sb.append("torpedoCooldown(").append(this.stats.torpedoCooldown).append("). ");
        this.aspHelper.sb.append("sonarCooldown(").append(this.stats.sonarCooldown).append("). ");
        this.aspHelper.sb.append("silenceCooldown(").append(this.stats.silenceCooldown).append("). ");
        this.aspHelper.sb.append("mineCooldown(").append(this.stats.mineCooldown).append(").\n");

        // Detecting of Sonar Scan of Opponent Sector
        if (!this.stats.sonarResult.equals("NA"))
            this.aspHelper.sb.append("opp").append((this.stats.sonarResult.equals("N") ? "No" : ""))
                    .append("Sector(").append(this.stats.sectorAsked).append("). ");

        // Defining Opponent Offsets
        this.aspHelper.sb.append("oppOffsets(").append(this.stats.opponentVerticalOffset).append(", ")
                                              .append(this.stats.opponentHorizontalOffset).append(").\n");

        // Detecting of Opponent Action
        if (!this.stats.opponentOrders.equals("NA")) {
            int timeCounter = 1;
            // Detecting which action is performed
            for (String command : this.stats.opponentOrders.split("\\|")) {
                String[] actionPerformed = command.split(" ");

                // Capitalization of Action's Name
                this.aspHelper.sb.append("opp")
                        .append(String.valueOf(actionPerformed[0].charAt(0)).toUpperCase())
                        .append(actionPerformed[0].substring(1).toLowerCase());

                // Inserting Time Ordering of Action
                this.aspHelper.sb.append("(").append(timeCounter);

                // Inserting Parameters of the Action
                if (actionPerformed.length == 2) {
                    // Move, Mine Commands have only one parameter
                    this.aspHelper.sb.append(",").append(actionPerformed[1].toLowerCase());
                }
                else if (actionPerformed.length > 2) {
                    // Torpedo, Trigger Commands have two parameters (col and row)
                    // that needs to be switched to be in order

                        this.aspHelper.sb.append(",").append(actionPerformed[2].toLowerCase())
                                         .append(",").append(actionPerformed[1].toLowerCase());
                }

                this.aspHelper.sb.append(").\n");
                timeCounter++;
            }
        }

        this.aspHelper.mutableFacts = this.aspHelper.sb.toString();
        this.aspHelper.sb.setLength(0);

    }

    protected void updateCurrentInternalState(List<ASPCommand> actions) {

        for (ASPCommand command : actions) {
            if (command instanceof Move) {
                Move moveCommand = (Move) command;

                // Update Position Based on Chosen Moving Directions
                switch (moveCommand.getDir().toUpperCase()) {
                    case "N" -> this.stats.positionY -= 1;
                    case "S" -> this.stats.positionY += 1;
                    case "W" -> this.stats.positionX -= 1;
                    case "E" -> this.stats.positionX += 1;
                }

                // Updating Current Visited Cell
                this.gridCells[this.stats.positionY][this.stats.positionX] = 1;

            }
            else if (command instanceof Surface) {
                // Update Already Visited Cells to 0 if Surface Action executed
                for (int row = 0; row < this.gridHeight; row++)
                    for (int col = 0; col < this.gridWidth; col++)
                        this.gridCells[row][col] = (this.gridCells[row][col] == 2) ? 2 : 0;

                // Current Position is now the only Visited Cell
                this.gridCells[this.stats.positionY][this.stats.positionX] = 1;

            }
            else if (command instanceof Mine) {
                Mine mineCommand = (Mine) command;

                int row = this.stats.positionY, col = this.stats.positionX;

                // Detecting Mine Position Based on Current Player Position and Throwing Direction
                switch (mineCommand.getDir().toUpperCase()) {
                    case "N" -> row -= 1;
                    case "S" -> row += 1;
                    case "W" -> col -= 1;
                    case "E" -> col += 1;
                }

                // Registering new placed Mine
                this.stats.minedCells.add(new MinedCell(row, col));

            }
            else if (command instanceof Trigger) {
                Trigger triggerCommand = (Trigger) command;

                // Triggered Mine is not usable anymore
                this.stats.minedCells.remove(new MinedCell(triggerCommand.getRow(), triggerCommand.getColumn()));

            }
            else if (command instanceof Sonar) {
                Sonar sonarCommand = (Sonar) command;

                // Saving Sector Asked for next Round
                this.stats.sectorAsked = sonarCommand.getSectorAsked();

            }
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
//
//        return List.of(7,7);

        int possibleRow, possibleCol;

        while (true) {
            possibleRow = this.randomGenerator.nextInt(this.gridHeight);
            possibleCol = this.randomGenerator.nextInt(this.gridWidth);

            if (this.gridCells[possibleRow][possibleCol] == 0)
                return List.of(possibleCol, possibleRow);
        }

        // return List.of();
    }

    // By Default, it tells to Move South and not to use any powers
    protected List<ASPCommand> chooseNextAction() {
        this.printInfoMessage("Choosing which action to execute");

        // this.printInfoMessage("ASP Facts:");
        // this.printInfoMessage("Immutable: " + this.aspHelper.immutableFacts);
        this.printInfoMessage("Mutable: " + this.aspHelper.mutableFacts);

        List<ASPCommand> aspCommands = new ArrayList<>();

        // Cleaning Data for ASP Program
        this.aspHelper.handler.removeAll();

        // Setting Options to get all possible AnswerSets
        OptionDescriptor allAnsSetOption = new OptionDescriptor("-n 0");
        this.aspHelper.handler.addOption(allAnsSetOption);

        // Adding Current Facts from Game Round
        if (!this.aspHelper.mutableFacts.isEmpty())
            this.aspHelper.handler.addProgram(new ASPInputProgram(this.aspHelper.mutableFacts));

        // Adding ASP Program and Immutable Facts
        this.aspHelper.handler.addProgram(this.aspHelper.aspInputProgram);

        // Getting the AnswerSets
        Output aspProgramOutput = this.aspHelper.handler.startSync();
        AnswerSets answerSets = (AnswerSets) aspProgramOutput;

        if (answerSets == null || answerSets.getAnswersets().isEmpty())
            return List.of(new Move(0,"S", "nil"));

        for (AnswerSet as: answerSets.getAnswersets()) printInfoMessage("Answer: " + as);
        for (AnswerSet as: answerSets.getOptimalAnswerSets()) printInfoMessage("Optimal: " + as);

        // Pick Optimal Answer Sets (If there isn't defined any, it throws an Exception)
        List<AnswerSet> possibleSets = answerSets.getOptimalAnswerSets();
        // If there isn't any weight on ASP Program, use this line
        // List<AnswerSet> possibleSets = answerSets.getAnswersets();

        printInfoMessage("Number of Optimal Answer Sets: " + possibleSets.size());
        AnswerSet a = possibleSets.get(this.randomGenerator.nextInt(possibleSets.size()));

        try {
            for (Object obj : a.getAtoms()) {

                if (obj instanceof ASPCommand) { aspCommands.add((ASPCommand) obj); }
//                if (obj instanceof Move) {
//                    Move move = (Move) obj;
//                    // commands.add(move.toUpperString());
//                    aspCommands.add(move);
//                }
//                else if (obj instanceof Torpedo) {
//                    Torpedo torpedo = (Torpedo) obj;
//                    // commands.add(torpedo.toUpperString());
//                    aspCommands.add(torpedo);
//                }
//                else if (obj instanceof Surface) {
//                    Surface surface = (Surface) obj;
//                    // commands.add(surface.toUpperString());
//                    aspCommands.add(surface);
//                }
            }
            this.printInfoMessage("Commands from ASP: " + aspCommands);
        }
        catch (Exception e) {
            this.printInfoMessage("No command from ASP.");
        }

        this.printInfoMessage("ASP COMMANDS: " + aspCommands);
        if (!aspCommands.isEmpty())
            // Returning Sorted Actions by Time Ordering
            return aspCommands.stream()
                    .sorted(Comparator.comparing(ASPCommand::getTimeOrder))
                    .collect(Collectors.toList());

        return List.of(new Move(0,"S", "nil"));
    }

    // It helps to print out the next Actions in Standard Form
    protected void printNextAction(List<ASPCommand> actions) {
        this.printInfoMessage("Executing this set of actions: " + actions);

        StringBuilder actionsToPrint = new StringBuilder(
                (actions.isEmpty() || actions.get(0) == null) ? "" : actions.get(0).toUpperString()
        );

        for (int i = 1; i < actions.size(); i++)
            actionsToPrint.append(" | ").append(actions.get(i).toUpperString());


        System.out.println(actionsToPrint);
    }

}


