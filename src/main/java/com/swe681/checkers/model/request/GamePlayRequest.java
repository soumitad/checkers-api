package com.swe681.checkers.model.request;

public class GamePlayRequest {
    private String gameId;
    private String color;
    private String type;
    private String pieceId;
    private String currentPosition;
    private String movePosition;

    public GamePlayRequest(String gameId, String color, String type, String pieceId, String currentPosition) {
        this.gameId = gameId;
        this.color = color;
        this.type = type;
        this.pieceId = pieceId;
        this.currentPosition = currentPosition;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPieceId() {
        return pieceId;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getMovePosition() {
        return movePosition;
    }

    public void setMovePosition(String movePosition) {
        this.movePosition = movePosition;
    }
}
