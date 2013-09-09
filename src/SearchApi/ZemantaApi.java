package SearchApi;

import java.util.HashMap;

import com.zemanta.api.Zemanta;
import com.zemanta.api.ZemantaResult;

public class ZemantaApi {

	public static void main(String[] args) {

		final String API_SERVICE_URL = "http://api.zemanta.com/services/rest/0.0/";

		String apiKey = "oe2fh2udxiuqqcgtgf2wqkqu";
		String text = "mahatma gandhi was killed by nathuram godse by a pistol";

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("method", "zemanta.suggest_markup");
		parameters.put("api_key", apiKey);
		parameters.put("text", text);
		parameters.put("format", "xml");

		Zemanta zem = new Zemanta(apiKey, API_SERVICE_URL);

		// Example 1: suggest
		ZemantaResult zemResult = zem.suggest(parameters);
		if (!zemResult.isError) {
			System.out.println(zemResult);
		}

		// Example 2: suggest markup
		ZemantaResult zemMarkup = zem.suggestMarkup(text);
		if (!zemMarkup.isError) {
			System.out.println(zemMarkup);
		}

		// Example 3: json or rdfxml
		parameters.put("format", "json");
		String resultJSON = zem.getRawData(parameters);
		System.out.println(resultJSON);
	}
}
