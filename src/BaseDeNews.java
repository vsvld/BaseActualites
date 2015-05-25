import java.io.*;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by vsevolod on 16.04.15.
 */
public class BaseDeNews implements Serializable {
    private TreeSet<News> base;

    public void initialiser() {
        base = new TreeSet<>();
    }

    public void ajoute(News news) {
        base.add(news);
    }

    public void afficher() {
        for (News news : base) {
            System.out.println(news);
        }
    }

    public void supprimer() {

    }

    public void ecrireDansFichier(String fichier) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fichier); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(base);
        }
    }

    public void lireDansFichier(String fichier) throws IOException, ClassNotFoundException {
        try (FileInputStream fos = new FileInputStream(fichier); ObjectInputStream oos = new ObjectInputStream(fos)) {
            base = (TreeSet<News>) oos.readObject();
        }
    }
}
