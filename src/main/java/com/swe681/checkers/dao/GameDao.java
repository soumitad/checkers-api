package com.swe681.checkers.dao;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.request.GameRequest;

public interface GameDao {

    public int createGame(GameRequest gameRequest);
}
