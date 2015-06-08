import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;

public abstract class News implements Comparable<News>, Serializable {
    private String titre, auteur, type;
    private LocalDate date;
    private URL source;

    public News() {
    }

    public News(String titre, String auteur, LocalDate date, URL source) {
        this.titre = titre;
        this.auteur = auteur;
        this.date = date;
        this.source = source;
        type = this.getClass().getSimpleName();
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

    public String getType() {
        return type;
    }

    public void afficher() {
//        if (titre != null)  System.out.println("Titre: " + titre);
//        if (date != null)   System.out.println("Date: " + date);
//        if (auteur != null) System.out.println("Auteur: " + auteur);
//        if (source != null) System.out.println("Source: " + source);
    }

    @Override
    public String toString() {
        return titre + '|' + auteur + '|' + date + '|' + source;
    }

    @Override
    public int compareTo(News n) {
        return date.compareTo(n.date);
    }
}
