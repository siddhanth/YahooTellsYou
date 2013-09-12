package com.data.bingSearch;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;

public class bingApi {

	public static void main(String[] args) throws Exception {

		String query = "Who killed mahatma gandhi";
		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27"
				+ query.replace(" ", "%20") + "%27";

		String accountKey = "ugKMNFH1ELq7lwL4Kd0ImsQSgAP/cM9WQjPTeDt4JTU";
		byte[] accountKeyBytes = Base64
				.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic "
				+ accountKeyEnc);
		// FROM HERE DOWN IS WHAT SEEMS TO TAKE FOREVER.
		InputStream is = urlConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);

		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuilder sb = new StringBuilder();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}

		System.out.println(sb.toString());
	}
}
