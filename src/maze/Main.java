package maze;

import maze.ui.MazeAppPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("maze.Maze Generator and Solver");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new MazeAppPanel());

            frame.setMinimumSize(new Dimension(550, 400));
            frame.pack();
            frame.setResizable(true);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}