package sn.dev.parrainageapp.repositories.role;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sn.dev.parrainageapp.DBConnection;
import sn.dev.parrainageapp.entities.Role;
import sn.dev.parrainageapp.entities.Utilisateur;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleImpl implements IRole{
    private final DBConnection db = new DBConnection();

    @Override
    public Role getRoleById(int id) {
        String sql = "SELECT * FROM role WHERE id = ?";
        Role role = null;
        try{
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if(rs.next()){
                role = new Role(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt("etat")
                );
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return role;
    }

    public Role getRoleByName(String name) {
        String sql = "SELECT * FROM role WHERE name = ?";
        Role role = null;
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, name);
            ResultSet rs = db.executeSelect();
            if(rs.next()){
                role = new Role(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("etat")
                );
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public int changeRoleEtat(int id, int etat) {
        String sql = "UPDATE role SET etat = ? WHERE id = ?";
        int ok;
        try{
            db.initPrepar(sql);
            db.getPstm().setInt(1, etat);
            db.getPstm().setInt(2, id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ok;
    }

    public ObservableList<Role> getAllRoles() {
        ObservableList<Role> roles = FXCollections.observableArrayList();
        String sql = "SELECT * FROM role";
        try{
            //Préparation ou initialisation de la request
            db.initPrepar(sql);
            //Execution de la request
            ResultSet rs = db.executeSelect();
            while(rs.next()){
                roles.add(new Role(rs.getInt(1), rs.getString(2), rs.getInt("etat")));
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return roles;
    }

}
