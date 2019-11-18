package com.swe681.checkers.service;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.util.CheckersUtil;
import org.springframework.stereotype.Service;

@Service
public class CheckerServiceImpl implements CheckerService{
    @Override
    public GameInfo createNewGame() {
        Space[][] gameBoard = CheckersUtil.initializeGameBoard();
        return null;
    }


}
