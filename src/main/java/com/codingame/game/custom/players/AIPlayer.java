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

public class AIPlayer extends GenericPlayer {

    // Path to ASP File to be excecuted
    protected static String encodingResource="encodings/prova";

    // ASP Handler
    protected static Handler handler;

    // Input Container for ASP Program
    protected static InputProgram inputProgram;



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
    }

    @Override
    protected void chooseAndPrintInitialPosition() {
        super.chooseAndPrintInitialPosition();
    }

    @Override
    protected void chooseAndPrintNextAction() {
        super.chooseAndPrintNextAction();
    }
}
