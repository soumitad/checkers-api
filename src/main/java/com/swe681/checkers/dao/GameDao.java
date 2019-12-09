package com.swe681.checkers.dao;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.GamePlay;
import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.model.response.CheckersMoveResponse;

import java.util.List;

public interface GameDao {

    public int createGame(GameRequest gameRequest);
    public int insertPieceInfo(Piece piece, int row, int col, String playerId, int gameId);
    public Piece fetchPieceByPosition(GamePlayRequest gamePlayRequest, int row, int col);
    public List<Piece> getAllPiecesByColor(String gameId, String color);
    public int updateGameAuditTrail(String gameId, String color, String playerId, String moveFrom, String moveTo);
    public int performMove(GamePlayRequest gamePlayRequest, boolean isJump, CheckersMoveResponse checkersMoveResponse);
    public List<GamePlay> fetchCheckersBoardPieces(String gameId);
    public List<GameInfo> fetchExistingUserGames(String username);
    public GameInfo fetchGame(String gameId);
    public int updateCurrentPlayerTurn(String gameId, String playerInfo);
}
