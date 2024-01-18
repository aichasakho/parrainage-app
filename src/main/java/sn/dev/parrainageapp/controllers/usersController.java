package sn.dev.parrainageapp.controllers;

import com.mysql.cj.util.Util;
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
import sn.dev.parrainageapp.repositories.role.IRole;
import sn.dev.parrainageapp.repositories.role.RoleImpl;
import sn.dev.parrainageapp.repositories.utilisateur.IUtilisateur;
import sn.dev.parrainageapp.repositories.utilisateur.UtilisateurImpl;
import sn.dev.parrainageapp.tools.Notification;
import sn.dev.parrainageapp.tools.Outils;
import sn.dev.parrainageapp.tools.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class usersController implements Initializable {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClean;

    @FXML
    private MenuItem adminLogout;


    @FXML
    private Button btnEdit;

    @FXML
    private Button btnSelect;

    @FXML
    private ComboBox<Role> cbbProfil;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colLogin;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, Role> colProfil;

    @FXML
    private TableColumn<Utilisateur, Integer> colStatus;

    @FXML
    private TableColumn<Utilisateur, String> colPassword;

    @FXML
    private TableView<Utilisateur> tabViewUser;

    @FXML
    private TextField txtNom;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPrenom;

    private int idUser;
    private String passwordUser;

    IUtilisateur iUser = new UtilisateurImpl();

    @FXML
    void cleanForm(ActionEvent event) {
        clearField();
    }

    @FXML
    void editUser(ActionEvent event) {
        Utilisateur user = new Utilisateur();
        if (Objects.equals(passwordUser, txtPassword.getText())) {
            user.setId(idUser);
            user.setNom(txtNom.getText());
            user.setPrenom(txtPrenom.getText());
            String login = String.format("%c%s%02d",
                    txtPrenom.getText().charAt(0),
                    txtNom.getText().toUpperCase(),
                    txtPrenom.getText().length()
            );
            user.setLogin(login);
            int ok = iUser.update(user);
            if (ok == 1){
                Notification.NotifSuccess("Success","L'utilisateur à été modifié !");
                loadTable();
                clearField();
            } else {
                Notification.NotifError("Erreur","Erreur de mise a jour !");
            }
        } else {
            Notification.NotifError("Erreur","Mot de passe incorrect!");
        }
    }

    @FXML
    void deleteUser(ActionEvent event) {
        if (Objects.equals(passwordUser, txtPassword.getText())) {
            int ok = iUser.delete(idUser);
            if (ok == 1){
                Notification.NotifSuccess("Success","L'utilisateur à été supprimé !");
                loadTable();
                clearField();
            } else {
                Notification.NotifError("Erreur","Erreur de mise a jour !");
            }
        } else {
            Notification.NotifError("Erreur","Mot de passe incorrect!");
        }
    }

    @FXML
    void storeUser(ActionEvent event) {
        Utilisateur user = new Utilisateur();
        user.setProfil(cbbProfil.getValue());
        if (user.getProfil().getEtat() == 0)
            Notification.NotifError("Error", "Vous ne pouvez plus enregistrer d'utilisateur de se type");
        else {
            user.setNom(txtNom.getText());
            user.setPrenom(txtPrenom.getText());
            String login = String.format("%c%s%02d",
                    txtPrenom.getText().charAt(0),
                    txtNom.getText().toUpperCase(),
                    txtPrenom.getText().length()
            );
            user.setLogin(login);
            user.setActived(1);
            user.setPassword(txtPassword.getText());
            int ok = iUser.store(user);
            if (ok == 1) {
                IRole iRole = new RoleImpl();
                if ( iUser.getCandidatCount() == 5 && iRole.getRoleById(2).getEtat() == 1 ) {
                    iRole.changeRoleEtat(2, 0);
                }
                loadTable();
                clearField();
            } else {
                Notification.NotifError("Erreur", "Erreur d'insertion !");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        loadCbb();
        btnAdd.setDisable(false);
    }

    public void loadTable() {
        ObservableList<Utilisateur> liste = iUser.getAllUser();
        tabViewUser.setItems(liste);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("actived"));
        colProfil.setCellValueFactory(new PropertyValueFactory<>("profil"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    public void loadCbb(){
        IRole iRole = new RoleImpl();
        ObservableList<Role> roles = iRole.getAllRoles();
        for (Role role: roles) {
            if(role.getEtat() == 1)
                cbbProfil.getItems().add(role);
        }
    }

    public void clearField(){
        txtPrenom.setText("");
        txtNom.setText("");
        txtPassword.setText("");
        cbbProfil.getItems().clear();
        loadCbb();
        btnAdd.setDisable(false);
    }

    @FXML
    void openCandidatPage(ActionEvent event) throws IOException {
        Outils.loadByMenu(event, "Gestion Utilisateurs", "/pages/users.fxml");
    }
    @FXML
    void openUsersPage(ActionEvent event) throws IOException {
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

    @FXML
    void returnAcceuil(ActionEvent event) throws IOException {
        Outils.loadByMenu(event, "Bienvenue", "/pages/admin.fxml");
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        UserSession.clearSession();
        Outils.loadByMenu(event, "Bienvenue", "/pages/login.fxml");
    }

    @FXML
    public void getLine(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
            Utilisateur user = tabViewUser.getSelectionModel().getSelectedItem();
            idUser = user.getId();
            passwordUser = user.getPassword();
            txtNom.setText(user.getNom());
            txtPrenom.setText(user.getPrenom());
            cbbProfil.setValue(user.getProfil());
            txtPassword.setText("");
            btnAdd.setDisable(true);
        }
    }
}
