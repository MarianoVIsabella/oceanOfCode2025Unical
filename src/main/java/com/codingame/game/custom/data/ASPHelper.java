package com.codingame.game.custom.data;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;

public class ASPHelper {
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
