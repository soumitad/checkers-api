package com.swe681.checkers.model.game.checkers;

public class Piece {
    private String pieceName;
    private String color;
    private String type;
    private String rowNum;
    private String colNum;

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public String getColNum() {
        return colNum;
    }

    public void setColNum(String colNum) {
        this.colNum = colNum;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPieceName() {
        return pieceName;
    }

    public void setPieceName(String pieceName) {
        this.pieceName = pieceName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
