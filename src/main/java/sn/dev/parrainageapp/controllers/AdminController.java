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
import sn.dev.parrainageapp.entities.Role;
import sn.dev.parrainageapp.entities.Utilisateur;
import sn.dev.parrainageapp.repositories.utilisateur.IUtilisateur;
import sn.dev.parrainageapp.repositories.utilisateur.UtilisateurImpl;
import sn.dev.parrainageapp.tools.Notification;
import sn.dev.parrainageapp.tools.Outils;
import sn.dev.parrainageapp.tools.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {


    @FXML
    private MenuItem adminMenuCandidat;

    @FXML
    private MenuItem adminMenuElecteur;

    @FXML
    private Menu adminUserMenu;

    @FXML
    private Button btnActive;

    @FXML
    private Button btnDesactive;

    @FXML
    private MenuItem closeMenu;

    @FXML
    private TableColumn<Utilisateur, Integer> colEtat;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colLogin;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPassword;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, Role> colProfil;

    @FXML
    private MenuItem menuAdminLogout;

    @FXML
    private Label selectedUserLabel;

    @FXML
    private TableView<Utilisateur> tbvUser;

    IUtilisateur iUser = new UtilisateurImpl();

    Utilisateur user = null;

    @FXML
    void openCandidatPage(ActionEvent event) throws IOException {
        Outils.loadByMenu(event, "Gestion Utilisateurs", "/pages/users.fxml");
    }

    @FXML
    void openElecteurPage(ActionEvent event) {
        try {
            Outils.loadByMenu(event, "Gestion Utilisateurs", "/pages/users.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openUserPage(ActionEvent event) {
        try {
            Outils.loadByMenu(event, "Gestion Utilisateurs", "/pages/users.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void quitterApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        UserSession.clearSession();
        Outils.loadByMenu(actionEvent, "Connexion", "/pages/login.fxml");
    }

    @FXML
    public void removeSelect(MouseEvent mouseEvent) {
        user = null;
        selectedUserLabel.setText("Aucun Utilisateur selectionné");
        btnDesactive.setDisable(true);
        btnActive.setDisable(true);
    }

    @FXML
    public void activedUser(ActionEvent actionEvent) {
        int ok;
        if (user != null && user.getActived() == 0) {
            ok = iUser.setStatus(user.getId(), 1);
            if ( ok == 1 ){
                Notification.NotifSuccess("Success", user.getNom() + " est désormais active");
                loadTable();
            } else {
                Notification.NotifError("Error", "Erreur de mise a jour");
            }
        } else  {
            Notification.NotifError("Error", "Merci de bien vouloir selectionner l'utilisateur");
        }
    }

    @FXML
    public void desactiveUser(ActionEvent actionEvent) {
        int ok;
        if (user != null && user.getActived() == 1) {
            ok = iUser.setStatus(user.getId(), 0);
            if ( ok == 1 ){
                Notification.NotifSuccess("Success", user.getNom() + " est désormais désactivé");
                loadTable();
            } else {
                Notification.NotifError("Error", "Erreur de mise a jour");
            }
        } else  {
            Notification.NotifError("Error", "Merci de bien vouloir selectionner l'utilisateur");
        }
    }

    public void loadTable() {
        ObservableList<Utilisateur> liste = iUser.getAllUser();
        tbvUser.setItems(liste);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("actived"));
        colProfil.setCellValueFactory(new PropertyValueFactory<>("profil"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    @FXML
    void selectUser(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            user = tbvUser.getSelectionModel().getSelectedItem();
            selectedUserLabel.setText("Utilisateur selectionner: " + user.getPrenom() + " " + user.getNom().toUpperCase());
            btnActive.setDisable(false);
            btnDesactive.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        btnActive.setDisable(true);
        btnDesactive.setDisable(true);
    }
}
