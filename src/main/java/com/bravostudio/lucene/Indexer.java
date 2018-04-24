package com.bravostudio.lucene;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bravostudio.utils.Utils;

public class Indexer {

	Path indexPath;
	String jsonURL;
	private static final String ACTORS = "actors";
	private static final String WRITERS = "writers";
	private static final String DIRECTORS = "directors";
	private static final String TITLE = "title";

	IndexWriter indexWriter = null;

	public Indexer(Path indexPath, String jsonURL) {
		super();
		this.indexPath = indexPath;
		this.jsonURL = jsonURL;
	}

	public void createIndex() {
		JSONArray jsonObjects = Utils.parseJSON(jsonURL);
		openIndex();
		addDocuments(jsonObjects);
		finish();
	}

	public void openIndex() {

		Directory dir;

		try {

			dir = FSDirectory.open(indexPath);
			indexWriter = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDocuments(JSONArray json) {

		for (int i = 0; i < json.length(); i++) {

			Document doc = new Document();
			JSONObject object = json.getJSONObject(i);

			doc.add(new TextField(TITLE, object.getString(TITLE), Field.Store.YES));
			doc.add(new TextField(ACTORS, object.getJSONArray(ACTORS).toString(), Field.Store.YES));
			doc.add(new TextField(WRITERS, object.getJSONArray(WRITERS).toString(), Field.Store.YES));
			doc.add(new TextField(DIRECTORS, object.getJSONArray(DIRECTORS).toString(), Field.Store.YES));

			try {
				indexWriter.addDocument(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void finish() {
		try {
			indexWriter.commit();
			indexWriter.close();
		} catch (IOException ex) {
			System.err.println("We had a problem closing the index: " + ex.getMessage());
		}
	}

}
