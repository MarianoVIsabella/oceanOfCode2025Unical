package com.codingame.game.custom.players;

import com.codingame.game.Player;
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

        //macOS
        //handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2-macOS-64bit.mac_5"));

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
        System.out.println("7 7"); return;
    }

    @Override
    protected void chooseAndPrintNextAction() {

        OptionDescriptor option = new OptionDescriptor("-n 0");
        handler.addOption(option);
        inputProgram.addFilesPath(encodingResource);
        handler.addProgram(inputProgram);
        Output o = handler.startSync();
        AnswerSets answers = (AnswerSets) o;
        int n = 0;
        for (AnswerSet a : answers.getAnswersets()) {
            System.out.println("AS n.: " + ++n);
            try {
                StringBuilder command = new StringBuilder();

                for (Object obj : a.getAtoms()) {
                    if (obj instanceof Move) {
                        Move move = (Move) obj;
                        command.append(move.toUpperString());
                    }
                    if (obj instanceof Torpedo) {
                        Torpedo torpedo = (Torpedo) obj;
                        command.append(" | ").append(torpedo.toUpperString());
                    }
                }

                System.out.println(command);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
