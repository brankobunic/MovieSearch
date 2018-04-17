package com.bravostudio.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;




public class Utils {

	public static JSONArray parseJSON(String url) {

		StringBuilder builderJson = new StringBuilder();

		try {
			URL jsonURL = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(jsonURL.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				builderJson.append(inputLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONArray json = new JSONArray(builderJson.toString());

		return json;
	}

}
