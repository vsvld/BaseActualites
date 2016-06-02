import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Database class.
 *
 * @author ALOKHIN Vsevolod
 * @author NIKONOVYCH Daria
 * @author TEN Alina
 */
public class BaseDeNews {
    private ArrayList<News> base;               // collection of news
    private transient RAMDirectory directory;   // in-memory representation of the index, not serializable
    private transient IndexWriter indexWriter;  // for creating the index, not serializable

    /**
     * Initialize DB.
     */
    public void initialiser() {
        base = new ArrayList<>();
        directory = new RAMDirectory();
    }

    /**
     * Get collection of elements in DB
     * @return
     */
    public ArrayList<News> getBase() {
        return base;
    }

    /**
     * Add news into DB.
     * @param news  news to be added
     * @throws IOException
     */
    public void ajouter(News news) throws IOException {
        base.add(news);

        indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
        Document doc = new Document();

        StringBuilder contenu = new StringBuilder();
        contenu.append(news.getTitre()).append(" ").append(news.getAuteur());
        if (news.getClass().getSimpleName().equals("ArticleDePresse")) contenu.append(((ArticleDePresse) news).getTexte());

        doc.add(new IntField("baseIndex", base.indexOf(news), Field.Store.YES));
        doc.add(new StringField("contenu", contenu.toString(), Field.Store.YES));

        indexWriter.addDocument(doc);
        indexWriter.close();
    }

    /**
     * Delete the news.
     * @param news news to be deleted from DB
     */
    public void supprimer(News news) {
        base.remove(news);
    }

    /**
     * Serializing DB to file.
     * @param file file to which serialize
     * @throws IOException
     */
    public void ecrireDansFichier(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(base);
        }
    }

    /**
     * Deseralizing DB from file.
     * @param file file from which deserialize
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void lireLeFichier(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fos = new FileInputStream(file); ObjectInputStream oos = new ObjectInputStream(fos)) {
            base = (ArrayList<News>) oos.readObject();
            directory = new RAMDirectory();
            indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));

            for (News news : base) {
                Document doc = new Document();

                StringBuilder contenu = new StringBuilder();
                contenu.append(news.getTitre()).append(" ").append(news.getAuteur());
                if (news.getClass().getSimpleName().equals("ArticleDePresse")) contenu.append(((ArticleDePresse) news).getTexte());

                doc.add(new IntField("baseIndex", base.indexOf(news), Field.Store.YES));
                doc.add(new StringField("contenu", contenu.toString(), Field.Store.YES));
                indexWriter.addDocument(doc);
            }

            indexWriter.close();
        }
    }

    /**
     * Change elements in DB.
     * @param old old news
     * @param neww new news
     * @throws IOException
     */
    public void changer(News old, News neww) throws IOException {
        base.set(base.indexOf(old), neww);

        indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
        Document doc = new Document();
        doc.add(new IntField("baseIndex", base.indexOf(neww), Field.Store.NO));
        StringBuilder contenu = new StringBuilder();
        contenu.append(neww.getTitre()).append(" ").append(neww.getAuteur());
        if (neww.getClass().getSimpleName().equals("ArticleDePresse")) contenu.append(((ArticleDePresse) neww).getTexte());

        doc.add(new IntField("baseIndex", base.indexOf(neww), Field.Store.YES));
        doc.add(new StringField("contenu", contenu.toString(), Field.Store.YES));
        indexWriter.updateDocument(new Term("contenu", old.getTitre()), doc);

        indexWriter.close();
    }

    /**
     * Search in DB
     * @param searchString search query
     * @return news elements that correspond the search
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<News> chercher(String searchString) throws IOException, ParseException {
        ArrayList<News> news = new ArrayList<>();

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("contenu", analyzer);
        queryParser.setAllowLeadingWildcard(true);
        Query query = queryParser.parse("*" + searchString + "*");
        TopDocs results = searcher.search(query, 10);

        for (ScoreDoc scoreDoc : results.scoreDocs) {
            String ind = String.valueOf(searcher.doc(scoreDoc.doc).get("baseIndex"));
            news.add(base.get(Integer.parseInt(ind)));
        }

        return news;
    }
}
