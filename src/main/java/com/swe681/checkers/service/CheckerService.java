package com.swe681.checkers.service;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.model.request.GameRequest;

public interface CheckerService {

    public GameInfo createNewGame(GameRequest gameRequest);
}
