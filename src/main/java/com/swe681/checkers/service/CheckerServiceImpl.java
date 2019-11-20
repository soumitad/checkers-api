package com.swe681.checkers.service;

import com.swe681.checkers.dao.GameDao;
import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.util.CheckersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String[] fetchLegalMoves(String gameId, String color, String currentPosition, String pieceId) {
        String[] positionArray = currentPosition.split("-");

        return new String[0];
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
        //Fetch list of remaining White pieces and their current position
        //For each piece, call legalMoves to check if one exists,
        //If even one legal move exists, the color is not winner, use break statement

        return false;
    }


}
