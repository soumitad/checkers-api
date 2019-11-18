package com.soumita.checkersapi.controller;

import com.soumita.checkersapi.model.User;
import com.soumita.checkersapi.model.game.checkers.Space;
import com.soumita.checkersapi.service.CheckerService;
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
    public ResponseEntity<Space[][]> getUserInfo() {
        Space[][] board = checkerService.createNewGame();
        return new ResponseEntity<>(board, HttpStatus.OK);
    }
}
