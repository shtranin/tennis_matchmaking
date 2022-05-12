package com.kefx.tennis_matchmaking.documents;

public enum Process {
    registration("reg"),
    choosingUser("choosing");


    private String name;
    Process(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
