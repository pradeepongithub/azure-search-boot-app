package com.azure.search.model;

import lombok.Data;

@Data
public class Document {
	private String featureName = "";
	private String featureClass = "";
	private String stateAlpha = "";
	private String countyName = "";
	private String longitude = "";
	private String latitude = "";
	private String mapName = "";
	private int elevationMeter = 0;
	private int elevationFt = 0;
	private String description = "";
	private String history = "";
	private String dateCreated = "";
	private String dateEdited = "";

	
}
