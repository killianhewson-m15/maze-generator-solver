package maze.ui;

import maze.Cell;
import maze.Maze;
import maze.MazeGenerator;
import maze.MazeSolver;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

// Handles the controls, dimension input and animated algorithm steps.
public class MazeAppPanel extends JPanel
{
    private static final int MAX_DIMENSION = 100;

    private final JTextField rowsField = new JTextField("10", 3);
    private final JTextField columnsField = new JTextField("10", 3);

    private final JButton generateButton = new JButton("Generate");
    private final JButton solveButton = new JButton("Solve");
    private final JButton resetButton = new JButton("Reset");

    private final JLabel statusLabel = new JLabel();
    private final JScrollPane scrollPane = new JScrollPane();

    private Maze maze;
    private MazePanel mazePanel;
    private Timer timer;
    private boolean mazeGenerated = false;

    // Builds the controls, scrollable maze area and status message.
    public MazeAppPanel()
    {
        setLayout(new BorderLayout());

        JPanel controls = new JPanel();

        controls.add(new JLabel("Rows:"));
        controls.add(rowsField);
        controls.add(new JLabel("Columns:"));
        controls.add(columnsField);

        controls.add(generateButton);
        controls.add(solveButton);
        controls.add(resetButton);

        add(controls, BorderLayout.NORTH);

        scrollPane.setPreferredSize(new Dimension(650, 450));
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(30);

        add(scrollPane, BorderLayout.CENTER);

        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(statusLabel, BorderLayout.SOUTH);

        generateButton.addActionListener(event -> generateMaze());
        solveButton.addActionListener(event -> solveMaze());
        resetButton.addActionListener(event -> resetMaze());

        resetMaze();
    }

    // Creates a fresh maze and advances generation once per timer event.
    private void generateMaze()
    {
        if (!resetMaze())
        {
            return;
        }

        MazeGenerator generator = new MazeGenerator(maze);
        mazePanel.setCurrentCell(generator.getCurrentCell());

        setBusy(true);
        statusLabel.setText("Generating maze...");

        timer = new Timer(30, event -> {
            // Ignore any queued event from an old timer after a reset.
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

    // Animates BFS, then displays the solution and its number of moves.
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
            // Ignore any queued event from an old timer after a reset.
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

    // Validates dimensions before replacing the maze; returns false for invalid input.
    private boolean resetMaze()
    {
        int rows;
        int columns;

        try
        {
            rows = Integer.parseInt(rowsField.getText().trim());
            columns = Integer.parseInt(columnsField.getText().trim());
        }
        catch (NumberFormatException exception)
        {
            statusLabel.setText(
                    "Enter whole numbers for rows and columns.");

            return false;
        }

        if (rows < 1 || rows > MAX_DIMENSION
                || columns < 1 || columns > MAX_DIMENSION)
        {
            statusLabel.setText(
                    "Rows and columns must be between 1 and "
                            + MAX_DIMENSION + ".");

            return false;
        }

        // Validation passed, so the old animation and maze can now be replaced.
        stopTimer();

        maze = new Maze(rows, columns);
        mazePanel = new MazePanel(maze);

        scrollPane.setViewportView(mazePanel);
        scrollPane.getViewport().setViewPosition(new Point(0, 0));

        mazeGenerated = false;
        setBusy(false);

        statusLabel.setText(
                "Ready: " + rows + " rows x " + columns
                        + " columns. Click Generate.");

        revalidate();
        repaint();

        return true;
    }

    // Prevents conflicting actions during animation; Reset remains available.
    private void setBusy(boolean busy)
    {
        generateButton.setEnabled(!busy);
        solveButton.setEnabled(!busy && mazeGenerated);

        rowsField.setEnabled(!busy);
        columnsField.setEnabled(!busy);
    }

    // Stops the current animation and clears the active timer reference.
    private void stopTimer()
    {
        if (timer != null)
        {
            timer.stop();
            timer = null;
        }
    }
}