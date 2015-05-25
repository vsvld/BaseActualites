import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created with IntelliJ IDEA.
 * User: vsvld
 * Date: 24.05.2015
 * Time: 23:31
 */
public class MesNews extends Application {

    private static BaseDeNews mabase;
    private Stage window;
    private BorderPane layout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Base d'actualités");

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

        actualitesMenu.getItems().addAll(nouvellePhotoBouton, nouveauArticleBouton, new SeparatorMenuItem(), afficherPhotosBouton, afficherArticlesBouton, new SeparatorMenuItem(), rechercherBouton);

        // Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(baseMenu, actualitesMenu);

        layout = new BorderPane();
        layout.setTop(menuBar);
        Scene scene = new Scene(layout, 400, 300);
        window.setScene(scene);
        window.show();
    }




    private void creerBase() {
        mabase = new BaseDeNews();
        mabase.initialiser();
        System.out.println("La base viens d'etre cree!");
    }

    private void ouvrir() {

    }

    private void sauvegarder() {

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
}
