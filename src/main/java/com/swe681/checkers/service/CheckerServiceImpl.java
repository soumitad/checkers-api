package com.swe681.checkers.service;

import com.swe681.checkers.dao.GameDao;
import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.util.CheckersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CheckerServiceImpl implements CheckerService{

    @Autowired
    GameDao gameDao;
    @Autowired
    CheckersUtil util;
    @Override
    public GameInfo createNewGame(GameRequest gameRequest) {
        GameInfo gameInfo = null;
        int gamePlayId = gameDao.createGame(gameRequest);
        if (gamePlayId > 0) {
            gameInfo = new GameInfo();
            gameInfo.setPlayer1(gameRequest.getPlayer1());
            gameInfo.setPlayer2(gameRequest.getPlayer2());
            gameInfo.setGameId(String.valueOf(gamePlayId));
        } else {
            return null;
        }
        gameInfo.setGameBoard(util.initializeGameBoard(gameInfo));
        return gameInfo;
    }

    @Override
    public List<String> fetchLegalMoves(GamePlayRequest gamePlayRequest) {
        List<String> allowedLegalMove = new ArrayList<>();
        Map<Integer, Integer> allowedRowMovement;
        Map<Integer, List<Integer>> allowedColMovement = util.allowedColMovementForPieces();
        int row = 0;
        String[] positionArray = gamePlayRequest.getCurrentPosition().split("-");
        int legalMovesCounter = 0;
        if (gamePlayRequest.getColor().equalsIgnoreCase("Red")) {
            allowedRowMovement = util.getRowMovementForRedPawn();
            row = allowedRowMovement.get(positionArray[0]);
            List<Integer> allowedColsList = allowedColMovement.get(positionArray[1]);
            //Iterate the Column list and combine with row to check if any piece exists
            for (Integer col: allowedColsList) {
                //Check if row - col combo already has a piece and the color of piece to
                Piece piece = gameDao.fetchPieceByPosition(gamePlayRequest, row, col);
                if (piece == null) {
                    allowedLegalMove.add(row + "-" + col) ;
                } else if (piece != null && piece.getColor().equalsIgnoreCase("Black")) {
                    if (col == Integer.parseInt(positionArray[1]) + 1 && (col + 1) <= 7) {
                        if(isJumpPossible(gamePlayRequest, row + 1, col + 1)) {
                            //Add the row - col combo to the legal moves array
                            allowedLegalMove.add((row + 1) + "-" + (col + 1));
                        }
                    } else if (col == Integer.parseInt(positionArray[1]) - 1 && (col - 1) >= 0) {
                        if(isJumpPossible(gamePlayRequest, row + 1, col - 1)) {
                            //Add the row - col combo to the legal moves array
                            allowedLegalMove.add((row + 1) + "-" + (col -1 ));
                        }
                    }
                }
            }

        } else {
            //Fetch legal moves for Black piece
            allowedRowMovement = util.getRowMovementForBlackPawn();
            row = allowedRowMovement.get(positionArray[0]);
            List<Integer> allowedColsList = allowedColMovement.get(positionArray[1]);
            //Iterate the Column list and combine with row to check if any piece exists
            for (Integer col: allowedColsList) {
                //Check if row - col combo already has a piece and the color of piece to
                Piece piece = gameDao.fetchPieceByPosition(gamePlayRequest, row, col);
                if (piece == null) {
                    allowedLegalMove.add(row + "-" + col) ;
                } else if (piece != null && piece.getColor().equalsIgnoreCase("Red")) {
                    if (col == Integer.parseInt(positionArray[1]) + 1 && (col + 1) <= 7) {
                        if(isJumpPossible(gamePlayRequest, row -1 , col + 1)) {
                            //Add the row - col combo to the legal moves array
                            allowedLegalMove.add((row - 1) + "-" + (col + 1));
                        }
                    } else if (col == Integer.parseInt(positionArray[1]) - 1 && (col - 1) >= 0) {
                        if(isJumpPossible(gamePlayRequest, row + 1, col - 1)) {
                            //Add the row - col combo to the legal moves array
                            allowedLegalMove.add((row - 1) + "-" + (col -1 ));
                        }
                    }
                }
            }
        }

        return allowedLegalMove;
    }

    private void fetchAdditionalLegalMoveKing(GamePlayRequest gamePlayRequest) {

    }

    private boolean isJumpPossible(GamePlayRequest gamePlayRequest, int row, int col) {
        // Need to determine if Jump is possible
        Piece blockerPiece = gameDao.fetchPieceByPosition(gamePlayRequest, row + 1, col + 1);
        if (blockerPiece == null) {
                // jump possible
            return true;
        } else {
                // jump not possible
            return false;
        }
    }

    @Override
    public boolean performMove(GamePlayRequest gamePlayRequest) {
        //Update in DB with the new move
        //Call isWinner to check if game can end
        return false;
    }



    /**
     * Method which is called after every move for a particular color to check, if its a winner
     * @param color
     * @return
     */
    private boolean isWinner(String color) {
        //Fetch list of remaining color pieces and their current position
        //For each piece, call legalMoves to check if one exists,
        //If even one legal move exists, the color is not winner, use break statement

        return false;
    }

    //Audit trail of the Checkers Game
    //GameId - Color - Piece Id - MoveFrom - MoveTo


}
