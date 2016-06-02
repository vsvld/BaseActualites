import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;

/**
 * Abstract news class.
 *
 * @author ALOKHIN Vsevolod
 * @author NIKONOVYCH Daria
 * @author TEN Alina
 */
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

    @Override
    public String toString() {
        return titre + '|' + auteur + '|' + date + '|' + source;
    }

    @Override
    public int compareTo(News n) {
        return date.compareTo(n.date);
    }
}
