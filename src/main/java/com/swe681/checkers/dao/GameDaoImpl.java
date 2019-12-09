package com.swe681.checkers.dao;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.GamePlay;
import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.game.checkers.Space;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import com.swe681.checkers.model.response.CheckersMoveResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static java.time.LocalTime.now;

@Service
public class GameDaoImpl implements GameDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int createGame(GameRequest gameRequest) {
        //Get the last id inserted into game table
        // "select "
        Integer lastGameplayId = fetchLastGameId();
        int status = jdbcTemplate.update(
                "INSERT INTO sdas22.game(gameId, player1, player2, status, created, winner, currentTurn, timeSinceLastTurn) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                lastGameplayId + 1, gameRequest.getPlayer1(), gameRequest.getPlayer2(),
                "InProgress",new java.sql.Timestamp(new java.util.Date().getTime()) , null, gameRequest.getPlayer1(), 0);
        if (status == 1) {
            return lastGameplayId + 1;
        } else {
            return 0;
        }

    }

    @Override
    public int insertPieceInfo(Piece piece, int row, int col, String playerId, int gameId) {
        int status = jdbcTemplate.update(
                "INSERT INTO sdas22.gameplay VALUES (?, ?, ?, ?, ?, ?, ?)",
                gameId, piece.getColor(), piece.getPieceName(),
                row, col, piece.getType(), playerId);
        return status;
    }

    @Override
    public Piece fetchPieceByPosition(GamePlayRequest gamePlayRequest, int row, int col) {
        String sql = "select color, pieceId, type, rowNum, colNum from sdas22.gameplay where rowNum=? and colNum=? and gameId=?";
        Piece piece;
        try{
            piece = jdbcTemplate.queryForObject(sql,
                    new Object[] { row, col, gamePlayRequest.getGameId() }, new PieceRowMapper());
        } catch (EmptyResultDataAccessException emptyException) {
            piece = null;
        }
        return piece;
    }

    @Override
    public List<Piece> getAllPiecesByColor(String gameId, String color) {
        String sql = "select color, pieceId, type, rowNum, colNum from sdas22.gameplay where gameId=? and color=?";
        List<Piece> pieceList;
        try{
            pieceList = jdbcTemplate.query(sql,
                    new PieceRowMapper(), gameId, color);
        } catch (EmptyResultDataAccessException emptyException) {
            pieceList = null;
        }
        return pieceList;
    }

    @Override
    public int updateGameAuditTrail(String gameId, String color,
                                    String playerId, String moveFrom, String moveTo) {

        return 0;
    }

    @Override
    public int performMove(GamePlayRequest gamePlayRequest, boolean isJump, CheckersMoveResponse checkersMoveResponse) {
        String moveSql = "Update sdas22.gameplay set rowNum=?, colNum=? where gameId=? and pieceId=?";
        String[] movePosArray = gamePlayRequest.getMovePosition().split("-");
        String[] currPosArray = gamePlayRequest.getCurrentPosition().split("-");
        int status = jdbcTemplate.update(
            moveSql,
            movePosArray[0], movePosArray[1], gamePlayRequest.getGameId(), gamePlayRequest.getPieceId());
        if (isJump) {
            int delRow = 0;
            int delCol = 0;
            if (Integer.parseInt(currPosArray[0]) > Integer.parseInt(movePosArray[0])) {
                if (Integer.parseInt(currPosArray[1]) > Integer.parseInt(movePosArray[1])) {
                    delRow = Integer.parseInt(currPosArray[0]) - 1;
                    delCol = Integer.parseInt(currPosArray[1]) - 1;
                } else {
                    delRow = Integer.parseInt(currPosArray[0]) - 1;
                    delCol = Integer.parseInt(currPosArray[1]) + 1;
                }
            } else {
                if (Integer.parseInt(currPosArray[1]) > Integer.parseInt(movePosArray[1])) {
                    delRow = Integer.parseInt(currPosArray[0]) + 1;
                    delCol = Integer.parseInt(currPosArray[1]) - 1;
                } else {
                    delRow = Integer.parseInt(currPosArray[0]) + 1;
                    delCol = Integer.parseInt(currPosArray[1]) + 1;
                }
            }

            String deleteSql = "Delete from sdas22.gameplay where rowNum=? and colNum=? and gameId=?";
            jdbcTemplate.update(
                deleteSql,
                delRow, delCol, gamePlayRequest.getGameId());
            Space jumpSpace = new Space();
            jumpSpace.setRow(delRow);
            jumpSpace.setCol(delCol);
            checkersMoveResponse.setDoubleJumpSpace(jumpSpace);
        }
        return status;
    }

    @Override
    public List<GamePlay> fetchCheckersBoardPieces(String gameId) {
        String sql = "select color, pieceId, type, rowNum, colNum, gameId, playerId from sdas22.gameplay where gameId=?";
        List<GamePlay> pieceList;
        try{
            pieceList = jdbcTemplate.query(sql,
                                           new GamePiecesRowMapper(), gameId);
        } catch (EmptyResultDataAccessException emptyException) {
            pieceList = null;
        }
        return pieceList;
    }

    public List<GameInfo> fetchExistingUserGames(String username) {
        String sql = "select gameId, player1, player2, status, currentTurn, timeSinceLastTurn from sdas22.game where player1=?";
        String sql2 = "select gameId, player1, player2, status, currentTurn, timeSinceLastTurn from sdas22.game where player2=?";

        List<GameInfo> gameList1;
        List<GameInfo> gameList2;
        try{
            gameList1 = jdbcTemplate.query(sql,
                    new GameInfoRowMapper(), username);
        } catch (EmptyResultDataAccessException emptyException) {
            gameList1 = null;
        }

        try{
            gameList2 = jdbcTemplate.query(sql2,
                    new GameInfoRowMapper(), username);
        } catch (EmptyResultDataAccessException emptyException) {
            gameList2 = null;
        }

        if (gameList1 != null) {
            gameList1.addAll(gameList2);
        } else {
            gameList1 = gameList2;
        }
        return gameList1;
    }

    @Override
    public GameInfo fetchGame(String gameId) {
        String sql = "select gameId, player1, player2, status, winner, currentTurn, timeSinceLastTurn from sdas22.game where gameId=?";
        List<GameInfo> gameList1;
        try{
            gameList1 = jdbcTemplate.query(sql,
                    new GameInfoRowMapper(), gameId);
        } catch (EmptyResultDataAccessException emptyException) {
            gameList1 = null;
        }
        if (gameList1 != null && gameList1.size() > 0) {
            return gameList1.get(0);
        }
        return null;
    }

    @Override
    public int updateCurrentPlayerTurn(String gameId, String playerInfo) {
        Instant instant = Instant.now();
        String statusSql = "Update sdas22.game set currentTurn=?, timeSinceLastTurn=? where gameId=?";
        int status = jdbcTemplate.update(
                statusSql,
                playerInfo,
                instant.toEpochMilli(),
                gameId);
        return status;
    }

    private Integer fetchLastGameId() {
        Integer id;
        String sql = "select gameId from sdas22.game order by gameId desc limit 1";
        try{
            id =  jdbcTemplate.queryForObject(
                    sql, new Object[]{}, Integer.class);
        } catch (EmptyResultDataAccessException emptyException) {
            System.out.println("No games exist, lets default to one!!");
            id = 100;
        }
        return id;
    }

    class PieceRowMapper implements RowMapper<Piece> {

        public Piece mapRow(ResultSet resultSet, int i) throws SQLException {
            Piece piece = new Piece();
            piece.setColor(resultSet.getString("color"));
            piece.setType(resultSet.getString("type"));
            piece.setPieceName(resultSet.getString("pieceId"));
            piece.setRowNum(resultSet.getString("rowNum"));
            piece.setColNum(resultSet.getString("colNum"));
            return piece;
        }
    }

    class GameInfoRowMapper implements RowMapper<GameInfo> {

        public GameInfo mapRow(ResultSet resultSet, int i) throws SQLException {
            GameInfo gameInfo = new GameInfo();
            gameInfo.setPlayer1(resultSet.getString("player1"));
            gameInfo.setPlayer2(resultSet.getString("player2"));
            gameInfo.setGameId(resultSet.getString("gameId"));
            gameInfo.setStatus(resultSet.getString("status"));
            gameInfo.setCurrentTurn(resultSet.getString("currentTurn"));
            gameInfo.setStatus(resultSet.getString("timeSinceLastTurn"));
            return gameInfo;
        }
    }

    class GamePiecesRowMapper implements RowMapper<GamePlay> {

        public GamePlay mapRow(ResultSet resultSet, int i) throws SQLException {
            GamePlay gamePlay = new GamePlay();
            gamePlay.setColNum(Integer.parseInt(resultSet.getString("colNum")));
            gamePlay.setRowNum(Integer.parseInt(resultSet.getString("rowNum")));
            gamePlay.setColor(resultSet.getString("color"));
            gamePlay.setType(resultSet.getString("type"));
            gamePlay.setGameId(resultSet.getString("gameId"));
            gamePlay.setPieceId(resultSet.getString("pieceId"));
            gamePlay.setPlayerId(resultSet.getString("playerId"));
            return gamePlay;
        }
    }
}
