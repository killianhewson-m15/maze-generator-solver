package maze;

import maze.structures.StackReferenceBased;
import java.util.Random;

// Generates a maze using randomized depth-first search and a backtracking stack.
public class MazeGenerator
{
    private final Maze maze;
    private final StackReferenceBased stack;
    private final Random random;

    // Starts generation at the top-left cell of a fresh maze.
    public MazeGenerator(Maze maze)
    {
        this.maze = maze;
        stack = new StackReferenceBased();
        random = new Random();

        Cell start = maze.getCell(0, 0);
        start.setVisited(true);
        stack.push(start);
    }

    public boolean isFinished()
    {
        return stack.isEmpty();
    }

    // Visits one unvisited neighbour or backtracks when none remain.
    public void step()
    {
        if (isFinished())
        {
            return;
        }

        Cell current = (Cell) stack.peek();
        Cell[] unvisitedNeighbours = new Cell[4];
        int count = 0;

        // Collect valid neighbours that have not been visited yet.
        for (Cell neighbour : maze.getNeighbours(current))
        {
            if (neighbour != null && !neighbour.isVisited())
            {
                unvisitedNeighbours[count] = neighbour;
                count++;
            }
        }

        if (count > 0)
        {
            // Choose randomly from the available unvisited neighbours.
            int index = random.nextInt(count);
            Cell next = unvisitedNeighbours[index];

            maze.removeWallBetween(current, next);
            next.setVisited(true);
            stack.push(next);
        }
        else
        {
            // Remove the dead end so the previous cell becomes current.
            stack.pop();
        }
    }

    // Runs generation to completion without animation, for example in tests.
    public void generate()
    {
        while (!isFinished())
        {
            step();
        }
    }

    // The top of the stack is the cell highlighted during generation.
    public Cell getCurrentCell()
    {
        if (isFinished())
        {
            return null;
        }

        return (Cell) stack.peek();
    }
}