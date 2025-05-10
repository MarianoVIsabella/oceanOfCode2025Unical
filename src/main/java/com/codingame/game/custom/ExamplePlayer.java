package com.codingame.game.custom;

public class ExamplePlayer extends GenericPlayer {

    public static void main(String[] args) { new ExamplePlayer().handleGameCycles(args); }

    @Override
    protected void chooseAndPrintNextAction() {}

    @Override
    protected void chooseAndPrintInitialPosition() {}
}
