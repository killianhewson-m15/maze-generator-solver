import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;

public class MazeAppPanel extends JPanel
{
    private final JButton generateButton = new JButton("Generate");
    private final JButton solveButton = new JButton("Solve");
    private final JButton resetButton = new JButton("Reset");
    private final JLabel statusLabel = new JLabel();

    private Maze maze;
    private MazePanel mazePanel;
    private Timer timer;
    private boolean mazeGenerated = false;

    public MazeAppPanel()
    {
        setLayout(new BorderLayout());

        JPanel controls = new JPanel();
        controls.add(generateButton);
        controls.add(solveButton);
        controls.add(resetButton);

        add(controls, BorderLayout.NORTH);

        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(statusLabel, BorderLayout.SOUTH);

        generateButton.addActionListener(event -> generateMaze());
        solveButton.addActionListener(event -> solveMaze());
        resetButton.addActionListener(event -> resetMaze());

        resetMaze();
    }

    private void generateMaze()
    {
        resetMaze();

        MazeGenerator generator = new MazeGenerator(maze);
        mazePanel.setCurrentCell(generator.getCurrentCell());

        setBusy(true);
        statusLabel.setText("Generating maze...");

        timer = new Timer(30, event -> {
            if (event.getSource() != timer)
            {
                return;
            }

            generator.step();
            mazePanel.setCurrentCell(generator.getCurrentCell());

            if (generator.isFinished())
            {
                stopTimer();
                mazePanel.setCurrentCell(null);

                mazeGenerated = true;
                setBusy(false);

                statusLabel.setText(
                        "Generation complete. Click Solve.");
            }
        });

        timer.start();
    }

    private void solveMaze()
    {
        if (!mazeGenerated)
        {
            return;
        }

        MazeSolver solver = new MazeSolver(maze);

        mazePanel.setCurrentCell(null);
        mazePanel.setSolutionPath(new Cell[0]);
        mazePanel.setShowVisited(true);

        setBusy(true);
        statusLabel.setText("Searching for the exit...");

        timer = new Timer(30, event -> {
            if (event.getSource() != timer)
            {
                return;
            }

            solver.step();
            mazePanel.setCurrentCell(solver.getCurrentCell());

            if (solver.isFinished())
            {
                stopTimer();
                mazePanel.setCurrentCell(null);

                Cell[] path = solver.getPath();
                mazePanel.setSolutionPath(path);

                setBusy(false);

                if (solver.isSolved())
                {
                    statusLabel.setText(
                            "Solved: " + (path.length - 1) + " moves.");
                }
                else
                {
                    statusLabel.setText("No route to the exit.");
                }
            }
        });

        timer.start();
    }

    private void resetMaze()
    {
        stopTimer();

        if (mazePanel != null)
        {
            remove(mazePanel);
        }

        maze = new Maze(10, 10);
        mazePanel = new MazePanel(maze);

        add(mazePanel, BorderLayout.CENTER);

        mazeGenerated = false;
        setBusy(false);

        statusLabel.setText("Click Generate to create a maze.");

        revalidate();
        repaint();
    }

    private void setBusy(boolean busy)
    {
        generateButton.setEnabled(!busy);
        solveButton.setEnabled(!busy && mazeGenerated);
    }

    private void stopTimer()
    {
        if (timer != null)
        {
            timer.stop();
            timer = null;
        }
    }
}