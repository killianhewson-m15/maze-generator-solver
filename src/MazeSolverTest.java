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

        System.out.println("PASS: unreachable exit");
        System.out.println("All search checks passed.");
    }
}