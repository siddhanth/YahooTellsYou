package com.data.googleSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

public class GoogleApi implements Runnable {

	private URL url;
	private JSONArray jarr;
	private JSONObject jobj;
	private boolean processed;

	public static void main(String[] args) throws ParseException,
			MalformedURLException, Exception {
		String query = "who is the founder of facebook	";
		GoogleApi g = new GoogleApi(query);
		g.run();
		System.out.println(g.getJarr());
	}

	public GoogleApi(String query) throws Exception {
		// query += " site:answers.yahoo.com";
		processed = false;
		url = new URL(
				"https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
						+ "q=" + query.replace(" ", "%20"));

	}

	public JSONArray getJarr() {
		return jarr;
	}

	public JSONObject getJobj() {
		return jobj;
	}

	@Override
	public void run() {

		URLConnection connection;
		try {
			connection = url.openConnection();
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			jobj = new JSONObject(builder.toString());
			jobj = (JSONObject) jobj.get("responseData");
			jarr = (JSONArray) jobj.get("results");
			processed = true;
			System.out.println(getJarr());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isProcessed() {
		return processed;
	}
}
