package com.data.googleSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

public class GoogleApi {

	public static void main(String[] args) throws ParseException,
			MalformedURLException, Exception {
		String query = "who killed mahatma gandhi";
		fetchResults(query);
	}

	static void fetchResults(String query) throws Exception {
		query += " site:answers.yahoo.com";
		URL url = new URL(
				"https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
						+ "q=" + query.replace(" ", "%20"));
		System.out.println(System.currentTimeMillis());

		URLConnection connection = url.openConnection();
		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		System.out.println(System.currentTimeMillis());
		JSONObject obj = new JSONObject(builder.toString());
		obj = (JSONObject) obj.get("responseData");
		JSONArray arr = (JSONArray) obj.get("results");
		System.out.println(arr.length());

	}
}
