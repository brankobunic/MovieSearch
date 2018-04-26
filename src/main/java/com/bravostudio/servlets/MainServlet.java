package com.bravostudio.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.json.JSONArray;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MainServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String inputSearch = request.getParameter("movieSearch");
		JSONArray responseJson = new JSONArray();

		if (inputSearch != null) {

			try {
				IndexSearcher indexSearcher = (IndexSearcher) getServletContext().getAttribute("indexSearcher");
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

		response.setContentType("application/json");
		response.getWriter().append(responseJson.toString()).close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
