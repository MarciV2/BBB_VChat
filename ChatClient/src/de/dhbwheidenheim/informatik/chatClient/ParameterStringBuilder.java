package de.dhbwheidenheim.informatik.chatClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ParameterStringBuilder {
	/**
	 * Methode, um Parameter einfach für HTTP-GET-Requests zu formatieren
	 * 
	 * @param params Map mit key-value-paaren
	 * @return String, der nach vorausstehendem % an URL angehangen werden kann
	 * @throws UnsupportedEncodingException
	 */
	public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			// Alle Parameter durchlaufen, String anreihen mit "key=value&..."
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		// letztes Zeichen (&) löschen, da keine parameter folgen (sofern string
		// überhaupt zeichen enthält)
		if (resultString.length() > 0)
			return resultString.substring(0, resultString.length() - 1);
		else
			return resultString;
	}
}