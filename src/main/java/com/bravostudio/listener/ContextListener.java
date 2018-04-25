package com.bravostudio.listener;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.bravostudio.lucene.Indexer;

@WebListener()
public class ContextListener implements ServletContextListener {

	Path dir = Paths.get("resources/lucene-database");
	static final String URL = "https://api.myjson.com/bins/110ew3";

	public void contextInitialized(ServletContextEvent sce) {
		Indexer indexer = new Indexer(dir, URL);
		indexer.createIndex();

		try {
			Directory indexDirectory = FSDirectory.open(dir);
			sce.getServletContext().setAttribute("indexDirectory", indexDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void contextDestroyed(ServletContextEvent sce) {

		try {
			Directory indexDirectory = (Directory) sce.getServletContext().getAttribute("indexDirectory");
			if (indexDirectory != null) {
				indexDirectory.close();
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
