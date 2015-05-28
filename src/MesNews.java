import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

import static javafx.scene.control.Alert.*;
import static javafx.scene.control.ButtonBar.*;

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
    private MenuBar menuBar;

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
        menuBar = new MenuBar();
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

        afficherBase();
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

            afficherBase();

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
        Dialog<Photo> dialog = new Dialog<>();
        dialog.setTitle("Photo creation");
        dialog.setHeaderText("Remplissez les paramètres de photo");

        // TODO set the icon

        // TODO extract
        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, annulerButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titre = new TextField();
        TextField auteur = new TextField();
        TextField date = new TextField(); // TODO DatePicker
        TextField source = new TextField();
        TextField resolutionHauteur = new TextField();
        TextField resolutionLargeur = new TextField();
        TextField format = new TextField(); // TODO chooser
        CheckBox enCouleur = new CheckBox();

        Button ouvrirImageBouton = new Button("Ouvrir...");
        final File[] file = new File[1];

        // TODO images only
        ouvrirImageBouton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir une image");
            file[0] = fileChooser.showOpenDialog(stage);
        });

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titre, 1, 0);
        grid.add(new Label("Auteur:"), 0, 1);
        grid.add(auteur, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(date, 1, 2);
        grid.add(new Label("Source:"), 0, 3);
        grid.add(source, 1, 3);
        grid.add(new Label("Image:"), 0, 4);
        grid.add(ouvrirImageBouton, 1, 4);
        grid.add(new Label("Resolution hauteur:"), 0, 5);
        grid.add(resolutionHauteur, 1, 5);
        grid.add(new Label("Resolution largeur:"), 0, 6);
        grid.add(resolutionLargeur, 1, 6);
        grid.add(new Label("Format:"), 0, 7);
        grid.add(format, 1, 7);
        grid.add(new Label("En couleur"), 0, 8);
        grid.add(enCouleur, 1, 8);

        // TODO add validation
//        username.textProperty().addListener((observable, oldValue, newValue) -> {
//            loginButton.setDisable(newValue.trim().isEmpty());
//        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the titre field by default.
        Platform.runLater(titre::requestFocus);

        // convert
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                try {
                    LocalDate localDate = LocalDate.parse(date.getText());
                    URL url = new URL(source.getText());
                    int hauteur = Integer.parseInt(resolutionHauteur.getText());
                    int largeur = Integer.parseInt(resolutionLargeur.getText());
                    return new Photo(titre.getText(), auteur.getText(), localDate, url, file[0], format.getText(), hauteur, largeur, enCouleur.isSelected());
                } catch (Exception e) {
                    e.printStackTrace(); // TODO Alert
                }
            }
            return null;
        });

        Optional<Photo> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
//            System.out.println(resultat);
            maBase.ajouter(resultat.get());
            afficherBase();
        }
    }

    private void creerArticle() {
        Dialog<ArticleDePresse> dialog = new Dialog<>();
        dialog.setTitle("Article de presse creation");
        dialog.setHeaderText("Remplissez les paramètres d'article");

        // TODO set the icon

        // TODO extract
        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, annulerButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titre = new TextField();
        TextField auteur = new TextField();
        TextField date = new TextField(); // TODO DatePicker
        TextField source = new TextField();
        TextArea texte = new TextArea();
        texte.setWrapText(true);
        TextField versionLongue = new TextField();
        CheckBox uniquementElectronique = new CheckBox();

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titre, 1, 0);
        grid.add(new Label("Auteur:"), 0, 1);
        grid.add(auteur, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(date, 1, 2);
        grid.add(new Label("Source:"), 0, 3);
        grid.add(source, 1, 3);
        grid.add(new Label("Texte:"), 0, 4);
        grid.add(texte, 1, 4);
        grid.add(new Label("Version longue (URL):"), 0, 5);
        grid.add(versionLongue, 1, 5);
        grid.add(new Label("Uniquement électronique:"), 0, 6);
        grid.add(uniquementElectronique, 1, 6);

        // TODO add validation
//        username.textProperty().addListener((observable, oldValue, newValue) -> {
//            loginButton.setDisable(newValue.trim().isEmpty());
//        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the titre field by default.
        Platform.runLater(titre::requestFocus);

        // convert
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                try {
                    LocalDate localDate = LocalDate.parse(date.getText());
                    URL url = new URL(source.getText());
                    URL longue = new URL(versionLongue.getText());
                    return new ArticleDePresse(titre.getText(), auteur.getText(), localDate, url, texte.getText(), longue, uniquementElectronique.isSelected());
                } catch (Exception e) {
                    e.printStackTrace(); // TODO Alert
                }
            }
            return null;
        });

        Optional<ArticleDePresse> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
//            System.out.println(resultat);
            maBase.ajouter(resultat.get());
            afficherBase();
        }
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

    private void afficherBase() {
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
    }

    private void montrerActualite() {

    }

    private void supprimerActualite() {

    }
}
