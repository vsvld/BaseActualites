import java.net.URL;
import java.time.LocalDate;

/**
 * Created by vsevolod on 15.04.15.
 */
public abstract class News implements Comparable<News> {
    private String titre, auteur;
    private LocalDate date;
    private URL source;

    public News(String titre, String auteur, LocalDate date, URL source) {
        this.titre = titre;
        this.auteur = auteur;
        this.date = date;
        this.source = source;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public URL getSource() {
        return source;
    }

    public void setSource(URL source) {
        this.source = source;
    }

    public void afficher() {
        if (titre != null)  System.out.println("Titre: " + titre);
        if (date != null)   System.out.println("Date: " + date);
        if (auteur != null) System.out.println("Auteur: " + auteur);
        if (source != null) System.out.println("Source: " + source);
    }

//    public void saisir() {
//        System.out.print("Titre: ");
//        titre = Lire.S();
//
//        System.out.print("Date (yyyy-mm-dd): ");
//        date = LocalDate.parse(Lire.S());
//
//        System.out.print("Auteur: ");
//        auteur = Lire.S();
//
//        System.out.print("Source: ");
//        try {
//            source = new URL(Lire.S());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public String toString() {
        return titre + '|' + auteur + '|' + date + '|' + source;
    }

    @Override
    public int compareTo(News n) {
        return date.compareTo(n.date);
    }
}
