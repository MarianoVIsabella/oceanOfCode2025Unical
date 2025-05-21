package com.codingame.game.custom.ASPclasses;

public interface ASPCommand {

    // Time Ordering of Actions
    int getTimeOrder();
    void setTimeOrder(int time);

    // To Print Command in Game Format
    String toUpperString();
}
