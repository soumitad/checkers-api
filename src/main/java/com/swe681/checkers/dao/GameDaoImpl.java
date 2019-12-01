package com.swe681.checkers.dao;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.game.checkers.Piece;
import com.swe681.checkers.model.request.GamePlayRequest;
import com.swe681.checkers.model.request.GameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                "INSERT INTO sdas22.game VALUES (?, ?, ?, ?, ?, ?)",
                lastGameplayId + 1, gameRequest.getPlayer1(), gameRequest.getPlayer2(),
                "InProgress",new java.sql.Timestamp(new java.util.Date().getTime()) , null);
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
    public int performMove(GamePlayRequest gamePlayRequest, boolean isJump) {
        String moveSql = "Update sdas22.gameplay set rowNum=?, colNum=? where gameId=? and pieceId=?";
        return 0;
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
}
