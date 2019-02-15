package com.azure.search.service;

import java.io.IOException;
import java.util.List;

import com.azure.search.model.Document;

public interface SearchService {

boolean createIndex() throws IOException;

boolean createDatasource() throws IOException;

boolean createIndexer() throws IOException;

boolean syncIndexerData() throws IOException, InterruptedException;

List<Document> search(String pattern);

}
