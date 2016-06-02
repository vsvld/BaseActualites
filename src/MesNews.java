import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.control.Alert.*;
import static javafx.scene.control.ButtonBar.*;
import static javafx.stage.FileChooser.*;

/**
 * Classe principal de gestion de logiciel avec GUI.
 *
 * @author ALOKHIN Vsevolod
 * @author NIKONOVYCH Daria
 * @author TEN Alina
 */
public class MesNews extends Application {
    private static BaseDeNews maBase;   // base de news
    private Stage stage;                // fenetre principal
    private BorderPane layout;          // disposition d'elements
    private MenuBar menuBar;            // menu
    TextField rechercheChamp;
    TableView<News> table;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Entry point of the Application program.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Base d'actualités");

        // DB menu
        Menu baseMenu = new Menu("Base d'actualités");

        // Buttons of menu
        // New database
        MenuItem nouvelleBaseBouton = new MenuItem("Créer nouvelle");
        nouvelleBaseBouton.setOnAction(e -> creerBase());

        // Open database from file
        MenuItem ouvrirBaseBouton = new MenuItem("Ouvrir");
        ouvrirBaseBouton.setOnAction(e -> ouvrirBase());

        // Save database to file
        MenuItem sauvegarderBaseBouton = new MenuItem("Sauvegarder");
        sauvegarderBaseBouton.setOnAction(e -> sauvegarderBase());

        // Adding buttons to DB menu
        baseMenu.getItems().addAll(nouvelleBaseBouton, ouvrirBaseBouton, sauvegarderBaseBouton);

        // News menu
        Menu actualitesMenu = new Menu("Actualités");

        // New photo
        MenuItem nouvellePhotoBouton = new MenuItem("Nouvelle photo");
        nouvellePhotoBouton.setOnAction(e -> creerPhoto());

        // New article
        MenuItem nouveauArticleBouton = new MenuItem("Nouveau article de presse");
        nouveauArticleBouton.setOnAction(e -> creerArticle());

        // Refresh table
        MenuItem rafraichirBouton = new MenuItem("Rafraîchir");
        rafraichirBouton.setOnAction(e -> afficherTable(maBase.getBase()));

        // Adding buttons to News menu
        actualitesMenu.getItems().addAll(nouvellePhotoBouton, nouveauArticleBouton, new SeparatorMenuItem(), rafraichirBouton);

        // Main menu bar
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(baseMenu, actualitesMenu);

        // Setting and showing the window
        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(new Label("Créez ou ouvrez la base, s'il vous plaît"));
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creating DB method.
     */
    private void creerBase() {
        // Creating new DB object and initializing it
        maBase = new BaseDeNews();
        maBase.initialiser();

        // Message of successful creation
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("La base viens d'être crée!");
        alert.setContentText("Maintenant vous pouvez creer nouvelles actualités!");

        alert.showAndWait();

        // Show all elements of DB via table
        afficherTable(maBase.getBase());
    }

    /**
     * Method to open DB from a file.
     */
    private void ouvrirBase() {
        // Window for opening a file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier de base d'actualités");
        File file = fileChooser.showOpenDialog(stage);

        // Reading from file, handling exceptions
        try {
            maBase = new BaseDeNews();
            maBase.lireLeFichier(file);

            // Message of successful DB opening
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez bien ouvert le fichier !");
            alert.showAndWait();

            // Show all elements of DB via table
            afficherTable(maBase.getBase());

        } catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Quelque chose ne va pas avec ouverture de votre fichier !");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    /**
     * Method for saving DB to a file.
     */
    private void sauvegarderBase() {
        // Can not save when DB does not exist
        if (!estCree()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Il n'y a pas de base !");
            alert.setContentText("Creer ou ouvrez la base !");
            alert.showAndWait();
            return;
        }

        // Window for choosing where to save DB
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder la base d'actualités");
        File file = fileChooser.showSaveDialog(stage);

        // Writing to file, handling exceptions
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

    /**
     * Method for photo creation.
     */
    private void creerPhoto() {
        // Can not add news when DB does not exist
        if (!estCree()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Il n'y a pas de base !");
            alert.setContentText("Creer ou ouvrez la base !");
            alert.showAndWait();
            return;
        }

        // Dialog for entering photo data

        Dialog<Photo> dialog = new Dialog<>();
        dialog.setTitle("Photo creation");
        dialog.setHeaderText("Remplissez les paramètres de photo");

        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, annulerButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titre = new TextField();
        TextField auteur = new TextField();
        DatePicker date = new DatePicker();
        TextField source = new TextField();
        TextField resolutionHauteur = new TextField();
        TextField resolutionLargeur = new TextField();
        TextField format = new TextField(); // TODO chooser
        CheckBox enCouleur = new CheckBox();

        Button ouvrirImageBouton = new Button("Ouvrir...");
        final File[] file = new File[1];

        ouvrirImageBouton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir une image");
            // Only images
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
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

        // Validation
        Node enregistrerBouton = dialog.getDialogPane().lookupButton(enregistrerButtonType);
        enregistrerBouton.setDisable(true);

        titre.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        auteur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        source.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        resolutionHauteur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        resolutionLargeur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        format.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the titre field by default
        Platform.runLater(titre::requestFocus);

        // Convert
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                try {
                    LocalDate localDate = date.getValue();
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

        // if object was created successfuly, add it to DB
        if (resultat.isPresent()) {
            try {
                maBase.ajouter(resultat.get());
            } catch (IOException  e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Quelque chose ne va pas avec addition d'objet sur la base !");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
            afficherTable(maBase.getBase());
        }
    }

    /**
     * Method for creating new article.
     */
    private void creerArticle() {
        // Can not add news when DB does not exist
        if (!estCree()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Il n'y a pas de base !");
            alert.setContentText("Creer ou ouvrez la base !");
            alert.showAndWait();
            return;
        }

        // Dialog for entering photo data

        Dialog<ArticleDePresse> dialog = new Dialog<>();
        dialog.setTitle("Article de presse creation");
        dialog.setHeaderText("Remplissez les paramètres d'article");

        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, annulerButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titre = new TextField();
        TextField auteur = new TextField();
        DatePicker date = new DatePicker();
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

        // Validation

        Node enregistrerBouton = dialog.getDialogPane().lookupButton(enregistrerButtonType);
        enregistrerBouton.setDisable(true);

        titre.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        auteur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        source.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        texte.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the titre field by default.
        Platform.runLater(titre::requestFocus);

        // convert
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                try {
                    LocalDate localDate = date.getValue();
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
            try {
                maBase.ajouter(resultat.get());
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Quelque chose ne va pas avec addition d'objet sur la base !");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
            afficherTable(maBase.getBase());
        }
    }

    /**
     * Searching the DB.
     */
    private void rechercher() {
        try {
            afficherTable(maBase.chercher(rechercheChamp.getText()));
        } catch (IOException | ParseException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Quelque chose ne va pas avec recherche !");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    /**
     * Checks if DB exists (created or opened).
     * @return true if DB opened in the program
     */
    private boolean estCree() {
        return maBase != null;
    }

    /**
     * Showing DB elements in the table.
     * @param news news to show in the table
     */
    private void afficherTable(ArrayList<News> news) {
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


        // Recherche champ

        rechercheChamp = new TextField();
        rechercheChamp.setMinWidth(200);

        // Recherche bouton
        Button rechercheBouton = new Button("Chercher");
        rechercheBouton.setOnAction(e -> rechercher());

        // "Montrer" bouton
        Button montrerBouton = new Button("Montrer");
        montrerBouton.setOnAction(e -> montrerActualite());

        // "Modifier" bouton
        Button modifierBouton = new Button("Modifier");
        modifierBouton.setOnAction(e -> modifierActualite());

        // "Supprimer" bouton
        Button supprimerBouton = new Button("Supprimer");
        supprimerBouton.setOnAction(e -> supprimerActualite());

        HBox rightFooter = new HBox();
        rightFooter.setPadding(new Insets(10, 10, 10, 10));
        rightFooter.setSpacing(10);
        rightFooter.setAlignment(Pos.BASELINE_RIGHT);
        rightFooter.getChildren().addAll(rechercheChamp, rechercheBouton);

        HBox leftFooter = new HBox();
        leftFooter.setPadding(new Insets(10, 10, 10, 10));
        leftFooter.setSpacing(10);
        leftFooter.setAlignment(Pos.BASELINE_LEFT);
        leftFooter.getChildren().addAll(montrerBouton, modifierBouton, supprimerBouton);

        HBox footer = new HBox();
        HBox.setHgrow(rightFooter, Priority.ALWAYS);
        footer.getChildren().addAll(leftFooter, rightFooter);

        table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(news));
        table.getColumns().addAll(dateColonne, typeColonne, titreColonne, auteurColonne);

        layout.setCenter(table);
        layout.setBottom(footer);
    }

    /**
     * Show new window with detailed info of selected news.
     */
    private void montrerActualite() {
        ObservableList<News> newsSelected = table.getSelectionModel().getSelectedItems();

        // TODO error handling
        for (News news : newsSelected) {
            switch (news.getClass().getSimpleName()) {
                case "Photo":
                    montrerPhoto((Photo) news);
                    break;
                case "ArticleDePresse":
                    montrerArticle((ArticleDePresse) news);
                    break;
            }
        }
    }

    /**
     * Show photo.
     * @param photo photo to show
     */
    private void montrerPhoto(Photo photo) {
        Stage showStage = new Stage();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setPrefSize(640, 480);

        showStage.setTitle("Photo");
        Image image = null;

        try {
            image = new Image(new FileInputStream(photo.getFichier()));
        } catch (FileNotFoundException e) {
            // TODO Normal error
            System.out.println("Something is wrong with loading the image");
        }

        ImageView imageView = new ImageView(image);

        if (photo.getResolutionLargeur() > 600) {
            imageView.setFitWidth(600);
            imageView.setPreserveRatio(true);
        }

        imageView.setSmooth(true);

        Text titre = new Text(photo.getTitre());
        titre.setFont(new Font(20));
        titre.setWrappingWidth(600);
        titre.setTextAlignment(TextAlignment.CENTER);

        StringBuilder infoStr = new StringBuilder();
        infoStr.append("Auteur: ").append(photo.getAuteur()).append("\n");
        infoStr.append("Date: ").append(photo.getDate()).append("\n");
        infoStr.append("Source: ").append(photo.getSource()).append("\n");
        infoStr.append("Format: ").append(photo.getFormat()).append("\n");
        infoStr.append("Resolution: ").append(photo.getResolutionLargeur()).append("x").append(photo.getResolutionHauteur());

        Text info = new Text(infoStr.toString());
        info.setTextAlignment(TextAlignment.CENTER);

        vBox.getChildren().addAll(
                new Label(),
                titre,
                new Label(),
                new Label(),
                imageView,
                new Label(),
                info);

        Scene showScene = new Scene(scrollPane, 640, 480);
        showStage.setScene(showScene);
        showStage.showAndWait();
    }

    /**
     * Show article.
     * @param articleDePresse article to show
     */
    private void montrerArticle(ArticleDePresse articleDePresse) {
        Stage showStage = new Stage();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setPrefSize(640, 480);

        showStage.setTitle("Article de presse");

        Text titre = new Text(articleDePresse.getTitre());
        titre.setFont(new Font(20));
        titre.setWrappingWidth(600);
        titre.setTextAlignment(TextAlignment.CENTER);

        StringBuilder infoStr1 = new StringBuilder();
        infoStr1.append("Auteur: ").append(articleDePresse.getAuteur()).append("\n");
        infoStr1.append("Date: ").append(articleDePresse.getDate()).append("\n");
        infoStr1.append("Source: ").append(articleDePresse.getSource());

        Text info1 = new Text(infoStr1.toString());
        info1.setTextAlignment(TextAlignment.LEFT);
        info1.setWrappingWidth(600);

        Text texte = new Text(articleDePresse.getTexte());
        texte.setWrappingWidth(600);

        StringBuilder infoStr2 = new StringBuilder();
        infoStr2.append("Version longue: ").append(articleDePresse.getVersionLongue()).append("\n");
        infoStr2.append("Uniquement électronique: ").append(articleDePresse.isUniquementElectronique() ? "oui" : "non");

        Text info2 = new Text(infoStr2.toString());
        info2.setTextAlignment(TextAlignment.LEFT);
        info2.setWrappingWidth(600);

        vBox.getChildren().addAll(
                new Label(),
                titre,
                new Label(),
                info1,
                new Label(),
                texte,
                new Label(),
                info2);

        Scene showScene = new Scene(scrollPane, 640, 480);
        showStage.setScene(showScene);
        showStage.showAndWait();
    }

    /**
     * Edit selected news.
     */
    private void modifierActualite() {
        ObservableList<News> newsSelected = table.getSelectionModel().getSelectedItems();

        for (News news : newsSelected) {
            switch (news.getClass().getSimpleName()) {
                case "Photo":
                    modifierPhoto((Photo) news);
                    break;
                case "ArticleDePresse":
                    modifierArticle((ArticleDePresse) news);
                    break;
            }
        }
    }

    /**
     * Edit photo.
     * @param photo photo to edit
     */
    private void modifierPhoto(Photo photo) {
        Dialog<Photo> dialog = new Dialog<>();
        dialog.setTitle("Photo modification");
        dialog.setHeaderText("Remplissez les paramètres de photo");

        // TODO extract
        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, annulerButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titre = new TextField(photo.getTitre());
        TextField auteur = new TextField(photo.getAuteur());
        DatePicker date = new DatePicker(photo.getDate());
        TextField source = new TextField(photo.getSource().toString());
        TextField resolutionHauteur = new TextField(String.valueOf(photo.getResolutionHauteur()));
        TextField resolutionLargeur = new TextField(String.valueOf(photo.getResolutionLargeur()));
        TextField format = new TextField(photo.getFormat()); // TODO chooser
        CheckBox enCouleur = new CheckBox();
        if (photo.isEnCouleur()) enCouleur.setSelected(true);

        Button ouvrirImageBouton = new Button("Ouvrir...");
        final File[] file = new File[1];

        ouvrirImageBouton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir une image");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
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

        Node enregistrerBouton = dialog.getDialogPane().lookupButton(enregistrerButtonType);
        enregistrerBouton.setDisable(true);

        titre.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        auteur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        source.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        resolutionHauteur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        resolutionLargeur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        format.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the titre field by default.
        Platform.runLater(titre::requestFocus);

        // convert
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                try {
                    LocalDate localDate = date.getValue();
                    URL url = new URL(source.getText());
                    int hauteur = Integer.parseInt(resolutionHauteur.getText());
                    int largeur = Integer.parseInt(resolutionLargeur.getText());
                    if (file[0] == null) file[0] = photo.getFichier();
                    return new Photo(titre.getText(), auteur.getText(), localDate, url, file[0], format.getText(), hauteur, largeur, enCouleur.isSelected());
                } catch (Exception e) {
                    e.printStackTrace(); // TODO Alert
                }
            }
            return null;
        });

        Optional<Photo> resultat = dialog.showAndWait();
        if (resultat.isPresent()) {
            try {
                maBase.changer(photo, resultat.get());
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Quelque chose ne va pas avec changement d'objet sur la base !");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
            afficherTable(maBase.getBase());
        }
    }

    /**
     * Edit article.
     * @param articleDePresse article to edit
     */
    private void modifierArticle(ArticleDePresse articleDePresse) {
        Dialog<ArticleDePresse> dialog = new Dialog<>();
        dialog.setTitle("Article de presse modification");
        dialog.setHeaderText("Remplissez les paramètres d'article");

        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, annulerButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titre = new TextField(articleDePresse.getTitre());
        TextField auteur = new TextField(articleDePresse.getAuteur());
        DatePicker date = new DatePicker(articleDePresse.getDate());
        TextField source = new TextField(articleDePresse.getSource().toString());
        TextArea texte = new TextArea(articleDePresse.getTexte());
        texte.setWrapText(true);
        TextField versionLongue = new TextField(articleDePresse.getVersionLongue().toString());
        CheckBox uniquementElectronique = new CheckBox();
        if (articleDePresse.isUniquementElectronique()) uniquementElectronique.setSelected(true);

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

        Node enregistrerBouton = dialog.getDialogPane().lookupButton(enregistrerButtonType);
        enregistrerBouton.setDisable(true);

        titre.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        auteur.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        source.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });
        texte.textProperty().addListener((observable, oldValue, newValue) -> {
            enregistrerBouton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the titre field by default.
        Platform.runLater(titre::requestFocus);

        // convert
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                try {
                    LocalDate localDate = date.getValue();
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
            try {
                maBase.changer(articleDePresse, resultat.get());
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Quelque chose ne va pas avec changement d'objet sur la base !");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
            afficherTable(maBase.getBase());
        }
    }

    /**
     * Delete selected article.
     */
    private void supprimerActualite() {
        ObservableList<News> newsSelected = table.getSelectionModel().getSelectedItems();

        for (News news : newsSelected) {
            maBase.supprimer(news);
        }
    }
}
