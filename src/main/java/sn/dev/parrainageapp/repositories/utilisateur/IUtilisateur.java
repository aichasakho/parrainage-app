package sn.dev.parrainageapp.repositories.utilisateur;

import javafx.collections.ObservableList;
import sn.dev.parrainageapp.entities.Utilisateur;

public interface IUtilisateur {
    Utilisateur seConnecter(String login, String password);
    ObservableList<Utilisateur> getAllUser();
    int store(Utilisateur user);
    int update(Utilisateur user);
    int delete(int id);
    ObservableList<Utilisateur> getAllParrains(int id);
    ObservableList<Utilisateur> getAllCandidats();
    ObservableList<Utilisateur> getParCandidat(int id);
    int setStatus(int id, int active);
    int getCandidatCount();
}
