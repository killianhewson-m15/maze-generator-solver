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

            Timer timer = new Timer(30, event -> {
                generator.step();

                panel.setCurrentCell(generator.getCurrentCell());

                if (generator.isFinished())
                {
                    ((Timer) event.getSource()).stop();

                    MazeSolver solver = new MazeSolver(maze);
                    solver.solve();

                    panel.setSolutionPath(solver.getPath());

                    System.out.println(
                            "Maze solved: " + solver.isSolved());
                }
            });

            timer.start();
        });
    }
}