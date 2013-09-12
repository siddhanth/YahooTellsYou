package com.data.yahooAnswers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
	
	@Override
	public List<RawData> getResult(String query) {
		String request = getRequest(query);
		InputStream rstream = null;
		List<RawData> rawDataList = null;
		try {
			rstream = postRequest(request);
			rawDataList = populateData(query, rstream);
		} catch(IOException ioException){
			ioException.printStackTrace();
		}
		
		return rawDataList;
	}
	
	private List<RawData> populateData(String query, InputStream rstream) {
		List<RawData> rawDataList = null;
		//System.out.println("Query::"+query);
		if(rstream != null){
			// Process response
			Document response = null;
			try {
				response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rstream);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}

			//System.out.println("RS Stream::" + response);
			
			rawDataList = getRawData(response);
			rawDataList = getSortedAndRankedData(query, rawDataList);
		}
		return rawDataList;
	}


	private List<RawData> getSortedAndRankedData(String query, List<RawData> rawDataList) {
		Penalizer penalizer = new Penalizer();
		HashMap<RawData, Double> score = penalizer.penalize(rawDataList, query
				.replaceAll("%20", " ").trim());
		
		ArrayList<RawData> keys = new ArrayList<RawData>();
		for (RawData key : score.keySet()) {
			System.out.println(score.get(key));
			if (score.get(key) <= 0) {
				keys.add(key);
			}
		}

		for (RawData key : keys) {
			score.remove(key);
		}
		
		if (score.size() < 2.5) {
			System.out.println("Hmm, guess we don't know ! Wanna <a href=\"http://answers.yahoo.com/\">ask</a>?");
			System.exit(0);
		}
		
		
		for(int i=0; i< rawDataList.size(); i++) {
		//	System.out.println(rawDataList.get(i).getAnswer());
		}
		
		ValueComparator bvc = new ValueComparator(score);
		TreeMap<RawData, Double> sorted_map = new TreeMap(bvc);
		sorted_map.putAll(score);
		
		List<RawData> sortedRawDataList = new ArrayList<RawData>();
		
		int size = sorted_map.size();
		for(RawData key:sorted_map.keySet()){
			System.out.println("Answer::" + key.getAnswer());
			sortedRawDataList.add(key);
		}
		
		return sortedRawDataList;
	}



	private ArrayList<RawData> getRawData(Document response) {
		ArrayList<RawData> rawDataList = new ArrayList<RawData>();
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		// Get all search Result nodes
		NodeList nodes = null;
		int nodeCount = 0;
		
		try {
			nodes = (NodeList) xPath.evaluate("query/results/Question", response, XPathConstants.NODESET);
			nodeCount = nodes.getLength();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		// iterate over search Result nodes		
		// int length = nl.getLength();
		
		for (int i = 0; i < nodeCount; i++) {
			RawData data = new RawData();
			// Get each xpath expression as a string
			try {
				String title = (String) xPath.evaluate("Subject",nodes.item(i), XPathConstants.STRING);
				data.setQuestion(title);
				
				String summary = (String) xPath.evaluate("ChosenAnswer",nodes.item(i), XPathConstants.STRING);
				data.setAnswer(summary);
				
				String url = (String) xPath.evaluate("Url", nodes.item(i),XPathConstants.STRING);
				data.setUrl(url);
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			// print out the Title, Summary, and URL for each search result
			//System.out.println("Question: " + data.getQuestion());
			
			
			//SecureRandom random = new SecureRandom();
			//String hash = new BigInteger(130, random).toString(32);
			rawDataList.add(data);
		}
		return rawDataList;
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
			//System.out.println(method.getResponseBodyAsString());
			return method.getResponseBodyAsStream();
		}
	}
}
