package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;

public class Solver {

    private int minMoves;
    private ArrayList<WorldState> solution;

    public Solver(WorldState initial) {
        solution = new ArrayList<>();
        SearchNode initialNode = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> pq = new MinPQ<>(
                (a, b) -> Integer.compare(
                        a.movesFromInitial + a.worldState.estimatedDistanceToGoal(),
                        b.movesFromInitial + b.worldState.estimatedDistanceToGoal()
                )
        );
        pq.insert(initialNode);

        SearchNode goalNode = null;

        while (!pq.isEmpty()) {
            SearchNode current = pq.delMin();
            if (current.worldState.isGoal()) {
                goalNode = current;
                break;
            }

            for (WorldState neighbor : current.worldState.neighbors()) {
                if (current.previous != null && neighbor.equals(current.previous.worldState)) {
                    continue; // don't go back to previous state
                }
                pq.insert(new SearchNode(neighbor, current.movesFromInitial + 1, current));
            }
        }

        // reconstruct path
        while (goalNode != null) {
            solution.add(0, goalNode.worldState);
            goalNode = goalNode.previous;
        }

        minMoves = solution.size() - 1;
    }

    public int moves() {
        return minMoves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }

    private class SearchNode {
        WorldState worldState;
        int movesFromInitial;
        SearchNode previous;

        SearchNode(WorldState ws, int moves, SearchNode prev) {
            worldState = ws;
            movesFromInitial = moves;
            previous = prev;
        }
    }
}