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
        String[] moveArray = gamePlayRequest.getCurrentPosition().split("-");
        List<String> allowedLegalMove = calculateDiagonal(gamePlayRequest,
                Integer.parseInt(moveArray[0]), Integer.parseInt(moveArray[1]),
                gamePlayRequest.getColor(), gamePlayRequest.getType());

        return allowedLegalMove;
    }


    private boolean isJumpPossible(GamePlayRequest gamePlayRequest, int row, int col) {
        // Need to determine if Jump is possible
        if (col < 0 || col > 7 || row < 0 || row > 7) {
            return false;
        }
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
        String currentPosition = gamePlayRequest.getCurrentPosition();
        String movePosition = gamePlayRequest.getMovePosition();
        boolean isCurrentMoveJump = false;

        String[] currentPosArray = new String[2];
        String[] movePosArray = new String[2];
        currentPosArray = currentPosition.split("-");
        movePosArray = movePosition.split("-");

        if (Math.abs(Integer.parseInt(movePosArray[0])
                - Integer.parseInt(currentPosArray[0])) > 1) {
            isCurrentMoveJump = true;
        }

        //Call isWinner to check if game can end

        //Step1: Get list of all existing pieces of opposite color
        //Step2: If no piece exists, current color is the winner
        //Step3: If pieces exist, for each piece call calculateDiagonal(), If even 1 move exist, break out
        return isWinner(gamePlayRequest);
    }



    /**
     * Method which is called after every move for a particular color to check, if its a winner
     * @param gamePlayRequest
     * @return
     */
    private boolean isWinner(GamePlayRequest gamePlayRequest) {
        //Fetch list of remaining color pieces and their current position
        //For each piece, call legalMoves to check if one exists,
        //If even one legal move exists, the color is not winner, use break statement
        List<Piece> pieceList = gameDao.getAllPiecesByColor(gamePlayRequest.getGameId(), gamePlayRequest.getColor());
        if(pieceList == null) {
            return true;
        } else {
            for (Piece piece: pieceList) {
                List<String> movesList = calculateDiagonal(gamePlayRequest,
                        Integer.parseInt(piece.getRowNum()),
                        Integer.parseInt(piece.getColNum()),
                        gamePlayRequest.getColor(),
                        piece.getType());
                if (movesList.size() > 0) {
                    return false;
                } else {
                    continue;
                }
            }
        }
        return true;
    }

    //Audit trail of the Checkers Game
    //GameId - Color - Piece Id - MoveFrom - MoveTo


    //Determine Legal move for a Piece (Pawn or a King)

    /**
     * 1. Fetch current row and col for the Piece
     * 2. Calculate 4 possible row and diagonals for the Piece
     * 3. If piece is Pawn, eliminate 2 of the diagonals
     */

    private List<String> calculateDiagonal(GamePlayRequest gamePlayRequest, int row, int col, String color, String type) {
        List<String> possibleMovesList = new ArrayList<>();
        List<String> allowedMovesList = new ArrayList<>();

        // Diagonal 2
        if (color.equalsIgnoreCase("Black")) {
            if ((row > 0 && row < 7) && (col < 7)) {
                possibleMovesList.add((row - 1) + "-" + (col + 1));
            }
            if ((row > 0 && row < 7) && (col > 0)) {
                possibleMovesList.add((row - 1) + "-" + (col - 1));
            }

            if (type.equalsIgnoreCase("King")) {
                if ((row < 7) && (col < 7)) {
                    possibleMovesList.add((row + 1) + "-" + (col + 1));
                }
                if ((row < 7) && (col > 0)) {
                    possibleMovesList.add((row + 1) + "-" + (col - 1));
                }
            }

        } else {
            if ((row > 0 && row < 7) && (col < 7)) {
                possibleMovesList.add((row + 1) + "-" + (col + 1));
            }
            if ((row > 0 && row < 7) && (col > 0)) {
                possibleMovesList.add((row + 1) + "-" + (col - 1));
            }

            if (type.equalsIgnoreCase("King")) {
                if ((row > 0) && (col < 7)) {
                    possibleMovesList.add((row - 1) + "-" + (col + 1));
                }
                if ((row > 0) && (col > 0)) {
                    possibleMovesList.add((row - 1) + "-" + (col - 1));
                }
            }
        }

        for (String move: possibleMovesList) {
            String[] moveArray = move.split("-");
            Piece piece = gameDao.fetchPieceByPosition(gamePlayRequest,
                    Integer.parseInt(moveArray[0]), Integer.parseInt(moveArray[1]));
            if (piece == null) {
                allowedMovesList.add(move);
            } else if (color.equalsIgnoreCase("Black")
                    && piece.getColor().equalsIgnoreCase("Red")) {
                if (Integer.parseInt(moveArray[0]) < row) {
                    if(col < Integer.parseInt(moveArray[1])) {
                        if(isJumpPossible(gamePlayRequest,
                                Integer.parseInt(moveArray[0]) - 1, Integer.parseInt(moveArray[1]) + 1)) {
                            allowedMovesList.add(move);
                        }
                    } else {
                        if(isJumpPossible(gamePlayRequest,
                                Integer.parseInt(moveArray[0]) - 1, Integer.parseInt(moveArray[1]) -1)) {
                            allowedMovesList.add(move);
                        }
                    }
                } else {
                    if (type.equalsIgnoreCase("King")) {
                        if(col < Integer.parseInt(moveArray[1])) {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) + 1, Integer.parseInt(moveArray[1]) + 1)) {
                                allowedMovesList.add(move);
                            }
                        } else {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) + 1, Integer.parseInt(moveArray[1]) -1)) {
                                allowedMovesList.add(move);
                            }
                        }
                    }
                }
            } else if (color.equalsIgnoreCase("Red")
                    && piece.getColor().equalsIgnoreCase("Black")) {

                if (Integer.parseInt(moveArray[0]) > row) {
                    if(col < Integer.parseInt(moveArray[1])) {
                        if(isJumpPossible(gamePlayRequest,
                                Integer.parseInt(moveArray[0]) + 1, Integer.parseInt(moveArray[1]) + 1)) {
                            allowedMovesList.add(move);
                        }
                    } else {
                        if(isJumpPossible(gamePlayRequest,
                                Integer.parseInt(moveArray[0]) + 1, Integer.parseInt(moveArray[1]) - 1)) {
                            allowedMovesList.add(move);
                        }
                    }
                } else {
                    if (type.equalsIgnoreCase("King")) {
                        if(col < Integer.parseInt(moveArray[1])) {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) - 1, Integer.parseInt(moveArray[1]) + 1)) {
                                allowedMovesList.add(move);
                            }
                        } else {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) - 1, Integer.parseInt(moveArray[1]) - 1)) {
                                allowedMovesList.add(move);
                            }
                        }
                    }
                }

            }
        }
        return allowedMovesList;
    }

}
