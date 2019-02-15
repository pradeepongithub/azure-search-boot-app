package com.azure.search.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Formatter;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SearchUtil {

	private static final String _searchURL = "https://%s.search.windows.net/indexes/%s/docs?api-version=%s&search=%s&searchMode=all";
	private static final String _createIndexURL = "https://%s.search.windows.net/indexes/%s?api-version=%s";
	private static final String _createIndexerDatasourceURL = "https://%s.search.windows.net/datasources/%s?api-version=%s";
	private static final String _createIndexerURL = "https://%s.search.windows.net/indexers/%s?api-version=%s";
	private static final String _runIndexerURL = "https://%s.search.windows.net/indexers/%s/run?api-version=%s";
	private static final String _getIndexerStatusURL = "https://%s.search.windows.net/indexers/%s/status?api-version=%s";

	@Value("${SearchServiceName}")
	private String SearchServiceName;
	@Value("${IndexName}")
	private String IndexName;
	@Value("${ApiVersion}")
	private String ApiVersion;
	@Value("${IndexerName}")
	private String IndexerName;
	@Value("${DataSourceName}")
	private String DataSourceName;

	public URL getSearchURL(String query) throws MalformedURLException {
		Formatter strFormatter = new Formatter();
		strFormatter.format(_searchURL, SearchServiceName, IndexName, ApiVersion, query);
		String url = strFormatter.out().toString();
		strFormatter.close();

		return new URL(url);
	}

	public URL getCreateIndexURL() throws MalformedURLException {
		Formatter strFormatter = new Formatter();
		strFormatter.format(_createIndexURL, SearchServiceName, IndexName, ApiVersion);
		String url = strFormatter.out().toString();
		strFormatter.close();

		return new URL(url);
	}

	public URL getCreateIndexerURL() throws MalformedURLException {
		Formatter strFormatter = new Formatter();
		strFormatter.format(_createIndexerURL, SearchServiceName, IndexerName, ApiVersion);
		String url = strFormatter.out().toString();
		strFormatter.close();

		return new URL(url);
	}

	public URL getCreateIndexerDatasourceURL() throws MalformedURLException {
		Formatter strFormatter = new Formatter();
		strFormatter.format(_createIndexerDatasourceURL, SearchServiceName, DataSourceName, ApiVersion);
		String url = strFormatter.out().toString();
		strFormatter.close();

		return new URL(url);
	}

	public URL getRunIndexerURL() throws MalformedURLException {
		Formatter strFormatter = new Formatter();
		strFormatter.format(_runIndexerURL, SearchServiceName, IndexerName, ApiVersion);
		String url = strFormatter.out().toString();
		strFormatter.close();

		return new URL(url);
	}

	public URL getIndexerStatusURL() throws MalformedURLException {
		Formatter strFormatter = new Formatter();
		strFormatter.format(_getIndexerStatusURL, SearchServiceName, IndexerName, ApiVersion);
		String url = strFormatter.out().toString();
		strFormatter.close();

		return new URL(url);
	}

	public HttpsURLConnection getHttpURLConnection(URL url, String method, String apiKey) throws IOException {
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("api-key", apiKey);

		return connection;
	}

	public void logMessage(String message) {
		System.out.println(message);
	}

	public boolean isSuccessResponse(HttpsURLConnection connection) {
		try {
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_ACCEPTED
					|| responseCode == HttpURLConnection.HTTP_NO_CONTENT
					|| responseCode == HttpsURLConnection.HTTP_CREATED) {
				return true;
			}

			// We got an error
			if (connection.getErrorStream() != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					logMessage(inputLine);
				}

				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
