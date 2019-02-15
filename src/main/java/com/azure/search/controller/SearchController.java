package com.azure.search.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.search.model.Document;
import com.azure.search.service.SearchService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/v1/ontology")
public class SearchController {
	
@Autowired
private SearchService service;

@ApiOperation(value = "create index", response = Boolean.class)
@PutMapping("/index")
public boolean createIndex() throws IOException {
	return service.createIndex();
}
@ApiOperation(value = "create data source", response = Boolean.class)
@PutMapping("/datasource")
public boolean createDatasource() throws IOException {
	return service.createDatasource();
}
@ApiOperation(value = "create indexer", response = Boolean.class)
@PutMapping("/indexer")
public boolean createIndexer() throws IOException {
	return service.createIndexer();
}
@ApiOperation(value = "synch indexer data", response = Boolean.class)
@PutMapping("/synch")
public boolean syncIndexerData() throws IOException, InterruptedException {
	return service.syncIndexerData();
}
@ApiOperation(value = "search ontology by pattern", response = Document.class)
@GetMapping("/search")
public List<Document> search(@RequestParam("pattern") String pattern){
	return service.search(pattern);
	
}


}
