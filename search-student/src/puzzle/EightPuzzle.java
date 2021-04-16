/*
 * Copyright 2017 Marc Liberatore.
 */

package puzzle;

import java.util.*;

import search.SearchProblem;
import search.Searcher;

/**
 * A class to represent an instance of the eight-puzzle.
 * 
 * The spaces in an 8-puzzle are indexed as follows:
 * 
 * 0 | 1 | 2
 * --+---+---
 * 3 | 4 | 5
 * --+---+---
 * 6 | 7 | 8
 * 
 * The puzzle contains the eight numbers 1-8, and an empty space.
 * If we represent the empty space as 0, then the puzzle is solved
 * when the values in the puzzle are as follows:
 * 
 * 1 | 2 | 3
 * --+---+---
 * 4 | 5 | 6
 * --+---+---
 * 7 | 8 | 0
 * 
 * That is, when the space at index 0 contains value 1, the space 
 * at index 1 contains value 2, and so on.
 * 
 * From any given state, you can swap the empty space with a space 
 * adjacent to it (that is, above, below, left, or right of it,
 * without wrapping around).
 * 
 * For example, if the empty space is at index 2, you may swap
 * it with the value at index 1 or 5, but not any other index.
 * 
 * Only half of all possible puzzle states are solvable! See:
 * https://en.wikipedia.org/wiki/15_puzzle
 * for details.
 * 

 * @author liberato
 *
 */
public class EightPuzzle implements SearchProblem<List<Integer>> {
	private final List<Integer> initialState;
	private final List<Integer> goalState = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 0);;
	private List<List<Integer>> successorsList;
	/**
	 * Creates a new instance of the 8 puzzle with the given starting values.
	 * 
	 * The values are indexed as described above, and should contain exactly the
	 * nine integers from 0 to 8.
	 * 
	 * @param startingValues
	 *            the starting values, 0 -- 8
	 * @throws IllegalArgumentException
	 *             if startingValues is invalid
	 */
	public EightPuzzle(List<Integer> startingValues) throws IllegalArgumentException {
		if (!startingValues.containsAll(goalState) || startingValues.size() > 9) {
			throw new IllegalArgumentException();
		}
		else {
			initialState = new ArrayList<>(startingValues);
			successorsList = new ArrayList<>();
		}
	}

	@Override
	public List<Integer> getInitialState() {
		// TODO
		return initialState;
	}

	@Override
	public List<List<Integer>> getSuccessors(List<Integer> currentState) {
		// TODO
		int emptySpace = currentState.indexOf(0);
		List<Integer> leftSuccessor = Arrays.asList(1, 2, 4, 5, 7, 8);
		List<Integer> rightSuccessor = Arrays.asList(0, 1, 3, 4, 6, 7);
		List<Integer> upperSuccessor = Arrays.asList(3, 4, 5, 6, 7, 8);
		List<Integer> belowSuccessor = Arrays.asList(0, 1, 2, 3, 4, 5);

		if (leftSuccessor.contains(emptySpace)) {
			List<Integer> copyList = new ArrayList<>(currentState);
			copyList.set(currentState.indexOf(0), currentState.get(currentState.indexOf(0) - 1));
			copyList.set(emptySpace - 1, 0);
			successorsList.add(copyList);
		}
		if (rightSuccessor.contains(emptySpace)) {
			List<Integer> copyList = new ArrayList<>(currentState);
			copyList.set(currentState.indexOf(0), currentState.get(currentState.indexOf(0) + 1));
			copyList.set(emptySpace + 1, 0);
			successorsList.add(copyList);
		}
		if (upperSuccessor.contains(emptySpace)) {
			List<Integer> copyList = new ArrayList<>(currentState);
			copyList.set(currentState.indexOf(0), currentState.get(currentState.indexOf(0) - 3));
			copyList.set(emptySpace - 3, 0);
			successorsList.add(copyList);
		}
		if (belowSuccessor.contains(emptySpace)) {
			List<Integer> copyList = new ArrayList<>(currentState);
			copyList.set(currentState.indexOf(0), currentState.get(currentState.indexOf(0) + 3));
			copyList.set(emptySpace + 3, 0);
			successorsList.add(copyList);
		}
		return successorsList;
	}

	@Override
	public boolean isGoal(List<Integer> state) {
		// TODO
		return goalState.equals(state);
	}

	public static void main(String[] args) {
		EightPuzzle eightPuzzle = new EightPuzzle(Arrays.asList(new Integer[] {1, 2, 3, 4, 0, 6, 7, 5, 8 }));

		List<List<Integer>> solution = new Searcher<List<Integer>>(eightPuzzle).findSolution();
		for (List<Integer> state : solution) {
			System.out.println(state);
		}
		System.out.println(solution.size() + " states in solution");
	}
}
