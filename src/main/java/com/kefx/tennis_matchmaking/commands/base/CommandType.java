package com.kefx.tennis_matchmaking.commands.base;

public enum CommandType {

    registration("/registration"),
    showTable("/showTable"),
    start("/start"),
    askingName("reg1"),
    menu("/menu"),
    settings("/settings"),
    deleteUser("/deleteUser"),
    finallyDelete("/finallyDelete"),
    rename("/rename"),
    info("/info"),
    createGame("/createGame"),
    rival("rival"),
    player("player"),
    winner("winner"),
    cancel("cancel"),
    accept("accept"),
    nothing("nothing"),
    statistic("/statistic"),
    rating("/rating"),
    gameDetails("/gameDetails");



    private String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
