package com.bravostudio.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryRescorer;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import com.bravostudio.lucene.Indexer;
import com.bravostudio.utils.Utils;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "https://api.myjson.com/bins/110ew3";
	Path dir = Paths.get("src/main/resources/lucene-database");

	public MainServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String inputSearch = request.getParameter("movieSearch");

		Directory indexDirectory = FSDirectory.open(dir);
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		JSONArray responseJson = new JSONArray();

		if (inputSearch != null) {

			try {
				IndexSearcher indexSearcher = new IndexSearcher(indexReader);
				Analyzer analyzer = new StandardAnalyzer();

				MultiFieldQueryParser parser = new MultiFieldQueryParser(
						new String[] { "title", "actors", "directors", "writers" }, analyzer);

				TopDocs top = indexSearcher.search(parser.parse(inputSearch + "*"), 10);

				for (ScoreDoc score : top.scoreDocs) {
					Document doc = indexSearcher.doc(score.doc);
					responseJson.put(doc.get("title"));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		
		response.getWriter().append(responseJson.toString()).close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
