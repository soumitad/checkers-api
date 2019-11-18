package com.swe681.checkers.model.game.checkers;

public class GameInfo {
    private String gameId;
    private String player1;
    private String player2;
    private Space[][] gameBoard;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Space[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Space[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

}
