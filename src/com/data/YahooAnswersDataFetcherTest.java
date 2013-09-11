package com.data;

public class YahooAnswersDataFetcherTest {

	public static void main(String[] args) {
		String query = "Who killed Mahatama Gandhi";
		YahooAnswersDataFetcher dataFetcher = new YahooAnswersDataFetcher();
		dataFetcher.getResult(query);
	}
}
