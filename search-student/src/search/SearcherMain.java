package search;

import java.util.*;
import mazes.Cell;
import mazes.Maze;
import mazes.MazeGenerator;

public class SearcherMain {
    public static void main(String[] args) {
        Maze maze = new MazeGenerator(3, 3, 2).generateDfs();
        List<Cell> solution = new ArrayList<Cell>();
		final Searcher<Cell> s = new Searcher<Cell>(maze);
		solution.add(new Cell(1, 0));
		solution.add(new Cell(0, 0));
		solution.add(new Cell(0, 1));
		solution.add(new Cell(0, 2));
		solution.add(new Cell(1, 2));
        s.isValidSolution(solution);
        System.out.println("In main method");
    }
}
