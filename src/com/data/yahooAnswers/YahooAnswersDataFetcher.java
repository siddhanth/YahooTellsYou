package com.data.yahooAnswers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import DataFlowInterface.DataFetcher;
import DataFlowInterface.RawData;


public class YahooAnswersDataFetcher implements DataFetcher{
	
	public RawData getResult(String query) {
		RawData rawData = null;
		String request = getRequest(query);
		InputStream rstream = null;
		try {
			rstream = postRequest(request);
		} catch(IOException ioException){
			
		}
		
		if(rstream != null){
			// Process response
			Document response = null;
			try {
				response = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().parse(rstream);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}

			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			// Get all search Result nodes
			NodeList nodes = null;
			int nodeCount = 0;
			
			try {
				nodes = (NodeList) xPath.evaluate(
						"query/results/Question", response, XPathConstants.NODESET);
				nodeCount = nodes.getLength();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			// iterate over search Result nodes
			String answers = "";
			String questions = "";
			// int length = nl.getLength();
			HashMap<String, String> questID = new HashMap<String, String>();
			HashMap<String, String> IDanswer = new HashMap<String, String>();
			for (int i = 0; i < nodeCount; i++) {
				// Get each xpath expression as a string
				String title = null;
				try {
					title = (String) xPath.evaluate("Subject",
							nodes.item(i), XPathConstants.STRING);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String summary = null;
				try {
					summary = (String) xPath.evaluate("ChosenAnswer",
							nodes.item(i), XPathConstants.STRING);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					String chosenid = (String) xPath.evaluate("ChosenAnswerId",
							nodes.item(i), XPathConstants.STRING);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String url = null;
				try {
					url = (String) xPath.evaluate("Url", nodes.item(i),
							XPathConstants.STRING);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// print out the Title, Summary, and URL for each search result
				System.out.println("Question: " + title);
				questions += title + '~';
				SecureRandom random = new SecureRandom();
				String hash = new BigInteger(130, random).toString(32);
				questID.put(title, hash);
				IDanswer.put(hash, summary);
				System.out.println("URL: " + url);
				answers += summary + " oNeMoRe ";
				System.out.println("-----------");
			}
			
			
			Penalizer penalizer = new Penalizer();
			HashMap<String, Double> score = penalizer.penalize(questions, query
					.replaceAll("%20", " ").trim());
			ArrayList<String> keys = new ArrayList<String>();
			for (String key : score.keySet()) {
				if (score.get(key) <= 0) {
					keys.add(key);
				}
			}
			for (String key : keys) {
				// System.out.println(key);
				score.remove(key);
			}
			if (score.size() < 3) {
				System.out.println("Hmm, guess we don't know ! Wanna <a href=\"http://answers.yahoo.com/\">ask</a>?");
				System.exit(0);
			}
			ValueComparator bvc = new ValueComparator(score);
			TreeMap<String, Double> sorted_map = new TreeMap(bvc);
			sorted_map.putAll(score);
			int count = 0;
			HashMap<String, Double> answerscore = new HashMap<String, Double>();
			answers = query + " qUeStIoNbReAk ";
			for (String key : sorted_map.keySet()) {
				if (++count > 10)
					break;
				// System.out.println(key);
				answerscore.put(IDanswer.get(questID.get(key)).toLowerCase(),
						score.get(key));
				// System.out.println(IDanswer.get(questID.get(key)));
				answers += key + " " + IDanswer.get(questID.get(key))
						+ " oNeMoRe ";
			}
		}
		return rawData;
	}
	
	@Override
	public String getRequest(String query) {
		String request = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20answers.search%20where%20query%3D'AND%20(%22"
				+ query.replaceAll(" ", "%20")
				+ "%22)'%20and%20type%3D%22resolved%22&diagnostics=true";
		return request;
	}

	@Override
	public InputStream postRequest(String request) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(request);

		int statusCode = client.executeMethod(method);
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + method.getStatusLine());
			return null;
		} else {
			return method.getResponseBodyAsStream();
		}
	}
}
