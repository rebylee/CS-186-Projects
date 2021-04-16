/*
 * Copyright 2017 Marc Liberatore.
 */

package crawler;

import java.net.URI;
import java.util.*;

import org.jsoup.nodes.Document;

import document.RetrievedDocument;

/**
 * A simplified web crawler, specialized to crawl local URIs rather
 * than to retrieve remote documents.
 * 
 * @author liberato
 *
 */
public class UriCrawler {
	int visitQuota;
	int siteVisitCount;
	Set<URI> urisVisited;
	Set<URI> urisTotalVisit;
	Set<URI> urisToVisit;
	Set<RetrievedDocument> visitedDocs;

	/**
	 * Instantiates a new UriCrawler. The maximum number of documents a crawler
	 * will attempt to visit, ever, is limited to visitQuota.
	 * 
	 * @param visitQuota
	 *            the maximum number of documents a crawler will attempt to
	 *            visit
	 * @throws IllegalArgumentException
	 *             if maximumRetrievalAttempts is less than one
	 */
	public UriCrawler(int visitQuota) throws IllegalArgumentException {
		if (visitQuota < 1) {
			throw new IllegalArgumentException();
		}
		// TODO
		this.visitQuota = visitQuota;
		siteVisitCount = 0;
		urisVisited = new HashSet<>();
		urisTotalVisit = new HashSet<>();
		urisToVisit = new HashSet<>();
		visitedDocs = new HashSet<>();
	}

	/**
	 * Returns the set of URIs that this crawler has attempted to visit
	 * (successfully or not).
	 * 
	 * @return the set of URIs that this crawler has attempted to visit
	 */
	public Set<URI> getVistedUris() {
		// TODO
		return urisTotalVisit;
	}
	
	/**
	 * Returns the set of RetrievedDocuments corresponding to the URIs
	 * this crawler has successfully visited.
	 * 
	 * @return the set of RetrievedDocuments corresponding to the URIs
	 * this crawler has successfully visited
	 */
	public Set<RetrievedDocument> getVisitedDocuments() {
		// TODO
		return visitedDocs;
	}

	/**
	 * Adds a URI to the collections of URIs that this crawler should attempt to
	 * visit. Does not visit the URI.
	 * 
	 * @param uri
	 *            the URI to be visited (later!)
	 */
	public void addUri(URI uri) {
		// TODO
		urisToVisit.add(uri);
	}

	/**
	 * Attempts to visit a single as-yet unattempted URI in this crawler's
	 * collection of to-be-visited URIs.
	 * 
	 * Visiting a document entails parsing the text and links from the URI.
	 * 
	 * If the parse succeeds:
	 * 
	 * - The "file:" links should be added to this crawler's collection of
	 * to-be-visited URIs.
	 * 
	 * - A new RetrievedDocument should be added to this crawler's collection of
	 * successfully visited documents.
	 * 
	 * If the parse fails, this method considers the visit attempted but
	 * unsuccessful.
	 * 
	 * @throws MaximumVisitsExceededException
	 *             if this crawler has already attempted to visit its quota of
	 *             visits
	 * @throws NoUnvisitedUrisException
	 *             if no more unattempted URI remain in this crawler's
	 *             collection of URIs to visit
	 */
	public void visitOne() throws MaximumVisitsExceededException, NoUnvisitedUrisException {
		// TODO
		Document parsedDoc;
		
		siteVisitCount++;

		if (siteVisitCount > visitQuota) {
				throw new MaximumVisitsExceededException();
			}
		if (urisToVisit.isEmpty()) {
			throw new NoUnvisitedUrisException();
		}

		for (URI oneUri : urisToVisit) {
			parsedDoc = CrawlerUtils.parse(oneUri);
			if (parsedDoc != null) {
			urisToVisit.addAll(CrawlerUtils.getFileUriLinks(parsedDoc));
			RetrievedDocument ultCrawler = new RetrievedDocument(oneUri, parsedDoc.text(), CrawlerUtils.getFileUriLinks(parsedDoc));
			urisToVisit.remove(oneUri);
			urisVisited.add(oneUri);
			visitedDocs.add(ultCrawler);
			}
			urisTotalVisit.add(oneUri);
		}
		
	}

	/**
	 * Attempts to visit all URIs in this crawler (and any URIs they reference,
	 * and so on).
	 * 
	 * This method will not raise a MaximumVisitsExceededException if there are
	 * more URIs than can be visited. It will instead stop once the UriCrawler's
	 * quota has been reached.
	 */
	public void visitAll() {
		// TODO
		while(!urisToVisit.isEmpty() || siteVisitCount < visitQuota) {
			try {
				visitOne();
			}
			catch (Exception e) {
				System.out.println("No unvisited URIs left");
			}
		}
	}
}
