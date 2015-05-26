import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by vsevolod on 16.04.15.
 */
public class BaseDeNews {
    private ArrayList<News> base;

    public void initialiser() {
        base = new ArrayList<>();
    }

    public ArrayList<News> getBase() {
        return base;
    }

    public void ajouter(News news) {
        base.add(news);
    }

    public void ecrireDansFichier(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(base);
        }
    }

    public void lireDansFichier(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fos = new FileInputStream(file); ObjectInputStream oos = new ObjectInputStream(fos)) {
            base = (ArrayList<News>) oos.readObject();
        }
    }
}
