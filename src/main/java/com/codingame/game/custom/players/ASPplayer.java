package com.codingame.game.custom.players;

import com.codingame.game.custom.ASPclasses.Move;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
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

public class ASPplayer {

    public static String encodingResource="encodings/prova";

    public static Handler handler;

    public static void main(String[] args) {

        System.out.println("7 7");

        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

        try {
            ASPMapper.getInstance().registerClass(Move.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e1) {
            System.err.println(Arrays.toString(e1.getStackTrace()));
        }

        InputProgram encoding= new ASPInputProgram();
        encoding.addFilesPath(encodingResource);

        handler.addProgram(encoding);

        // inserire la condizione di fine gioco, no continua a richiamare dlv all'infinito
        // leggere dall'input i punti vita propri e dell'avversario (?)
        for (int i=0; i<6; i++) {
            Output o = handler.startSync();

            AnswerSets answersets = (AnswerSets) o;

            for (AnswerSet a : answersets.getAnswersets()) {
                try {

                    for (Object obj : a.getAtoms()) {
                        if (!(obj instanceof Move)) continue;
                        Move move = (Move) obj;
                        System.out.println(move.toUpperString());
                    }
                } catch (Exception e) {
                    System.err.println(Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }
}
