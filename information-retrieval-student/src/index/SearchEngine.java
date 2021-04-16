/*
 * Copyright 2017 Marc Liberatore.
 */

package index;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.io.*;

import comparators.TfIdfComparator;
import documents.DocumentId;

/**
 * A simplified document indexer and search engine.
 * 
 * Documents are added to the engine one-by-one, and uniquely identified by a DocumentId.
 *
 * Documents are internally represented as "terms", which are lowercased versions of each word 
 * in the document. 
 * 
 * Queries for terms are also made on the lowercased version of the term. Terms are 
 * therefore case-insensitive.
 * 
 * Lookups for documents can be done by term, and the most relevant document(s) to a specific term 
 * (as computed by tf-idf) can also be retrieved.
 *
 * See:
 * - <https://en.wikipedia.org/wiki/Inverted_index>
 * - <https://en.wikipedia.org/wiki/Search_engine_(computing)> 
 * - <https://en.wikipedia.org/wiki/Tf%E2%80%93idf>
 * 
 * @author Marc Liberatore
 *
 */
public class SearchEngine {

	public Map<DocumentId, Map<String, Integer>> ultTermsDocIds;

	public SearchEngine() {
		ultTermsDocIds = new HashMap<>();
	}
	
	/**
	 * Inserts a document into the search engine for later analysis and retrieval.
	 * 
	 * The document is uniquely identified by a documentId; attempts to re-insert the same 
	 * document are ignored.
	 * 
	 * The document is supplied as a Reader; this method stores the document contents for 
	 * later analysis and retrieval.
	 * 
	 * @param documentId
	 * @param reader
	 * @throws IOException iff the reader throws an exception 
	 */
	public void addDocument(DocumentId documentId, Reader reader) throws IOException {
		Map<String, Integer> innerMap = new HashMap<>();
		int termCount = 0;
		BufferedReader br = new BufferedReader(reader);
		String[] term = null;
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			term = line.split("\\W");
			for (int i = 0; i < term.length; i++) {
				if (term[i].trim().length() > 0 && !innerMap.containsKey(term[i].trim().toLowerCase())) {
					innerMap.put(term[i].trim().toLowerCase(), 1);
				}
				else if (term[i].trim().length() > 0 && innerMap.containsKey(term[i].trim().toLowerCase())) {
					termCount = innerMap.get(term[i].trim().toLowerCase());
					termCount++;
					innerMap.replace(term[i].trim().toLowerCase(), termCount);
				}
			}
		}
		ultTermsDocIds.put(documentId, innerMap);
	}
	
	/**
	 * Returns the set of DocumentIds contained within the search engine that contain a given term.
	 * 
	 * @param term
	 * @return the set of DocumentIds that contain a given term
	 */
	public Set<DocumentId> indexLookup(String term) {
		Set<DocumentId> targetDoc = new HashSet<>();
		for (Map.Entry<DocumentId, Map<String, Integer>> entry : ultTermsDocIds.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet())
			if (entry2.getKey().equalsIgnoreCase(term)) {
				targetDoc.add(entry.getKey());
			}
		}
		return targetDoc;
	}
	
	/**
	 * Returns the term frequency of a term in a particular document.
	 * 
	 * The term frequency is number of times the term appears in a document.
	 * 
	 * See 
	 * @param documentId
	 * @param term
	 * @return the term frequency of a term in a particular document
	 * @throws IllegalArgumentException if the documentId has not been added to the engine
	 */
	public int termFrequency(DocumentId documentId, String term) throws IllegalArgumentException {
		int frequency = 0;
		if (!ultTermsDocIds.keySet().contains(documentId)) {
			throw new IllegalArgumentException();
		}
		for (Map.Entry<DocumentId, Map<String, Integer>> entry : ultTermsDocIds.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet())
				if (entry.getKey().equals(documentId) && entry2.getKey().equalsIgnoreCase(term)) {
					frequency = entry2.getValue();
				}
		}
		return frequency;
	}
	
	/**
	 * Returns the inverse document frequency of a term across all documents in the index.
	 * 
	 * For our purposes, IDF is defined as log ((1 + N) / (1 + M)) where 
	 * N is the number of documents in total, and M
	 * is the number of documents where the term appears.
	 * 
	 * @param term
	 * @return the inverse document frequency of term 
	 */
	public double inverseDocumentFrequency(String term) {
		double totalDocs = ultTermsDocIds.size();
		double docCount = 0.0;
		double idf = 0.0;
		for (Map.Entry<DocumentId, Map<String, Integer>> entry : ultTermsDocIds.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
				if (entry2.getKey().equals(term)) {
					docCount++;
				}
			}
		}
		idf = Math.log((1 + totalDocs) / (1 + docCount));
		return idf;
	}
	
	/**
	 * Returns the tfidf score of a particular term for a particular document.
	 * 
	 * tfidf is the product of term frequency and inverse document frequency for the given term and document.
	 * 
	 * @param documentId
	 * @param term
	 * @return the tfidf of the the term/document
	 * @throws IllegalArgumentException if the documentId has not been added to the engine
	 */
	public double tfIdf(DocumentId documentId, String term) throws IllegalArgumentException {
		double tfIdfValue = 0.0;
		if (!ultTermsDocIds.keySet().contains(documentId)) {
			throw new IllegalArgumentException();
		}
		else {
			tfIdfValue = termFrequency(documentId, term) * inverseDocumentFrequency(term);
		}
		return tfIdfValue;
	}
	
	/**
	 * Returns a sorted list of documents, most relevant to least relevant, for the given term.
	 * 
	 * A document with a larger tfidf score is more relevant than a document with a lower tfidf score.
	 * 
	 * Each document in the returned list must contain the term.
	 * 
	 * @param term
	 * @return a list of documents sorted in descending order by tfidf
	 */
	public List<DocumentId> relevanceLookup(String term) {
		TfIdfComparator comparator = new TfIdfComparator(this, term);
		List<DocumentId> docList = new ArrayList<>();
		for (Map.Entry<DocumentId, Map<String, Integer>> entry : ultTermsDocIds.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
				if (entry2.getKey().equals(term)) {
					docList.add(entry.getKey());
				}
			}
		}
		Collections.sort(docList, comparator);
		return docList;
	}
}
