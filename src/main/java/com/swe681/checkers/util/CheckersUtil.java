package com.swe681.checkers.util;

import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.game.checkers.Space;

public class CheckersUtil {

    public static Space[][] initializeGameBoard() {
        Space[][] checkersBoard = new Space[8][8];
        int pieceCounter = 1;
        //Create top 3 rows for Red pieces on the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                Space space = new Space();
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        setPieceInSpace(space, j, i, false, null);
                    } else {
                        Piece piece = new Piece();
                        piece.setColor("Red");
                        piece.setPieceName("Red-"+pieceCounter++);
                        piece.setType("Pawn");
                        setPieceInSpace(space, j, i, true, piece);
                    }
                } else {
                    if (j % 2 == 0) {
                        Piece piece = new Piece();
                        piece.setColor("Red");
                        piece.setPieceName("Red-"+pieceCounter++);
                        piece.setType("Pawn");
                        setPieceInSpace(space, j, i, true, piece);
                    } else {
                        setPieceInSpace(space, j, i, false, null);
                    }
                }
                checkersBoard[i][j] = space;
            }
        }

        pieceCounter = 1;
        //Create bottom 3 rows for Black pieces on the board
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Space space = new Space();
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        setPieceInSpace(space, j, i, false, null);
                    } else {
                        Piece piece = new Piece();
                        piece.setColor("Black");
                        piece.setPieceName("Black-"+pieceCounter++);
                        piece.setType("Pawn");
                        setPieceInSpace(space, j, i, true, piece);
                    }
                } else {
                    if (j % 2 == 0) {
                        Piece piece = new Piece();
                        piece.setColor("Black");
                        piece.setPieceName("Black-"+pieceCounter++);
                        piece.setType("Pawn");
                        setPieceInSpace(space, j, i, true, piece);
                    } else {
                        setPieceInSpace(space, j, i, false, null);
                    }
                }
                checkersBoard[i][j] = space;
            }
        }

        // Create middle 2 space for playing
        for (int i=3; i < 5; i++) {
            for (int j = 0; j < 8; j++ ) {
                Space space = new Space();
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        setPieceInSpace(space, j, i, false, null);
                    } else {
                        setPieceInSpace(space, j, i, true, null);
                    }
                } else {
                    if (j % 2 == 0) {
                        setPieceInSpace(space, j, i, true, null);
                    } else {
                        setPieceInSpace(space, j, i, false, null);
                    }
                }
                checkersBoard[i][j] = space;
            }

        }

        return checkersBoard;
    }

    private static void setPieceInSpace(Space space, int col, int row, boolean playable, Piece piece) {
        space.setCol(col);
        space.setRow(row);
        space.setPlayable(playable);
        space.setPiece(piece);
    }
}
