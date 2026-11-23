import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            Maze maze = new Maze(10, 10);
            MazeGenerator generator = new MazeGenerator(maze);

            MazePanel panel = new MazePanel(maze);
            panel.setCurrentCell(generator.getCurrentCell());

            JFrame frame = new JFrame("Maze Generator and Solver");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            Timer generationTimer = new Timer(30, event -> {
                generator.step();

                panel.setCurrentCell(generator.getCurrentCell());

                if (generator.isFinished())
                {
                    ((Timer) event.getSource()).stop();
                    startSolving(maze, panel);
                }
            });

            generationTimer.start();
        });
    }

    private static void startSolving(Maze maze, MazePanel panel)
    {
        MazeSolver solver = new MazeSolver(maze);

        panel.setCurrentCell(null);
        panel.setSolutionPath(new Cell[0]);
        panel.setShowVisited(true);

        Timer solverTimer = new Timer(30, event -> {
            solver.step();

            panel.setCurrentCell(solver.getCurrentCell());

            if (solver.isFinished())
            {
                ((Timer) event.getSource()).stop();

                panel.setCurrentCell(null);
                panel.setSolutionPath(solver.getPath());

                System.out.println(
                        "Maze solved: " + solver.isSolved());
            }
        });

        solverTimer.start();
    }
}