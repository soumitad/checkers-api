package com.swe681.checkers.service;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;

import java.util.List;

public interface CheckerService {

    public GameInfo createNewGame(GameRequest gameRequest);
    public List<String> fetchLegalMoves(GamePlayRequest gamePlayRequest);
    public boolean performMove(GamePlayRequest gamePlayRequest);
}
