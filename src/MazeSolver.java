public class MazeSolver
{
    private final Maze maze;
    private final QueueArrayBased queue;
    private final Cell[][] predecessors;
    private final Cell exit;

    private boolean finished = false;
    private boolean solved = false;

    public MazeSolver(Maze maze)
    {
        this.maze = maze;

        queue = new QueueArrayBased(
                maze.getRows() * maze.getColumns());

        predecessors = new Cell[maze.getRows()][maze.getColumns()];

        exit = maze.getCell(
                maze.getRows() - 1, maze.getColumns() - 1);

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

    public Cell getPredecessor(Cell cell)
    {
        return predecessors[cell.getRow()][cell.getColumn()];
    }

    public void step()
    {
        if (isFinished())
        {
            return;
        }

        Cell current = (Cell) queue.dequeue();

        if (current == exit)
        {
            solved = true;
            finished = true;
            return;
        }

        Cell[] neighbours = maze.getNeighbours(current);

        boolean[] walls = {
                current.hasTopWall(), current.hasRightWall(),
                current.hasBottomWall(), current.hasLeftWall()
        };

        for (int direction = 0; direction < 4; direction++)
        {
            Cell next = neighbours[direction];

            if (next != null && !walls[direction] && !next.isVisited())
            {
                next.setVisited(true);

                predecessors[next.getRow()][next.getColumn()] = current;

                queue.enqueue(next);
            }
        }

        if (queue.isEmpty())
        {
            finished = true;
        }
    }

    public void solve()
    {
        while (!isFinished())
        {
            step();
        }
    }

    public Cell[] getPath()
    {
        if (!isSolved())
        {
            return new Cell[0];
        }

        StackReferenceBased pathStack = new StackReferenceBased();
        Cell current = exit;
        int length = 0;

        while (current != null)
        {
            pathStack.push(current);
            length++;
            current = getPredecessor(current);
        }

        Cell[] path = new Cell[length];

        for (int i = 0; i < length; i++)
        {
            path[i] = (Cell) pathStack.pop();
        }

        return path;
    }
}