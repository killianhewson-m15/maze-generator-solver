import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Generator and Solver");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new MazeAppPanel());
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}