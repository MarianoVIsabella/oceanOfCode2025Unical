package com.codingame.game.custom.players;

import com.codingame.game.custom.ASPclasses.Move;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AIPlayer extends GenericPlayer {

    // Path to ASP File to be excecuted
    protected static String aspProgramPath = "encodings/prova";

    // Types of Facts to be submitted to ASP Program
    protected static String immutableFacts;
    protected static String mutableFacts;

    // ASP Handler
    protected static Handler handler;

    // Input Container for ASP Program
    protected static InputProgram inputProgram;

    protected static StringBuilder sb;

    public static void main(String[] args) {
        init();

        new AIPlayer().handleGameCycles();
    }

    protected static void init() {

        // Declaration of ASP Handler
        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

        // Inserting Predicate Classes into ASP Mapper
        try {
            ASPMapper.getInstance().registerClass(Move.class);
        }
        catch (ObjectNotValidException | IllegalAnnotationException e1) {
            System.err.println(Arrays.toString(e1.getStackTrace()));
        }

        // Declaring Container for Program Inputs
        inputProgram = new ASPInputProgram();
        inputProgram.addFilesPath(aspProgramPath);

        // Declaring String Builder in order to collect Data to create ASP Predicates
        sb = new StringBuilder();
    }

    @Override
    protected void updateInitialInternalState() {
        if (sb.length() > 0) sb.setLength(0);

        // Defining Water Cells
        for (int i = 0; i < this.gridWidth; i++)
            for (int j = 0; j < this.gridHeight; j++)
                if (this.gridCells[i][j] != 2)
                    sb.append("watercell(").append(i).append(", ").append(j).append(".");

        immutableFacts = sb.toString();
        sb.setLength(0);
    }

    @Override
    protected List<Integer> chooseInitialPosition() {
        return super.chooseInitialPosition();
    }

    @Override
    protected void updateCurrentInternalState() {
        if (sb.length() > 0) sb.setLength(0);

        // Defining Visited Cells
        for (int i = 0; i < this.gridWidth; i++)
            for (int j = 0; j < this.gridHeight; j++)
                if (this.gridCells[i][j] == 1)
                    sb.append("visitedCell(").append(i).append(", ").append(j).append(").");

        // Defining Statistics of Current Round
        sb.append("myPos(").append(this.stats.positionX).append(", ").append(this.stats.positionY).append(").");
        sb.append("myLife(").append(this.stats.myLifeValue).append(").");

        sb.append("oppLife(").append(this.stats.opponentLifeValue).append(").");

        sb.append("torpedoCooldown(").append(this.stats.torpedoCooldown).append(").");
        sb.append("sonarCooldown(").append(this.stats.torpedoCooldown).append(").");
        sb.append("silenceCooldown(").append(this.stats.torpedoCooldown).append(").");
        sb.append("mineCooldown(").append(this.stats.torpedoCooldown).append(").");

        // Detecting of Sonar Scan of Opponent Sector (?)
        if (!this.stats.sonarResult.equals("NA"))
            sb.append("oppSector(").append(this.stats.sonarResult).append(").");

        // Detecting of Opponent Action (?)
        if (!this.stats.sonarResult.equals("NA"))
            sb.append("oppCommand(").append(this.stats.opponentOrders).append(").");

        mutableFacts = sb.toString();

        sb.setLength(0);
    }

    @Override
    protected List<String> chooseNextAction() {
        handler.removeAll();

        OptionDescriptor option = new OptionDescriptor("-n 0");
        handler.addOption(option);

        // Add Immutable & Mutable Facts
        InputProgram facts = new ASPInputProgram();
        facts.addProgram(immutableFacts);
        facts.addProgram(mutableFacts);
        handler.addProgram(facts);

        // Add ASP Program
        handler.addProgram(inputProgram);

        Output o = handler.startSync();
        AnswerSets answers = (AnswerSets) o;
        int n = 0;
        List<String> commands = new ArrayList<>();

        if (!answers.getAnswersets().isEmpty()) {
            AnswerSet a = answers.getAnswersets().get(0);

            try {

                for (Object obj : a.getAtoms()) {
                    if (obj instanceof Move) {
                        Move move = (Move) obj;
                        commands.add(move.toUpperString());
                    }
                    if (obj instanceof Torpedo) {
                        Torpedo torpedo = (Torpedo) obj;
                        commands.add(" | " + torpedo.toUpperString());
                    }
                }

                System.out.println(commands);
            }
            catch (Exception e) { e.printStackTrace(); }
        }

//        for (AnswerSet a : answers.getAnswersets()) {
//            System.out.println("AS n.: " + ++n);
//            try {
//
//                for (Object obj : a.getAtoms()) {
//                    if (obj instanceof Move) {
//                        Move move = (Move) obj;
//                        commands.add(move.toUpperString());
//                    }
//                    if (obj instanceof Torpedo) {
//                        Torpedo torpedo = (Torpedo) obj;
//                        commands.add(" | " + torpedo.toUpperString());
//                    }
//                }
//
//                System.out.println(commands);
//            }
//            catch (Exception e) { e.printStackTrace(); }
//        }

        return commands;
    }
}
