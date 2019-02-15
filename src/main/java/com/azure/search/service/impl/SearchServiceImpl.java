package com.azure.search.service.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.search.model.Document;
import com.azure.search.service.SearchService;
import com.azure.search.util.SearchUtil;


@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchUtil searchUtil;

	@Value("${SearchServiceApiKey}")
	private String _apiKey;
	@Value("${DataSourceName}")
	private String DataSourceName;
	@Value("${DataSourceType}")
	private String _DataSourceType;
	@Value("${DataSourceConnectionString}")
	private String DataSourceConnectionString;
	@Value("${DataSourceTable}")
	private String DataSourceTable;
	@Value("${IndexName}")
	private String IndexName;

	@Override
	public boolean createIndex() throws IOException {
		searchUtil.logMessage("\n Creating Index...");

		URL url = searchUtil.getCreateIndexURL();

		HttpsURLConnection connection = searchUtil.getHttpURLConnection(url, "PUT", _apiKey);
		connection.setDoOutput(true);

		// Index definition
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
		outputStreamWriter.append("{\"fields\":[");
		outputStreamWriter.append(
				"{\"name\": \"FEATURE_ID\"	, \"type\": \"Edm.String\"			, \"key\": true	, \"searchable\": false, 	\"filterable\": false, \"sortable\": false, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"FEATURE_NAME\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"FEATURE_CLASS\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"STATE_ALPHA\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"STATE_NUMERIC\"	, \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"COUNTY_NAME\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"COUNTY_NUMERIC\", \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"LOCATION\"		, \"type\": \"Edm.GeographyPoint\"	, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"ELEV_IN_M\"		, \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"ELEV_IN_FT\"	, \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"MAP_NAME\"		, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"DESCRIPTION\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": false, \"sortable\": false, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"HISTORY\"		, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": false, \"sortable\": false, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"DATE_CREATED\"	, \"type\": \"Edm.DateTimeOffset\"	, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter.append(
				"{\"name\": \"DATE_EDITED\"	, \"type\": \"Edm.DateTimeOffset\"	, \"key\": false, \"searchable\": false, 	\"filterable\": true, \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true}");
		outputStreamWriter.append("]}");
		outputStreamWriter.close();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		return searchUtil.isSuccessResponse(connection);
	}

	@Override
	public boolean createDatasource() throws IOException {
		searchUtil.logMessage("\n Creating Indexer Data Source...");

		URL url = searchUtil.getCreateIndexerDatasourceURL();
		HttpsURLConnection connection = searchUtil.getHttpURLConnection(url, "PUT", _apiKey);
		connection.setDoOutput(true);

		String dataSourceRequestBody = "{ 'description' : 'USGS Dataset','type' : '" + _DataSourceType
				+ "','credentials' : " + DataSourceConnectionString + ",'container' : { 'name' : '" + DataSourceTable
				+ "' }} ";

		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
		outputStreamWriter.write(dataSourceRequestBody);
		outputStreamWriter.close();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		return searchUtil.isSuccessResponse(connection);
	}

	@Override
	public boolean createIndexer() throws IOException {
		searchUtil.logMessage("\n Creating Indexer...");

		URL url = searchUtil.getCreateIndexerURL();
		HttpsURLConnection connection = searchUtil.getHttpURLConnection(url, "PUT", _apiKey);
		connection.setDoOutput(true);

		String indexerRequestBody = "{ 'description' : 'USGS data indexer', 'dataSourceName' : '" + DataSourceName
				+ "', 'targetIndexName' : '" + IndexName
				+ "' ,'parameters' : { 'maxFailedItems' : 10, 'maxFailedItemsPerBatch' : 5, 'base64EncodeKeys': false }}";

		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
		outputStreamWriter.write(indexerRequestBody);
		outputStreamWriter.close();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		return searchUtil.isSuccessResponse(connection);
	}

	@Override
	public boolean syncIndexerData() throws IOException, InterruptedException {
		searchUtil.logMessage("\n Syncing data...");

		// Run indexer
		URL url = searchUtil.getRunIndexerURL();
		HttpsURLConnection connection = searchUtil.getHttpURLConnection(url, "POST", _apiKey);
		connection.setRequestProperty("Content-Length", "0");
		connection.setDoOutput(true);
		connection.getOutputStream().flush();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		if (!searchUtil.isSuccessResponse(connection)) {
			return false;
		}

		// Check indexer status
		searchUtil.logMessage("Synchronization running...");

		boolean running = true;
		URL statusURL = searchUtil.getIndexerStatusURL();
		connection = searchUtil.getHttpURLConnection(statusURL, "GET", _apiKey);

		while (running) {
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return false;
			}

			JsonReader jsonReader = Json.createReader(connection.getInputStream());
			JsonObject responseJson = jsonReader.readObject();

			if (responseJson != null) {
				JsonObject lastResultObject = responseJson.getJsonObject("lastResult");

				if (lastResultObject != null) {
					String inderxerStatus = lastResultObject.getString("status");

					if (inderxerStatus.equalsIgnoreCase("inProgress")) {
						searchUtil.logMessage("Synchronization running...");
						Thread.sleep(1000);

					} else {
						running = false;
						searchUtil.logMessage("Synchronized " + lastResultObject.getInt("itemsProcessed") + " rows...");
					}
				}
			}
		}

		return true;
	}

	@Override
	public List<Document> search(String pattern) {

		if (pattern == null || pattern.isEmpty()) {
			pattern = "*";
		}

		try {
			URL url = searchUtil.getSearchURL(URLEncoder.encode(pattern, StandardCharsets.UTF_8.toString()));
			HttpsURLConnection connection = searchUtil.getHttpURLConnection(url, "GET", _apiKey);

			javax.json.JsonReader jsonReader =  Json.createReader(connection.getInputStream());
			JsonObject jsonObject = jsonReader.readObject();
			JsonArray jsonArray = jsonObject.getJsonArray("value");
			 jsonReader.close();

			System.out.println(connection.getResponseMessage());
			System.out.println(connection.getResponseCode());

			if (searchUtil.isSuccessResponse(connection)) {
				return jsonToDocument(jsonArray);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<Document> jsonToDocument(JsonArray jsonArray) {
		List<Document> result = new ArrayList<Document>();

		if (jsonArray == null) {
			return result;
		}

		for (JsonValue jsonDoc : jsonArray) {
			JsonObject object = (JsonObject) jsonDoc;

			Document document = new Document();
			document.setFeatureName(object.getString("FEATURE_NAME"));
			document.setFeatureClass(object.getString("FEATURE_CLASS"));
			document.setStateAlpha(object.getString("STATE_ALPHA"));
			document.setCountyName(object.getString("COUNTY_NAME"));
			document.setElevationMeter(object.getInt("ELEV_IN_M"));
			document.setElevationFt(object.getInt("ELEV_IN_FT"));
			document.setMapName(object.getString("MAP_NAME"));
			document.setDescription(object.getString("DESCRIPTION"));
			document.setHistory(object.getString("HISTORY"));
			JsonValue dateValue = object.get("DATE_CREATED");
			document.setDateCreated(dateValue == JsonValue.NULL ? " " : dateValue.toString());
			dateValue = object.get("DATE_EDITED");
			document.setDateEdited(dateValue == JsonValue.NULL ? " " : dateValue.toString());

			JsonValue location = object.get("LOCATION");

			if (location != JsonValue.NULL) {
				JsonArray coordinatesArray = ((JsonObject) location).getJsonArray("coordinates");

				if (coordinatesArray != null && coordinatesArray.size() == 2) {
					JsonValue latitudeValue = coordinatesArray.get(0);
					JsonValue longitudeValue = coordinatesArray.get(1);

					String latitude = latitudeValue == null ? "" : latitudeValue.toString();
					String longitude = longitudeValue == null ? "" : longitudeValue.toString();

					document.setLatitude(latitude);
					document.setLongitude(longitude);
				}
			}

			result.add(document);
		}

		return result;
	}

}
