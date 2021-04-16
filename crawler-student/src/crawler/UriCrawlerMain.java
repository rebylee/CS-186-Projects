package crawler;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import document.RetrievedDocument;

public class UriCrawlerMain {
    public static void main(String[] args) throws MaximumVisitsExceededException, NoUnvisitedUrisException {
        final String DEADLINK_CONTENTS = "dead link We're on a road to nowhere.";
	    final String DUPLICATE_CONTENTS = "duplicates I link to trivial.html twice.";
	    final String LINKED_TO_TRIVIAL_CONTENTS = "a file with a link I link to trivial.html.";
	    final String TRIVIAL_CONTENTS = "A trivial example This is a trivial example HTML file.";
	    final String RESOURCE_DIR = "test" + File.separator + "resources" + File.separator;
	    final URI DEADLINK_URI = new File(RESOURCE_DIR + "deadLink.html").getAbsoluteFile().toURI();
	    final URI DUPLICATE_URI = new File(RESOURCE_DIR + "duplicateLink.html").getAbsoluteFile().toURI();
	    final URI LINKED_TO_TRIVIAL_URI = new File(RESOURCE_DIR + "linkedToTrivial.html").getAbsoluteFile().toURI();
        final URI TRIVIAL_URI = new File(RESOURCE_DIR + "trivial.html").getAbsoluteFile().toURI();
        
        try {
            UriCrawler uriCrawler = new UriCrawler(2);
            URI uri = DEADLINK_URI;
            uriCrawler.addUri(uri);
            uriCrawler.visitOne();
            uriCrawler.visitOne();

            Set<RetrievedDocument> set = uriCrawler.getVisitedDocuments();
            //assertFalse(set.isEmpty());
            System.out.println("set is empty = " + set.isEmpty());
            
            List<RetrievedDocument> list = new ArrayList<>(set);
            RetrievedDocument document = list.get(0);
            System.out.println("uri = " + uri);
            System.out.println("document.uri = " + document.uri);
            System.out.println("dead link = " + DEADLINK_CONTENTS);
            System.out.println("document.text = " + document.text);
            System.out.println("document.links.isEmpty() = " + document.links.isEmpty());
            /*assertEquals(uri, document.uri);
            assertEquals(DEADLINK_CONTENTS, document.text);
            assertFalse(document.links.isEmpty());*/
        }
        catch (Exception e) {
            System.out.println("Exception" + e);
        }
        
        System.out.println("In main");
    }
}
