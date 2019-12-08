package com.swe681.checkers.service;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.model.response.CheckersMoveResponse;

import java.util.List;

public interface CheckerService {

    public GameInfo createNewGame(GameRequest gameRequest);
    public Space[] fetchLegalMoves(GamePlayRequest gamePlayRequest);
    public CheckersMoveResponse performMove(GamePlayRequest gamePlayRequest);
    public GameInfo fetchCheckersBoard(String gameId);
    public List<GameInfo> fetchExistingUserGames(String username);
}
