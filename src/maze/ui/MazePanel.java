package maze.ui;

import maze.Cell;
import maze.Maze;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

// Draws the maze walls and the current search or solution state.
public class MazePanel extends JPanel
{
    private static final int CELL_SIZE = 30;
    private static final int PADDING = 20;

    private final Maze maze;
    private Cell[] solutionPath = new Cell[0];
    private Cell currentCell;
    private boolean showVisited = false;

    // Uses fixed-size cells; the surrounding scroll pane handles larger mazes.
    public MazePanel(Maze maze)
    {
        this.maze = maze;
        setBackground(Color.WHITE);

        int width = maze.getColumns() * CELL_SIZE + 2 * PADDING;
        int height = maze.getRows() * CELL_SIZE + 2 * PADDING;

        setPreferredSize(new Dimension(width, height));
    }

    public void setSolutionPath(Cell[] solutionPath)
    {
        this.solutionPath = solutionPath;
        repaint();
    }

    public void setCurrentCell(Cell currentCell)
    {
        this.currentCell = currentCell;
        repaint();
    }

    public void setShowVisited(boolean showVisited)
    {
        this.showVisited = showVisited;
        repaint();
    }

    // Draws cell colours first, then walls so the grid stays visible.
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        // Show cells discovered during BFS.
        if (showVisited)
        {
            g.setColor(new Color(173, 216, 230));

            for (int row = 0; row < maze.getRows(); row++)
            {
                for (int column = 0; column < maze.getColumns(); column++)
                {
                    Cell cell = maze.getCell(row, column);

                    if (cell.isVisited())
                    {
                        int x = PADDING + column * CELL_SIZE;
                        int y = PADDING + row * CELL_SIZE;

                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }

        // Draw the final solution over the discovered cells.
        g.setColor(new Color(144, 238, 144));

        for (Cell cell : solutionPath)
        {
            int x = PADDING + cell.getColumn() * CELL_SIZE;
            int y = PADDING + cell.getRow() * CELL_SIZE;

            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // Highlight the current cell.
        if (currentCell != null)
        {
            g.setColor(new Color(255, 200, 100));

            int x = PADDING + currentCell.getColumn() * CELL_SIZE;
            int y = PADDING + currentCell.getRow() * CELL_SIZE;

            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // Keep the walls visible over every colour.
        g.setColor(Color.BLACK);

        for (int row = 0; row < maze.getRows(); row++)
        {
            for (int column = 0; column < maze.getColumns(); column++)
            {
                Cell cell = maze.getCell(row, column);

                int x = PADDING + column * CELL_SIZE;
                int y = PADDING + row * CELL_SIZE;

                if (cell.hasTopWall())
                {
                    g.drawLine(x, y, x + CELL_SIZE, y);
                }

                if (cell.hasRightWall())
                {
                    g.drawLine(x + CELL_SIZE, y,
                            x + CELL_SIZE, y + CELL_SIZE);
                }

                if (cell.hasBottomWall())
                {
                    g.drawLine(x, y + CELL_SIZE,
                            x + CELL_SIZE, y + CELL_SIZE);
                }

                if (cell.hasLeftWall())
                {
                    g.drawLine(x, y, x, y + CELL_SIZE);
                }
            }
        }
    }
}