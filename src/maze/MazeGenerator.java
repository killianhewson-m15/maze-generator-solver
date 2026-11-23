package maze;

import maze.structures.StackReferenceBased;
import java.util.Random;

public class MazeGenerator
{
    private final Maze maze;
    private final StackReferenceBased stack;
    private final Random random;

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

    public void step()
    {
        if (isFinished())
        {
            return;
        }

        Cell current = (Cell) stack.peek();
        Cell[] unvisitedNeighbours = new Cell[4];
        int count = 0;

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
            int index = random.nextInt(count);
            Cell next = unvisitedNeighbours[index];

            maze.removeWallBetween(current, next);
            next.setVisited(true);
            stack.push(next);
        }
        else
        {
            stack.pop();
        }
    }

    public void generate()
    {
        while (!isFinished())
        {
            step();
        }
    }

    public Cell getCurrentCell()
    {
        if (isFinished())
        {
            return null;
        }

        return (Cell) stack.peek();
    }
}