package com.cpf.fulltextsearch.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class IndexUtils {

    private static final String INDEX_PATH = "/tmp/testIndex";

    private static final int MAX_MATCH_SIZE = 100;

    public static final String INDEX_FIELD_FILENAME = "fileName";

    public static final String INDEX_FIELD_CONTENT = "content";

    public static final String INDEX_FIELD_FILE_PATH = "path";

    public static void addFileIntoIndex(File file) {
        try (
                Analyzer chineseAnalyzer = new CJKAnalyzer();
                Directory indexStore = FSDirectory.open(Paths.get(INDEX_PATH));
                IndexWriter indexWriter = new IndexWriter(indexStore, new IndexWriterConfig(chineseAnalyzer))
        ) {

            Document document = new Document();
            document.add(new Field(INDEX_FIELD_FILENAME, file.getName(), TextField.TYPE_STORED));
            document.add(new Field(INDEX_FIELD_CONTENT, readContentAsString(file), TextField.TYPE_STORED));
            document.add(new Field(INDEX_FIELD_FILE_PATH, file.getAbsolutePath(), TextField.TYPE_STORED));

            indexWriter.addDocument(document);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static List<HitResult> searchFiles(String searchText) {
        try (
                Directory indexStore = FSDirectory.open(Paths.get(INDEX_PATH))
        ) {
            Analyzer analyzer = new CJKAnalyzer();
            QueryParser contentParser = new QueryParser(INDEX_FIELD_CONTENT, analyzer);
            QueryParser fileNameParser = new QueryParser(INDEX_FIELD_FILENAME, analyzer);

            Query contentQuery = contentParser.parse(searchText);
            Query fileNameQuery = fileNameParser.parse(searchText);

            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(indexStore));

            ScoreDoc[] hits = searcher.search(contentQuery, MAX_MATCH_SIZE).scoreDocs;

            return Arrays.stream(hits).map(scoreDoc -> newHitResultByScoreDoc(searcher, scoreDoc))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    private static HitResult newHitResultByScoreDoc(IndexSearcher searcher, ScoreDoc scoreDoc) {
        Document doc = null;
        HitResult hit = new HitResult();
        try {
            doc = searcher.doc(scoreDoc.doc);
            hit.setContent(doc.get(INDEX_FIELD_CONTENT));
            hit.setFile(new File(doc.get(INDEX_FIELD_FILE_PATH)));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return hit;
    }

    private static String readContentAsString(File file) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            StringBuffer sb = new StringBuffer();
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return "";
    }

    public void removeDocumentFromIndex(){

    }

}
