package com.swe681.checkers.controller;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.model.response.CheckersMoveResponse;
import com.swe681.checkers.service.CheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/checkers")
public class CheckersController {

    @Autowired
    private CheckerService checkerService;

    @RequestMapping(value = "/game", method = RequestMethod.POST)
    public ResponseEntity<GameInfo> createNewGame(@RequestBody GameRequest gameRequest) {
        GameInfo gameplay = checkerService.createNewGame(gameRequest);
        if (gameplay != null) {
            return new ResponseEntity<>(gameplay, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/game/{gameId}", method = RequestMethod.GET)
    public ResponseEntity<GameInfo> fetchCheckersBoard(@PathVariable("gameId") String gameId) {
        GameInfo gameplay = checkerService.fetchCheckersBoard(gameId);
        if (gameplay != null) {
            return new ResponseEntity<>(gameplay, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/{gameId}/moves", method = RequestMethod.GET)
    public ResponseEntity<Space[]> fetchLegalMoves(@PathVariable("gameId") String gameId,
                                            @RequestParam("color") String color,
                                                    @RequestParam("pieceId") String pieceId,
                                                    @RequestParam("currentPosition") String currentPosition,
                                                    @RequestParam("type") String type) {
        GamePlayRequest gamePlayRequest = new GamePlayRequest(gameId, color, type, pieceId, currentPosition);
        Space[] allowedMoves = checkerService.fetchLegalMoves(gamePlayRequest);
        return new ResponseEntity<>(allowedMoves, HttpStatus.OK);

    }

    @RequestMapping(value = "/{gameId}/space", method = RequestMethod.PUT)
    public ResponseEntity<CheckersMoveResponse> performMove(@PathVariable("gameId") String gameId,
                                                            @RequestBody GamePlayRequest gamePlayRequest) {

        CheckersMoveResponse checkersMoveResponse = checkerService.performMove(gamePlayRequest);
        return new ResponseEntity<>(checkersMoveResponse, HttpStatus.OK);

    }
}
