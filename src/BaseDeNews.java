import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
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

public class BaseDeNews {
    private ArrayList<News> base;
    private transient RAMDirectory directory; // in-memory representation of the index
    private transient IndexWriter indexWriter;

    public void initialiser() {
        base = new ArrayList<>();
        directory = new RAMDirectory();
    }

    public ArrayList<News> getBase() {
        return base;
    }

    public void ajouter(News news) throws IOException {
        base.add(news);

        indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
        Document doc = new Document();
        doc.add(new IntField("baseIndex", base.indexOf(news), Field.Store.NO));
        doc.add(new StringField("titre", news.getTitre(), Field.Store.YES));
        doc.add(new StringField("auteur", news.getAuteur(), Field.Store.YES));

        indexWriter.addDocument(doc);
        indexWriter.close();
    }

    public void ecrireDansFichier(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(base);
        }
    }

    public void lireLeFichier(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fos = new FileInputStream(file); ObjectInputStream oos = new ObjectInputStream(fos)) {
            base = (ArrayList<News>) oos.readObject();
            directory = new RAMDirectory();
            indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));

            for (News news : base) {
                Document doc = new Document();
                doc.add(new IntField("baseIndex", base.indexOf(news), Field.Store.NO));
                doc.add(new StringField("titre", news.getTitre(), Field.Store.YES));
                doc.add(new StringField("auteur", news.getAuteur(), Field.Store.YES));
                indexWriter.addDocument(doc);
            }

            indexWriter.close();
        }
    }
    
    public void changer(News old, News neww) throws IOException {
        base.set(base.indexOf(old), neww);

        indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
        Document doc = new Document();
        doc.add(new IntField("baseIndex", base.indexOf(neww), Field.Store.NO));
        doc.add(new StringField("titre", neww.getTitre(), Field.Store.YES));
        doc.add(new StringField("auteur", neww.getAuteur(), Field.Store.YES));
        indexWriter.updateDocument(new Term("titre", old.getTitre()), doc);

        indexWriter.close();
    }

    public ArrayList<News> chercher(String searchString) throws IOException, ParseException {
        ArrayList<News> news = new ArrayList<>();

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();
//        QueryParser queryParser = new MultiFieldQueryParser(new String[]{"titre", "auteur"}, analyzer);
        QueryParser queryParser = new QueryParser("titre", analyzer);
        Query query = queryParser.parse(searchString);
        TopDocs results = searcher.search(query, 10);

        // debug
        System.out.println(Arrays.toString(directory.listAll()));
        System.out.println(searchString);
        System.out.println(searcher.count(query));

        for (ScoreDoc scoreDoc : results.scoreDocs) {
            String ind = String.valueOf(searcher.doc(scoreDoc.doc).getField("baseIndex"));
            System.out.println(ind);
            news.add(base.get(Integer.parseInt(ind)));
        }

        return news;
    }
}
