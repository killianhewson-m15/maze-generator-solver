import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class MazePanel extends JPanel
{
    private static final int CELL_SIZE = 30;
    private static final int PADDING = 20;

    private final Maze maze;

    public MazePanel(Maze maze)
    {
        this.maze = maze;
        setBackground(Color.WHITE);

        int width = maze.getColumns() * CELL_SIZE + 2 * PADDING;
        int height = maze.getRows() * CELL_SIZE + 2 * PADDING;

        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
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