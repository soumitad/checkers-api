package com.swe681.checkers.service;

import com.swe681.checkers.dao.GameDao;
import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.GamePlay;
import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.model.response.CheckersMoveResponse;
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
    public Space[] fetchLegalMoves(GamePlayRequest gamePlayRequest) {
        String[] moveArray = gamePlayRequest.getCurrentPosition().split("-");
        List<String> allowedLegalMove = calculateDiagonal(gamePlayRequest,
                Integer.parseInt(moveArray[0]), Integer.parseInt(moveArray[1]),
                gamePlayRequest.getColor(), gamePlayRequest.getType());

        Space[] allowedSpaces = new Space[allowedLegalMove.size()];
        int count = 0;
        for (String legalMove: allowedLegalMove) {
            Space space = new Space();
            String[] move = legalMove.split("-");
            space.setRow(Integer.parseInt(move[0]));
            space.setCol(Integer.parseInt(move[1]));
            space.setHighlight(true);
            allowedSpaces[count++] = space;
        }

        return allowedSpaces;
    }


    private boolean isJumpPossible(GamePlayRequest gamePlayRequest, int row, int col) {
        // Need to determine if Jump is possible
        if (col < 0 || col > 7 || row < 0 || row > 7) {
            return false;
        }
        Piece blockerPiece = gameDao.fetchPieceByPosition(gamePlayRequest, row, col);
        if (blockerPiece == null) {
                // jump possible
            return true;
        } else {
                // jump not possible
            return false;
        }
    }

    @Override
    public CheckersMoveResponse performMove(GamePlayRequest gamePlayRequest) {
        GameInfo gameInfo = gameDao.fetchGame(gamePlayRequest.getGameId());
        CheckersMoveResponse response = new CheckersMoveResponse();
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
        int status = gameDao.performMove(gamePlayRequest, isCurrentMoveJump, response);
        //Call isWinner to check if game can end
        response.setWinner(isWinner(gamePlayRequest));
        // Call isKing to check if the piece has turned into a King
        response.setKing(isKing(gamePlayRequest));
        String nextPlayerTurn = "";
        if (gamePlayRequest.getColor().equalsIgnoreCase("Black")) {
            nextPlayerTurn = gameInfo.getPlayer2();
        } else {
            nextPlayerTurn = gameInfo.getPlayer1();
        }
        if (status == 1) {
            gameDao.updateCurrentPlayerTurn(gamePlayRequest.getGameId(), nextPlayerTurn);
            response.setNextPlayerTurn(nextPlayerTurn);
            response.setMoveStatus(true);
        }
        response.setJump(isCurrentMoveJump);
        return response;
    }

    @Override
    public GameInfo fetchCheckersBoard(String gameId) {
        GameInfo gameInfo = new GameInfo();
        Space[][] initialCheckersBoard = util.createEmptySpaces(gameId);
        List<GamePlay> gamePlayList = gameDao.fetchCheckersBoardPieces(gameId);
        for (GamePlay gamePlay: gamePlayList) {
            Piece piece = new Piece();
            piece.setRowNum(String.valueOf(gamePlay.getRowNum()));
            piece.setColNum(String.valueOf(gamePlay.getColNum()));
            piece.setColor(gamePlay.getColor());
            piece.setType(gamePlay.getType());
            piece.setPieceName(gamePlay.getPieceId());
            Space space = initialCheckersBoard[gamePlay.getRowNum()][gamePlay.getColNum()];
            space.setPiece(piece);
            initialCheckersBoard[gamePlay.getRowNum()][gamePlay.getColNum()] = space;
            if (gamePlay.getColor().equalsIgnoreCase("Black")) {
                gameInfo.setPlayer1(gamePlay.getPlayerId());
            } else {
                gameInfo.setPlayer2(gamePlay.getPlayerId());
            }
        }
        gameInfo.setGameBoard(initialCheckersBoard);
        gameInfo.setGameId(gameId);
        GameInfo tempGameInfo = gameDao.fetchGame(gameId);
        gameInfo.setStatus(tempGameInfo.getStatus());
        gameInfo.setCurrentTurn(tempGameInfo.getCurrentTurn());
        gameInfo.setTimeSinceLastMove(tempGameInfo.getTimeSinceLastMove());
        return gameInfo;
    }

    @Override
    public List<GameInfo> fetchExistingUserGames(String username) {
       return gameDao.fetchExistingUserGames(username);
    }

    /**
     * Checks to see if the current move has resulted in creation of a King
     * @param gamePlayRequest
     * @return
     */
    private boolean isKing(GamePlayRequest gamePlayRequest) {
        String movePosition = gamePlayRequest.getMovePosition();
        String[] movePosArray = movePosition.split("-");
        if (gamePlayRequest.getColor().equalsIgnoreCase("Red")) {
            if (Integer.parseInt(movePosArray[0]) == 7) {
                gameDao.updatePieceToKing(gamePlayRequest);
                return true;
            }
        } else {
            if (Integer.parseInt(movePosArray[0]) == 0) {
                gameDao.updatePieceToKing(gamePlayRequest);
                return true;
            }
        }
        return false;
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
        List<Piece> pieceList = gameDao.getAllPiecesByColor(gamePlayRequest.getGameId(),
                gamePlayRequest.getColor().equalsIgnoreCase("Red") ? "Black" : "Red");
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
            if ((row > 0 && row <= 7) && (col < 7)) {
                possibleMovesList.add((row - 1) + "-" + (col + 1));
            }
            if ((row > 0 && row <= 7) && (col > 0)) {
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
            if ((row >= 0 && row < 7) && (col < 7)) {
                possibleMovesList.add((row + 1) + "-" + (col + 1));
            }
            if ((row >= 0 && row < 7) && (col > 0)) {
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
                            allowedMovesList.add(Integer.parseInt(moveArray[0]) - 1 + "-"+ (Integer.parseInt(moveArray[1]) + 1));
                        }
                    } else {
                        if(isJumpPossible(gamePlayRequest,
                                Integer.parseInt(moveArray[0]) - 1, Integer.parseInt(moveArray[1]) -1)) {
                            allowedMovesList.add(Integer.parseInt(moveArray[0]) - 1 + "-"+ (Integer.parseInt(moveArray[1]) -1));
                        }
                    }
                } else {
                    if (type.equalsIgnoreCase("King")) {
                        if(col < Integer.parseInt(moveArray[1])) {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) + 1, Integer.parseInt(moveArray[1]) + 1)) {
                                allowedMovesList.add(Integer.parseInt(moveArray[0]) + 1 + "-" + (Integer.parseInt(moveArray[1]) + 1));
                            }
                        } else {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) + 1, Integer.parseInt(moveArray[1]) -1)) {
                                allowedMovesList.add(Integer.parseInt(moveArray[0]) + 1 + "-" + (Integer.parseInt(moveArray[1]) -1));
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
                            allowedMovesList.add(Integer.parseInt(moveArray[0]) + 1 + "-" + (Integer.parseInt(moveArray[1]) + 1));
                        }
                    } else {
                        if(isJumpPossible(gamePlayRequest,
                                Integer.parseInt(moveArray[0]) + 1, Integer.parseInt(moveArray[1]) - 1)) {
                            allowedMovesList.add(Integer.parseInt(moveArray[0]) + 1 + "-" + (Integer.parseInt(moveArray[1]) - 1));
                        }
                    }
                } else {
                    if (type.equalsIgnoreCase("King")) {
                        if(col < Integer.parseInt(moveArray[1])) {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) - 1, Integer.parseInt(moveArray[1]) + 1)) {
                                allowedMovesList.add(Integer.parseInt(moveArray[0]) - 1 + "-" + (Integer.parseInt(moveArray[1]) + 1));
                            }
                        } else {
                            if(isJumpPossible(gamePlayRequest,
                                    Integer.parseInt(moveArray[0]) - 1, Integer.parseInt(moveArray[1]) - 1)) {
                                allowedMovesList.add(Integer.parseInt(moveArray[0]) - 1 + "-" + (Integer.parseInt(moveArray[1]) - 1));
                            }
                        }
                    }
                }

            }
        }
        return allowedMovesList;
    }

}
