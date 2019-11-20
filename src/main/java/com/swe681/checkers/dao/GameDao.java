package com.swe681.checkers.dao;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;

public interface GameDao {

    public int createGame(GameRequest gameRequest);
    public int insertPieceInfo(Piece piece, int row, int col, String playerId, int gameId);
    public Piece fetchPieceByPosition(GamePlayRequest gamePlayRequest, int row, int col);
}
