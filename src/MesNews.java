import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static javafx.scene.control.Alert.*;

/**
 * Created with IntelliJ IDEA.
 * User: vsvld
 * Date: 24.05.2015
 * Time: 23:31
 */
public class MesNews extends Application {

    private static BaseDeNews maBase;
    private Stage stage;
    private BorderPane layout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Base d'actualités");

        // Base menu
        Menu baseMenu = new Menu("Base d'actualités");

        MenuItem nouvelleBaseBouton = new MenuItem("Créer nouvelle");
        nouvelleBaseBouton.setOnAction(e -> creerBase());

        MenuItem ouvrirBaseBouton = new MenuItem("Ouvrir");
        ouvrirBaseBouton.setOnAction(e -> ouvrir());

        MenuItem sauvegarderBaseBouton = new MenuItem("Sauvegarder");
        sauvegarderBaseBouton.setOnAction(e -> sauvegarder());

        baseMenu.getItems().addAll(nouvelleBaseBouton, ouvrirBaseBouton, sauvegarderBaseBouton);

        // Actualités menu
        // TODO Disable buttons when db not created
        Menu actualitesMenu = new Menu("Actualités");

        MenuItem nouvellePhotoBouton = new MenuItem("Nouvelle photo");
        nouvellePhotoBouton.setOnAction(e -> creerPhoto());

        MenuItem nouveauArticleBouton = new MenuItem("Nouveau article de presse");
        nouveauArticleBouton.setOnAction(e -> creerArticle());

        CheckMenuItem afficherPhotosBouton = new CheckMenuItem("Afficher photos");
        afficherPhotosBouton.setOnAction(e -> afficherPhotos());

        CheckMenuItem afficherArticlesBouton = new CheckMenuItem("Afficher articles de presse");
        afficherArticlesBouton.setOnAction(e -> afficherArticles());

        MenuItem rechercherBouton = new MenuItem("Rechercher");
        rechercherBouton.setOnAction(e -> rechercher());

        actualitesMenu.getItems().addAll(nouvellePhotoBouton, nouveauArticleBouton, new SeparatorMenuItem(),
                afficherPhotosBouton, afficherArticlesBouton, new SeparatorMenuItem(), rechercherBouton);

        // TODO General menu with "exit", "about us" etc.

        // Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(baseMenu, actualitesMenu);

        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(new Label("Ouvriez la base, s'il vous plaît"));
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }


    private void creerBase() {
        maBase = new BaseDeNews();
        maBase.initialiser();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("La base viens d'être crée!");
        alert.setContentText("Maintenant vous pouvez creer nouvelles actualités!");

        alert.showAndWait();

        afficherTable();
    }

    // TODO confirmation and saving
    private void ouvrir() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier de base d'actualités");
        File file = fileChooser.showOpenDialog(stage);

        try {
            maBase.lireDansFichier(file);

            // TODO all dialogs to methods
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez bien ouvert le fichier !");
            alert.showAndWait();

            afficherTable();

        } catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Quelque chose ne va pas avec ouverture de votre fichier !");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }

    private void sauvegarder() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder la base d'actualités");
        File file = fileChooser.showSaveDialog(stage);

        try {
            maBase.ecrireDansFichier(file);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez bien sauvegardé votre base !");
            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Quelque chose ne va pas avec enregistrement de votre base !");
            alert.setContentText(e.toString());
        }
    }

    private void creerPhoto() {

    }

    private void creerArticle() {

    }

    private void afficherArticles() {

    }

    private void afficherPhotos() {

    }

    private void rechercher() {

    }

    // TODO confirmation and saving
    private void quitter() {

    }

    private boolean estCree() {
        return maBase != null;
    }

    private void afficherTable() {
        // Date colonne
        TableColumn<News, String> dateColonne = new TableColumn<>("Date");
        dateColonne.setMinWidth(200);
        dateColonne.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Type colonne
        TableColumn<News, String> typeColonne = new TableColumn<>("Type");
        typeColonne.setMinWidth(200);
        typeColonne.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Titre colonne
        TableColumn<News, String> titreColonne = new TableColumn<>("Titre");
        titreColonne.setMinWidth(200);
        titreColonne.setCellValueFactory(new PropertyValueFactory<>("titre"));

        // Auteur colonne
        TableColumn<News, String> auteurColonne = new TableColumn<>("Auteur");
        auteurColonne.setMinWidth(200);
        auteurColonne.setCellValueFactory(new PropertyValueFactory<>("auteur"));

        // Recherche
        TextField rechercheChamp = new TextField();
        rechercheChamp.setMinWidth(200);

        Button rechercheBouton = new Button("Chercher");
        rechercheBouton.setOnAction(e -> rechercher());

        // Ouvrir et supprimer boutons
        Button montrerBouton = new Button("Montrer");
        montrerBouton.setOnAction(e -> montrerActualite());
        Button supprimerBouton = new Button("Supprimer");
        supprimerBouton.setOnAction(e -> supprimerActualite());

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(montrerBouton, supprimerBouton, rechercheChamp, rechercheBouton);

        TableView<News> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(maBase.getBase()));
        table.getColumns().addAll(dateColonne, typeColonne, titreColonne, auteurColonne);

        layout.setCenter(table);
        layout.setBottom(hBox);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
    }

    private void montrerActualite() {

    }

    private void supprimerActualite() {

    }
}
