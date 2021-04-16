/*
 * Copyright 2017 Marc Liberatore.
 */

package search;

import java.util.*;

/**
 * An implementation of a Searcher that performs an iterative search,
 * storing the list of next states in a Queue. This results in a
 * breadth-first search.
 * 
 * @author liberato
 *
 * @param <T> the type for each vertex in the search graph
 */
public class Searcher<T> {
	private final SearchProblem<T> searchProblem;
	
	/**
	 * Instantiates a searcher.
	 * 
	 * @param searchProblem
	 *            the search problem for which this searcher will find and
	 *            validate solutions
	 */
	public Searcher(SearchProblem<T> searchProblem) {
		this.searchProblem = searchProblem;
	}

	/**
	 * Finds and return a solution to the problem, consisting of a list of
	 * states.
	 * 
	 * The list should start with the initial state of the underlying problem.
	 * Then, it should have one or more additional states. Each state should be
	 * a successor of its predecessor. The last state should be a goal state of
	 * the underlying problem.
	 * 
	 * If there is no solution, then this method should return an empty list.
	 * 
	 * @return a solution to the problem (or an empty list)
	 */
	public List<T> findSolution() {		
		// TODO
		Queue<T> initialState = new LinkedList<>();
		initialState.add(searchProblem.getInitialState());

		Map<T, T> predState = new HashMap<>();
		predState.put(searchProblem.getInitialState(), null);
		
		List<T> solutionPath = new ArrayList<>();
		while (!initialState.isEmpty()) {
			T currStage = initialState.remove();
			if (searchProblem.isGoal(currStage)) {
				solutionPath.add(currStage);
				T previousState = predState.get(currStage);
				while (previousState != null) {
					solutionPath.add(0, previousState);
					previousState = predState.get(previousState);
				}
				break;
			}
			for (T nextState : searchProblem.getSuccessors(currStage)) {
				if (!predState.containsKey(nextState)) {
					initialState.add(nextState);
					predState.put(nextState, currStage);
				}
			}
		}
		return solutionPath;
	}

	/**
	 * Checks that a solution is valid.
	 * 
	 * A valid solution consists of a list of states. The list should start with
	 * the initial state of the underlying problem. Then, it should have one or
	 * more additional states. Each state should be a successor of its
	 * predecessor. The last state should be a goal state of the underlying
	 * problem.
	 * 
	 * @param solution
	 * @return true iff this solution is a valid solution
	 * @throws NullPointerException
	 *             if solution is null
	 */
	public final boolean isValidSolution(List<T> solution) throws NullPointerException {
		// TODO
		if (solution == null) {
			throw new NullPointerException();
		}
		if (solution.isEmpty()) {
			return false;
		}
		for (int i = 0; i < solution.size(); i++) {
			if (i == 0) {
				if (!searchProblem.getInitialState().equals(solution.get(0))) {
					return false;
				}
			}
			else {
				if (!searchProblem.getSuccessors(solution.get(i)).contains(solution.get(i - 1))) {
					return false;
				}
			}
		}
		return searchProblem.isGoal(solution.get(solution.size() - 1));
	}
}
