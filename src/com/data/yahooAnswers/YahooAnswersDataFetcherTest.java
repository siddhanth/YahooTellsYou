package com.data.yahooAnswers;

public class YahooAnswersDataFetcherTest {

	public static void main(String[] args) {
		String query = "Who killed Mahatma Gandhi?";
		YahooAnswersDataFetcher dataFetcher = new YahooAnswersDataFetcher();
		dataFetcher.getResult(query);
	}
}
