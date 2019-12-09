package com.swe681.checkers.model.game.checkers;

public class GameInfo {
    private String gameId;
    private String player1;
    private String player2;
    private Space[][] gameBoard;
    private String status;
    private String currentTurn;
    private long timeSinceLastMove;

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public long getTimeSinceLastMove() {
        return timeSinceLastMove;
    }
    public void setTimeSinceLastMove(long timeSinceLastMove) {
        this.timeSinceLastMove = timeSinceLastMove;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
