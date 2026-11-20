import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            Maze maze = new Maze(10, 10);

            MazeGenerator generator = new MazeGenerator(maze);
            generator.generate();

            MazeSolver solver = new MazeSolver(maze);
            solver.solve();

            System.out.println("Maze solved: " + solver.isSolved());

            MazePanel panel = new MazePanel(maze);
            panel.setSolutionPath(solver.getPath());

            JFrame frame = new JFrame("Maze Generator and Solver");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}