package maze.tests;

import maze.Cell;
import maze.Maze;
import maze.MazeGenerator;
import maze.structures.StackReferenceBased;

// Checks that generated mazes are connected and have consistent walls with no cycles.
public class MazeGeneratorTest
{
    // Repeats each size to check different random layouts and edge cases.
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

    // Checks visited cells, outer walls, shared walls, passages and connectivity.
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

        // Each passage is counted from both cells; a connected tree has cells - 1 edges.
        int passages = openSides / 2;

        check(passages == totalCells - 1,
                "Passage count should be cells - 1.");

        check(countReachableCells(maze) == totalCells,
                "Some cells cannot be reached through open passages.");
    }

    // Traverses open passages using a separate seen array to check connectivity.
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

    // Keeps wall order consistent with Maze.getNeighbours().
    private static boolean[] getWalls(Cell cell)
    {
        return new boolean[] {
                cell.hasTopWall(), cell.hasRightWall(),
                cell.hasBottomWall(), cell.hasLeftWall()
        };
    }

    // Stops the test immediately if a required condition fails.
    private static void check(boolean condition, String message)
    {
        if (!condition)
        {
            throw new IllegalStateException(message);
        }
    }
}