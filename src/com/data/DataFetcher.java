package com.data;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpException;

import DataFlowInterface.RawData;

public interface DataFetcher {
    /*
     * Data can come from different sources like yahoo answers, google search, bing search etc.
     */
	RawData getResult(String query);
	String getRequest(String query);
	InputStream postRequest(String request) throws HttpException, IOException;
}
