import java.net.URL;
import java.time.LocalDate;

/**
 * Created by vsevolod on 16.04.15.
 */
public class ArticleDePresse extends News {
    private String texte;
    private URL versionLongue;
    private boolean uniquementElectronique;

    public ArticleDePresse(String titre, String auteur, LocalDate date, URL source, String texte, URL versionLongue, boolean uniquementElectronique) {
        super(titre, auteur, date, source);
        this.texte = texte;
        this.versionLongue = versionLongue;
        this.uniquementElectronique = uniquementElectronique;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public URL getVersionLongue() {
        return versionLongue;
    }

    public void setVersionLongue(URL versionLongue) {
        this.versionLongue = versionLongue;
    }

    public boolean isUniquementElectronique() {
        return uniquementElectronique;
    }

    public void setUniquementElectronique(boolean uniquementElectronique) {
        this.uniquementElectronique = uniquementElectronique;
    }

    @Override
    public String toString() {
        return super.toString() + "\n\n" +
                texte + "\n\n" +
                "Lire tout le sujet: " + versionLongue;
    }
}
