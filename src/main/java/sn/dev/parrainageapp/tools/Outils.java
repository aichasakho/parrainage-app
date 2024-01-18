package sn.dev.parrainageapp.tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Outils {

    public static void showConfirmationMessage(String titre, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showErrorMessage(String titre, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadPage(ActionEvent event, String title, String url) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private void loadPageMenu(ActionEvent event, String title, String url) {
        try {
            // Obtient la source de l'événement
            Object source = event.getSource();

            // Vérifie si la source est un MenuItem et s'il a un parent popup
            if (source instanceof MenuItem) {
                MenuItem menuItem = (MenuItem) source;
                ContextMenu contextMenu = menuItem.getParentPopup();

                // Vérifie si le MenuItem est associé à un menu contextuel
                if (contextMenu != null) {
                    // Obtient la scène depuis le menu contextuel
                    Scene scene = contextMenu.getOwnerWindow().getScene();

                    // Ferme la scène actuelle
                    if (scene != null) {
                        Stage currentStage = (Stage) scene.getWindow();
                        currentStage.close();
                    }
                }
            }

            // Charge la nouvelle page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            if (!isStageAlreadyOpen(title)) {
                stage.setScene(new Scene(root));
                stage.setTitle(title);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier si la fenêtre est déjà ouverte
    private boolean isStageAlreadyOpen(String title) {
        for (Window existingWindow : Window.getWindows()) {
            if (existingWindow instanceof Stage) {
                Stage existingStage = (Stage) existingWindow;

                // Vérifie si le titre est null avant d'appeler equals()
                if (title != null && title.equals(existingStage.getTitle())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void loadByMenu(ActionEvent event, String title, String url) throws IOException{
        new Outils().loadPageMenu(event, title, url);
    }

    public static void load(ActionEvent event, String title, String url) throws IOException{
        new Outils().loadPage(event, title, url);
    }
}
