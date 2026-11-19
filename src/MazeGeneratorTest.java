public class MazeGeneratorTest
{
    public static void main(String[] args)
    {
        int[][] sizes = {
                {1, 1}, {1, 10}, {10, 1},
                {3, 5}, {10, 10}, {30, 30}
        };

        for (int[] size : sizes)
        {
            for (int run = 0; run < 10; run++)
            {
                Maze maze = new Maze(size[0], size[1]);
                MazeGenerator generator = new MazeGenerator(maze);
                generator.generate();

                checkMaze(maze);
            }

            System.out.println("PASS: " + size[0] + " x "
                    + size[1] + " (10 runs)");
        }

        System.out.println("All generation checks passed.");
    }

    private static void checkMaze(Maze maze)
    {
        int totalCells = maze.getRows() * maze.getColumns();
        int openSides = 0;

        for (int row = 0; row < maze.getRows(); row++)
        {
            for (int column = 0; column < maze.getColumns(); column++)
            {
                Cell cell = maze.getCell(row, column);

                check(cell.isVisited(),
                        "Not every cell was visited during generation.");

                Cell[] neighbours = maze.getNeighbours(cell);
                boolean[] walls = getWalls(cell);

                for (int direction = 0; direction < 4; direction++)
                {
                    Cell neighbour = neighbours[direction];

                    if (neighbour == null)
                    {
                        check(walls[direction],
                                "An outer boundary wall is missing.");
                    }
                    else
                    {
                        int opposite = (direction + 2) % 4;
                        boolean[] neighbourWalls = getWalls(neighbour);

                        check(walls[direction] == neighbourWalls[opposite],
                                "Neighbouring cells disagree about a wall.");

                        if (!walls[direction])
                        {
                            openSides++;
                        }
                    }
                }
            }
        }

        int passages = openSides / 2;

        check(passages == totalCells - 1,
                "Passage count should be cells - 1.");

        check(countReachableCells(maze) == totalCells,
                "Some cells cannot be reached through open passages.");
    }

    private static int countReachableCells(Maze maze)
    {
        boolean[][] seen = new boolean[maze.getRows()][maze.getColumns()];
        StackReferenceBased stack = new StackReferenceBased();

        stack.push(maze.getCell(0, 0));
        seen[0][0] = true;

        int count = 0;

        while (!stack.isEmpty())
        {
            Cell current = (Cell) stack.pop();
            count++;

            Cell[] neighbours = maze.getNeighbours(current);
            boolean[] walls = getWalls(current);

            for (int direction = 0; direction < 4; direction++)
            {
                Cell next = neighbours[direction];

                if (next != null && !walls[direction]
                        && !seen[next.getRow()][next.getColumn()])
                {
                    seen[next.getRow()][next.getColumn()] = true;
                    stack.push(next);
                }
            }
        }

        return count;
    }

    private static boolean[] getWalls(Cell cell)
    {
        return new boolean[] {
                cell.hasTopWall(), cell.hasRightWall(),
                cell.hasBottomWall(), cell.hasLeftWall()
        };
    }

    private static void check(boolean condition, String message)
    {
        if (!condition)
        {
            throw new IllegalStateException(message);
        }
    }
}