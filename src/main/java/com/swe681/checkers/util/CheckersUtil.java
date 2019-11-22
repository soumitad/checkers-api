package com.swe681.checkers.util;

import com.swe681.checkers.dao.GameDao;
import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.game.checkers.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckersUtil {

    @Autowired
    private GameDao gameDao;

    public Space[][] initializeGameBoard(GameInfo gameInfo) {
        CheckersUtil util = new CheckersUtil();
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
                        gameDao.insertPieceInfo(piece, i, j, gameInfo.getPlayer2(), Integer.parseInt(gameInfo.getGameId()));
                    }
                } else {
                    if (j % 2 == 0) {
                        Piece piece = new Piece();
                        piece.setColor("Red");
                        piece.setPieceName("Red-"+pieceCounter++);
                        piece.setType("Pawn");
                        setPieceInSpace(space, j, i, true, piece);
                        gameDao.insertPieceInfo(piece, i, j, gameInfo.getPlayer2(), Integer.parseInt(gameInfo.getGameId()));
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
                        gameDao.insertPieceInfo(piece, i, j, gameInfo.getPlayer1(), Integer.parseInt(gameInfo.getGameId()));
                    }
                } else {
                    if (j % 2 == 0) {
                        Piece piece = new Piece();
                        piece.setColor("Black");
                        piece.setPieceName("Black-"+pieceCounter++);
                        piece.setType("Pawn");
                        setPieceInSpace(space, j, i, true, piece);
                        gameDao.insertPieceInfo(piece, i, j, gameInfo.getPlayer1(), Integer.parseInt(gameInfo.getGameId()));
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

    public Map<Integer, Integer> getRowMovementForBlackPawn() {
        Map<Integer, Integer> blackPawnMap = new HashMap<>();
        blackPawnMap.put(7, 6);
        blackPawnMap.put(6, 5);
        blackPawnMap.put(5, 4);
        blackPawnMap.put(4, 3);
        blackPawnMap.put(3, 2);
        blackPawnMap.put(2, 1);
        blackPawnMap.put(1, 0);

        return blackPawnMap;
    }

    public Map<Integer, List<Integer>> allowedColMovementForPieces() {
        Map<Integer, List<Integer>> columnMovementMap = new HashMap();
        List<Integer> tempList = new ArrayList<>();
        tempList.add(1);
        columnMovementMap.put(0, tempList);
        tempList = new ArrayList<>();
        tempList.add(0);
        tempList.add(2);
        columnMovementMap.put(1, tempList);
        tempList = new ArrayList<>();
        tempList.add(1);
        tempList.add(3);
        columnMovementMap.put(2, tempList);
        tempList = new ArrayList<>();
        tempList.add(2);
        tempList.add(4);
        columnMovementMap.put(3, tempList);
        tempList = new ArrayList<>();
        tempList.add(3);
        tempList.add(5);
        columnMovementMap.put(4, tempList);
        tempList = new ArrayList<>();
        tempList.add(4);
        tempList.add(6);
        columnMovementMap.put(5, tempList);
        tempList = new ArrayList<>();
        tempList.add(5);
        tempList.add(7);
        columnMovementMap.put(6, tempList);
        tempList = new ArrayList<>();
        tempList.add(6);
        columnMovementMap.put(7, tempList);
        return columnMovementMap;
    }

    public Map<Integer, Integer> getRowMovementForRedPawn() {
        Map<Integer, Integer> whitePawnMap = new HashMap<>();
        whitePawnMap.put(6, 7);
        whitePawnMap.put(5, 6);
        whitePawnMap.put(4, 5);
        whitePawnMap.put(3, 4);
        whitePawnMap.put(2, 3);
        whitePawnMap.put(1, 2);
        whitePawnMap.put(0, 1);

        return whitePawnMap;
    }



    private void setPieceInSpace(Space space, int col, int row, boolean playable, Piece piece) {
        space.setCol(col);
        space.setRow(row);
        space.setPlayable(playable);
        space.setPiece(piece);
    }
}
