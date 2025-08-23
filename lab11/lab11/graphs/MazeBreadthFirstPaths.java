package lab11.graphs;

import java.util.LinkedList;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    //not sure why maze is not included above

    private int s;
    private int t;
    private Maze maze;
    private LinkedList<Integer> fringe;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        fringe = new LinkedList<>();
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    //explores all nodes closest to source first, then moves away. stops when target has been found
    //this means when target has been removed from the fringe
    private void bfs() {
        fringe.add(s);
        marked[s] = true;
        announce();

        while (!fringe.isEmpty()) {
            int v = fringe.remove();
            if (v == t) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    announce();
                    fringe.add(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

