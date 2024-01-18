package sn.dev.parrainageapp.repositories.parrainer;

import com.mysql.cj.util.Util;
import sn.dev.parrainageapp.DBConnection;
import sn.dev.parrainageapp.entities.Utilisateur;
import sn.dev.parrainageapp.tools.UserSession;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ParrainerImpl implements IParrainer{

    DBConnection db = new DBConnection();
    LocalDateTime currentDate = LocalDateTime.now();

    @Override
    public int store(int candidat_id) {
        String sql = "INSERT INTO parrainer VALUES (NULL, ?, ?, ?)";
        int ok;
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(currentDate);
        try {
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, timestamp);
            db.getPstm().setInt(2, UserSession.getUserConn().getId());
            db.getPstm().setInt(3, candidat_id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ok;
    }
}
