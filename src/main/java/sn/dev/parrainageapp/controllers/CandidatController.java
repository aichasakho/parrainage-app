package sn.dev.parrainageapp.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.dev.parrainageapp.entities.Role;
import sn.dev.parrainageapp.entities.Utilisateur;
import sn.dev.parrainageapp.repositories.utilisateur.IUtilisateur;
import sn.dev.parrainageapp.repositories.utilisateur.UtilisateurImpl;
import sn.dev.parrainageapp.tools.Outils;
import sn.dev.parrainageapp.tools.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CandidatController implements Initializable {

    @FXML
    private TableView<Utilisateur> ElectTbview; // TableView pour afficher les utilisateurs

    @FXML
    private TableColumn<Utilisateur, Integer> colId; // Colonne pour afficher l'id des utilisateurs

    @FXML
    private TableColumn<Utilisateur, String> colNom; // Colonne pour afficher le nom des utilisateurs

    @FXML
    private TableColumn<Utilisateur, String> colPrenom; // Colonne pour afficher le prénom des utilisateurs

    @FXML
    private TableColumn<Utilisateur, Role> colProfil; // Colonne pour afficher le profil des utilisateurs

    @FXML
    private TableColumn<Utilisateur, String> colStatus; // Colonne pour afficher le statut des utilisateurs

    @FXML
    private Label labelCandName; // Label pour afficher le prénom de l'utilisateur connecté

    @FXML
    private Label labelElectCount; // Label pour afficher le nombre d'utilisateurs affichés dans le tableau

    @FXML
    private MenuItem menuCandidatClose; // Élément de menu pour fermer l'application

    @FXML
    private MenuItem menuCandidatLogout; // Élément de menu pour se déconnecter

    private IUtilisateur iUser = new UtilisateurImpl(); // Implémentation de l'interface IUtilisateur

    @FXML
    void close(ActionEvent event) { // Gestionnaire d'événement pour l'action de fermeture
        Platform.exit(); // Ferme l'application
    }

    @FXML
    void logout(ActionEvent event) throws IOException { // Gestionnaire d'événement pour l'action de déconnexion
        UserSession.clearSession(); // Efface la session utilisateur
        Outils.loadByMenu(event, "Connexion", "/pages/login.fxml"); // Redirige vers l'écran de connexion
    }

    public void loadTable() { // Charge les données dans le tableau
        ObservableList<Utilisateur> liste = iUser.getAllParrains(UserSession.getUserConn().getId()); // Récupère la liste des utilisateurs candidats
        ElectTbview.setItems(liste); // Ajoute la liste au TableView
        colId.setCellValueFactory(new PropertyValueFactory<>("id")); // Configure la colonne pour afficher l'id des utilisateurs
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom")); // Configure la colonne pour afficher le nom des utilisateurs
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom")); // Configure la colonne pour afficher le prénom des utilisateurs
        colStatus.setCellValueFactory(new PropertyValueFactory<>("actived")); // Configure la colonne pour afficher le statut des utilisateurs
        colProfil.setCellValueFactory(new PropertyValueFactory<>("profil")); // Configure la colonne pour afficher le profil des utilisateurs
        labelElectCount.setText(String.format("%d", liste.size())); // Met à jour le texte du label avec le nombre d'utilisateurs dans la liste
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { // Méthode appelée lors de l'initialisation du contrôleur
        labelCandName.setText(UserSession.getUserConn().getPrenom()); // Configure le texte du label avec le prénom de l'utilisateur connecté
        loadTable(); // Charge les données dans le tableau
    }
}