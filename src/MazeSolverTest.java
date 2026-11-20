public class MazeSolverTest
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
                new MazeGenerator(maze).generate();

                MazeSolver solver = new MazeSolver(maze);
                solver.solve();

                if (!solver.isFinished() || !solver.isSolved())
                {
                    throw new IllegalStateException(
                            "Generated maze was not solved.");
                }

                checkPath(maze, solver);
            }

            System.out.println("PASS: " + size[0] + " x "
                    + size[1] + " (10 runs)");
        }

        Maze blockedMaze = new Maze(2, 2);
        MazeSolver blockedSolver = new MazeSolver(blockedMaze);
        blockedSolver.solve();

        if (!blockedSolver.isFinished() || blockedSolver.isSolved())
        {
            throw new IllegalStateException(
                    "Solver should stop without crossing walls.");
        }

        if (blockedSolver.getPath().length != 0)
        {
            throw new IllegalStateException(
                    "Unreachable exit should have an empty path.");
        }

        System.out.println("PASS: unreachable exit");
        System.out.println("All search and path checks passed.");
    }

    private static void checkPath(Maze maze, MazeSolver solver)
    {
        Cell[] path = solver.getPath();

        if (path.length == 0
                || path[0] != maze.getCell(0, 0)
                || path[path.length - 1] != maze.getCell(
                maze.getRows() - 1, maze.getColumns() - 1))
        {
            throw new IllegalStateException("Incorrect path endpoints.");
        }

        boolean[][] seen = new boolean[maze.getRows()][maze.getColumns()];

        for (int i = 0; i < path.length; i++)
        {
            Cell current = path[i];

            if (seen[current.getRow()][current.getColumn()])
            {
                throw new IllegalStateException("Path repeats a cell.");
            }

            seen[current.getRow()][current.getColumn()] = true;

            if (i == 0)
            {
                continue;
            }

            Cell previous = path[i - 1];

            int rowDifference = current.getRow() - previous.getRow();
            int columnDifference = current.getColumn() - previous.getColumn();

            boolean connected =
                    (rowDifference == -1 && columnDifference == 0
                            && !previous.hasTopWall() && !current.hasBottomWall())
                            || (rowDifference == 1 && columnDifference == 0
                            && !previous.hasBottomWall() && !current.hasTopWall())
                            || (rowDifference == 0 && columnDifference == -1
                            && !previous.hasLeftWall() && !current.hasRightWall())
                            || (rowDifference == 0 && columnDifference == 1
                            && !previous.hasRightWall() && !current.hasLeftWall());

            if (!connected)
            {
                throw new IllegalStateException(
                        "Path crosses a wall or skips a cell.");
            }
        }
    }
}