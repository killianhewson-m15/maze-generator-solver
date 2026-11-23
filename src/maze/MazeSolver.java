package maze;

import maze.structures.QueueArrayBased;
import maze.structures.StackReferenceBased;

// Uses breadth-first search to find a shortest route from start to exit.
public class MazeSolver
{
    private final Maze maze;
    private final QueueArrayBased queue;
    private final Cell[][] predecessors;
    private final Cell exit;

    private Cell currentCell;
    private boolean finished = false;
    private boolean solved = false;

    // Clears the visited state and adds the start cell to the search queue.
    public MazeSolver(Maze maze)
    {
        this.maze = maze;

        // Each cell enters the queue at most once, so this capacity is sufficient.
        queue = new QueueArrayBased(
                maze.getRows() * maze.getColumns());

        predecessors = new Cell[maze.getRows()][maze.getColumns()];

        exit = maze.getCell(
                maze.getRows() - 1, maze.getColumns() - 1);

        // Reset the visited flags left by maze generation.
        for (int row = 0; row < maze.getRows(); row++)
        {
            for (int column = 0; column < maze.getColumns(); column++)
            {
                maze.getCell(row, column).setVisited(false);
            }
        }

        Cell start = maze.getCell(0, 0);
        start.setVisited(true);
        queue.enqueue(start);
    }

    public boolean isFinished()
    {
        return finished;
    }

    public boolean isSolved()
    {
        return solved;
    }

    public Cell getCurrentCell()
    {
        return currentCell;
    }

    public Cell getPredecessor(Cell cell)
    {
        return predecessors[cell.getRow()][cell.getColumn()];
    }

    // Processes one queued cell and discovers neighbours through open passages.
    public void step()
    {
        if (isFinished())
        {
            return;
        }

        currentCell = (Cell) queue.dequeue();
        Cell current = currentCell;

        if (current == exit)
        {
            solved = true;
            finished = true;
            return;
        }

        Cell[] neighbours = maze.getNeighbours(current);

        // Wall order matches the neighbour order: top, right, bottom, left.
        boolean[] walls = {
                current.hasTopWall(), current.hasRightWall(),
                current.hasBottomWall(), current.hasLeftWall()
        };

        for (int direction = 0; direction < 4; direction++)
        {
            Cell next = neighbours[direction];

            if (next != null && !walls[direction] && !next.isVisited())
            {
                // Mark on enqueue to avoid duplicates and remember the first route here.
                next.setVisited(true);

                predecessors[next.getRow()][next.getColumn()] = current;

                queue.enqueue(next);
            }
        }

        // An empty queue means there are no more reachable cells to search.
        if (queue.isEmpty())
        {
            finished = true;
        }
    }

    // Runs the search to completion without animation.
    public void solve()
    {
        while (!isFinished())
        {
            step();
        }
    }

    // Returns the solution in start-to-exit order, or an empty array if unsolved.
    public Cell[] getPath()
    {
        if (!isSolved())
        {
            return new Cell[0];
        }

        StackReferenceBased pathStack = new StackReferenceBased();
        Cell current = exit;
        int length = 0;

        // Follow predecessor links from the exit back to the start.
        while (current != null)
        {
            pathStack.push(current);
            length++;
            current = getPredecessor(current);
        }

        Cell[] path = new Cell[length];

        // Reverse the order so the path runs from start to exit.
        for (int i = 0; i < length; i++)
        {
            path[i] = (Cell) pathStack.pop();
        }

        return path;
    }
}