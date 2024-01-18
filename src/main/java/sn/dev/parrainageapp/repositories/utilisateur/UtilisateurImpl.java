package sn.dev.parrainageapp.repositories.utilisateur;

import com.mysql.cj.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.dev.parrainageapp.DBConnection;
import sn.dev.parrainageapp.entities.Role;
import sn.dev.parrainageapp.entities.Utilisateur;
import sn.dev.parrainageapp.repositories.role.IRole;
import sn.dev.parrainageapp.repositories.role.RoleImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurImpl implements IUtilisateur{
    private DBConnection db = new DBConnection();
    private ResultSet rs;

    private Utilisateur setUser(ResultSet rs) throws SQLException {
        Utilisateur user = new Utilisateur();
        user.setId(rs.getInt("id"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setActived(rs.getInt("actived"));
        IRole iRole = new RoleImpl();
        Role profil = iRole.getRoleById(rs.getInt("profil"));
        user.setProfil(profil);

        return user;
    }

    @Override
    public Utilisateur seConnecter(String login, String password) {
        String sql = "SELECT * FROM user WHERE login = ? AND password = ?";
        Utilisateur user = null;
        try{
            //Préparation ou initialisation de la request
            db.initPrepar(sql);
            //Passage de valeurs
            db.getPstm().setString(1, login);
            db.getPstm().setString(2, password);
            //Execution de la request
            rs = db.executeSelect();
            if(rs.next()){
               user = this.setUser(rs);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public ObservableList<Utilisateur> getAllUser() {
        ObservableList<Utilisateur> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM user ORDER BY nom ASC";
        try{
            //Préparation ou initialisation de la request
            db.initPrepar(sql);
            //Execution de la request
            rs = db.executeSelect();
            while(rs.next()){
                users.add(setUser(rs));
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public ObservableList<Utilisateur> getAllParrains(int id) {
        ObservableList<Utilisateur> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM user u " +
                "INNER JOIN parrainer as p ON (u.id = p.electeur) " +
                "WHERE p.candidat = ?";
        try{
            //Préparation ou initialisation de la request
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            //Execution de la request
            rs = db.executeSelect();
            while(rs.next()){
                users.add(setUser(rs));
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public ObservableList<Utilisateur> getAllCandidats() {
        ObservableList<Utilisateur> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM user u WHERE u.profil = ?";
        try{
            //Préparation ou initialisation de la request
            db.initPrepar(sql);
            db.getPstm().setInt(1, 2);
            //Execution de la request
            rs = db.executeSelect();
            while(rs.next()){
                users.add(setUser(rs));
            }
            db.closeConnection();
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return users;
    }

    @Override
    public ObservableList<Utilisateur> getParCandidat(int id) {
        ObservableList<Utilisateur> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM user u " +
                "INNER JOIN parrainer as p ON (u.id = p.candidat)" +
                "WHERE p.electeur = ?";
        try{
            //Préparation ou initialisation de la request
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            //Execution de la request
            rs = db.executeSelect();
            if (rs.next()){
                users.add(setUser(rs));
            }
            db.closeConnection();
        }catch (Exception e){
            throw new RuntimeException();
        }
        return users;
    }

    public int store(Utilisateur user) {
        String sql = "INSERT INTO user VALUES (NULL, ?, ?, ?, ?, ?, ?)";
        int ok;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, user.getNom());
            db.getPstm().setString(2, user.getPrenom());
            db.getPstm().setString(3, user.getLogin());
            db.getPstm().setString(4, user.getPassword());
            db.getPstm().setInt(5, user.getActived());
            db.getPstm().setInt(6, user.getProfil().getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ok;
    }

    public int update(Utilisateur user) {
        String sql = "UPDATE user SET nom = ?, prenom = ?, login = ? WHERE id = ?";
        int ok;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, user.getNom());
            db.getPstm().setString(2, user.getPrenom());
            db.getPstm().setString(3, user.getLogin());
            db.getPstm().setInt(4, user.getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ok;
    }

    public int setStatus(int id, int active){
        String sql = "UPDATE user SET actived = ? WHERE id = ?";
        int ok;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, active);
            db.getPstm().setInt(2, id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ok;
    }

    @Override
    public int getCandidatCount() {
        String sql = "SELECT id FROM user WHERE profil = ?";
        int count = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, 2);
            rs = db.executeSelect();
            while(rs.next()){
                count++;
            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return count;
    }

    public int delete(int id){
        String sql = "DELETE FROM user WHERE id = ?";
        int ok;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            throw  new RuntimeException();
        }
        return ok;
    }


}
