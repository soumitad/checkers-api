package com.swe681.checkers.controller;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.service.CheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/checkers")
public class CheckersController {

    @Autowired
    private CheckerService checkerService;

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public ResponseEntity<GameInfo> createNewGame() {
        GameInfo gameplay = checkerService.createNewGame();
        return new ResponseEntity<>(gameplay, HttpStatus.OK);
    }
}
