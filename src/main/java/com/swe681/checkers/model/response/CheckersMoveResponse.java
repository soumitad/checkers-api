package com.swe681.checkers.model.response;

import com.swe681.checkers.model.game.checkers.Space;

public class CheckersMoveResponse {
    private Space move;
    private boolean jump;
    private boolean winner;
    private boolean king;
    private boolean moveStatus;
    private boolean doubleJumpPossible;
    private Space doubleJumpSpace;
    private String nextPlayerTurn;

    public String getNextPlayerTurn() {
        return nextPlayerTurn;
    }

    public void setNextPlayerTurn(String nextPlayerTurn) {
        this.nextPlayerTurn = nextPlayerTurn;
    }
    public boolean isDoubleJumpPossible() {
        return doubleJumpPossible;
    }

    public void setDoubleJumpPossible(boolean doubleJumpPossible) {
        this.doubleJumpPossible = doubleJumpPossible;
    }

    public Space getDoubleJumpSpace() {
        return doubleJumpSpace;
    }

    public void setDoubleJumpSpace(Space doubleJumpSpace) {
        this.doubleJumpSpace = doubleJumpSpace;
    }

    public Space getMove() {
        return move;
    }

    public void setMove(Space move) {
        this.move = move;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public boolean isKing() {
        return king;
    }

    public void setKing(boolean king) {
        this.king = king;
    }

    public boolean isMoveStatus() {
        return moveStatus;
    }

    public void setMoveStatus(boolean moveStatus) {
        this.moveStatus = moveStatus;
    }

}
