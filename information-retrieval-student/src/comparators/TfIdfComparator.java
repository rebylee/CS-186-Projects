/*
 * Copyright 2017 Marc Liberatore.
 */

package comparators;

import documents.DocumentId;
import index.SearchEngine;

import java.util.Comparator;

/**
 * Compare two documents in a search engine by tf-idf using a given term.
 * 
 * Using this comparator, the *larger* item should "come before" a smaller one so
 * that sort returns the list in descending (largest-to-smallest) order. 
 * 
 * It breaks ties by using the lexicographic ordering of the document IDs (that is, by using 
 * o1.id.compareTo(o2.id)).
 * 
 * @author liberato
 *
 */
public class TfIdfComparator implements Comparator<DocumentId> {
	private final SearchEngine searchEngine;
	private final String term;
	
	public TfIdfComparator(SearchEngine searchEngine, String term) {
		this.searchEngine = searchEngine;
		this.term = term;
	}
	
	@Override
	public int compare(DocumentId o1, DocumentId o2) {
		if (searchEngine.tfIdf(o1, term) == searchEngine.tfIdf(o2, term)) {
			return o1.id.compareTo(o2.id);
		}
		else {
			return -Double.compare(searchEngine.tfIdf(o1, term), searchEngine.tfIdf(o2, term));
		}
	}
}
