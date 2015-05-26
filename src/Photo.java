import java.io.File;
import java.net.URL;
import java.time.LocalDate;

/**
 * Created by vsevolod on 16.04.15.
 */
public class Photo extends News {
    private File fichier;
    private String format;
    private int resolutionHauteur;
    private int resolutionLargeur;
    private boolean enCouleur;

    public Photo(String titre, String auteur, LocalDate date, URL source, File fichier, String format, int resolutionHauteur, int resolutionLargeur, boolean enCouleur) {
        super(titre, auteur, date, source);
        this.fichier = fichier;
        this.format = format;
        this.resolutionHauteur = resolutionHauteur;
        this.resolutionLargeur = resolutionLargeur;
        this.enCouleur = enCouleur;
    }

    public File getFichier() {
        return fichier;
    }

    public void setFichier(File fichier) {
        this.fichier = fichier;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getResolutionHauteur() {
        return resolutionHauteur;
    }

    public void setResolutionHauteur(int resolutionHauteur) {
        this.resolutionHauteur = resolutionHauteur;
    }

    public int getResolutionLargeur() {
        return resolutionLargeur;
    }

    public void setResolutionLargeur(int resolutionLargeur) {
        this.resolutionLargeur = resolutionLargeur;
    }

    public boolean isEnCouleur() {
        return enCouleur;
    }

    public void setEnCouleur(boolean enCouleur) {
        this.enCouleur = enCouleur;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "Photo: '" + fichier.getName() + "'|" + format + "|" +
                resolutionLargeur + 'x' + resolutionHauteur + '|' +
                (enCouleur ? "en couleur" : "noir et blanc") ;
    }
}
