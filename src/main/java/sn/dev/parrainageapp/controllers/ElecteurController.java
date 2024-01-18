package sn.dev.parrainageapp.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sn.dev.parrainageapp.entities.Utilisateur;
import sn.dev.parrainageapp.repositories.parrainer.IParrainer;
import sn.dev.parrainageapp.repositories.parrainer.ParrainerImpl;
import sn.dev.parrainageapp.repositories.utilisateur.IUtilisateur;
import sn.dev.parrainageapp.repositories.utilisateur.UtilisateurImpl;
import sn.dev.parrainageapp.tools.Notification;
import sn.dev.parrainageapp.tools.Outils;
import sn.dev.parrainageapp.tools.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ElecteurController implements Initializable {


    @FXML
    private Label GreetingLabel;

    @FXML
    private Button btnParrainer;

    @FXML
    private TableView<Utilisateur> candTbview;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, String> colProfil;

    @FXML
    private TableColumn<Utilisateur, String> colStatus;

    @FXML
    private MenuItem menuElecteurClose;

    @FXML
    private MenuItem menuElecteurLogout;

    @FXML
    private TableColumn<Utilisateur, String> parColNom;

    @FXML
    private TableColumn<Utilisateur, String> parColPrenom;

    @FXML
    private TableColumn<Utilisateur, String> parColProfil;

    @FXML
    private TableColumn<Utilisateur, String> parColStatus;

    @FXML
    private TableColumn<Utilisateur, Integer> parColId;

    @FXML
    private TableView<Utilisateur> parTbView;

    IUtilisateur iUser = new UtilisateurImpl();
    IParrainer iParrainer = new ParrainerImpl();
    private int idUser = 0;

    @FXML
    void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void doParrainage(MouseEvent event) {
        if (UserSession.getUserConn().getActived() == 0) {
            Notification.NotifError("Error", "Vous ne pouvez plus parrainer un autre candidats");
        } else {
            if (idUser == 0 ) {
                Notification.NotifError("Error", "Vous devez d'abord selectionner un candidat");
            } else {
                if (iUser.getAllParrains(idUser).size() < 7) {
                    int ok = iParrainer.store(idUser);
                    if (ok == 1) {
                        Notification.NotifSuccess("Success", "Parrainage effectuer");
                        int UserConnId = UserSession.getUserConn().getId();
                        iUser.setStatus(UserConnId, 0);
                        btnParrainer.setDisable(true);
                        loadParrainageTable();
                    } else {
                        Notification.NotifError("Error", "Erreur l'ors du parrainage");
                    }
                } else {
                    Notification.NotifError("Error", "Vous ne pouvez plus parrainer ce candidat");
                }
            }
        }
    }

    @FXML
    void getCandSelected(MouseEvent event) {
        Utilisateur user = candTbview.getSelectionModel().getSelectedItem();
        idUser = user.getId();
    }

    public void loadCandTable() {
        ObservableList<Utilisateur> liste = iUser.getAllCandidats();
        setTableView(liste, candTbview, colId, colNom, colPrenom, colStatus, colProfil);
    }

    private void setTableView(ObservableList<Utilisateur> listUser, TableView<Utilisateur> TableView, TableColumn<Utilisateur, Integer> ColId, TableColumn<Utilisateur, String> ColNom, TableColumn<Utilisateur, String> ColPrenom, TableColumn<Utilisateur, String> ColStatus, TableColumn<Utilisateur, String> ColProfil) {
        TableView.setItems(listUser);
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        ColPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ColStatus.setCellValueFactory(new PropertyValueFactory<>("actived"));
        ColProfil.setCellValueFactory(new PropertyValueFactory<>("profil"));
    }

    public void loadParrainageTable() {
        ObservableList<Utilisateur> liste = iUser.getParCandidat(UserSession.getUserConn().getId());
        setTableView(liste, parTbView, parColId, parColNom, parColPrenom, parColStatus, parColProfil);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        UserSession.clearSession();
        Outils.loadByMenu(event, "Login", "/pages/login.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GreetingLabel.setText(UserSession.getUserConn().getPrenom() + " " + UserSession.getUserConn().getNom());
        btnParrainer.setDisable(UserSession.getUserConn().getActived() == 0);
        loadCandTable();
        loadParrainageTable();
    }
}
