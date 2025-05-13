package com.codingame.game.custom.players;

import com.codingame.game.custom.ASPclasses.Move;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.util.Arrays;
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

    public static void main(String[] args) { new AIPlayer().handleGameCycles(); }

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
        return super.chooseNextAction();
    }
}
