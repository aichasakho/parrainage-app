package sn.dev.parrainageapp.repositories.role;

import javafx.collections.ObservableList;
import sn.dev.parrainageapp.entities.Role;
import sn.dev.parrainageapp.entities.Utilisateur;

public interface IRole {
    Role getRoleById(int id);
    ObservableList<Role> getAllRoles();
    Role getRoleByName(String name);
    int changeRoleEtat(int id, int etat);
}
