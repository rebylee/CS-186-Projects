/*
 * Copyright 2017 Marc Liberatore.
 */

package graphs;

public interface UnweightedGraphInterface<V> extends GraphInterface<V> {
	void addEdge(V fromVertex, V toVertex);
}
