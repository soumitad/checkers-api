package com.swe681.checkers.service;

import com.swe681.checkers.dao.GameDao;
import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.util.CheckersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckerServiceImpl implements CheckerService{

    @Autowired
    GameDao gameDao;
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
        gameInfo.setGameBoard(CheckersUtil.initializeGameBoard());
        return gameInfo;
    }


}
