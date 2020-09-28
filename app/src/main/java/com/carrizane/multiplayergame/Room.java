package com.carrizane.multiplayergame;

public class Room {

    private String players;
    private String nameRoom;

    public Room(String players, String nameRoom) {
        this.players = players;
        this.nameRoom = nameRoom;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }
}
