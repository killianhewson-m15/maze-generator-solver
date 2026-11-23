package maze;

// Stores the grid and manages the walls between cells.
public class Maze
{
    private final int rows;
    private final int columns;
    private final Cell[][] grid;

    // Creates a grid with all walls initially present.
    public Maze(int rows, int columns)
    {
        if (rows <= 0 || columns <= 0)
        {
            throw new IllegalArgumentException(
                    "Rows and columns must be greater than zero.");
        }

        this.rows = rows;
        this.columns = columns;
        grid = new Cell[rows][columns];

        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                grid[row][column] = new Cell(row, column);
            }
        }
    }

    public int getRows()
    {
        return rows;
    }

    public int getColumns()
    {
        return columns;
    }

    // Checks whether a position is inside the grid.
    public boolean isInBounds(int row, int column)
    {
        return row >= 0 && row < rows
                && column >= 0 && column < columns;
    }

    // Returns null for positions outside the maze.
    public Cell getCell(int row, int column)
    {
        if (!isInBounds(row, column))
        {
            return null;
        }

        return grid[row][column];
    }

    // Returns neighbours in top, right, bottom, left order; boundary entries are null.
    public Cell[] getNeighbours(Cell cell)
    {
        int row = cell.getRow();
        int column = cell.getColumn();

        return new Cell[] {
                getCell(row - 1, column),
                getCell(row, column + 1),
                getCell(row + 1, column),
                getCell(row, column - 1)
        };
    }

    // Opens a passage by removing the matching walls from two adjacent cells.
    public void removeWallBetween(Cell first, Cell second)
    {
        if (first == null || second == null)
        {
            throw new IllegalArgumentException("Both cells must exist.");
        }

        if (getCell(first.getRow(), first.getColumn()) != first
                || getCell(second.getRow(), second.getColumn()) != second)
        {
            throw new IllegalArgumentException(
                    "Both cells must belong to this maze.");
        }

        int rowDifference = second.getRow() - first.getRow();
        int columnDifference = second.getColumn() - first.getColumn();

        // Direct neighbours differ by one row or one column, but not both.
        if (Math.abs(rowDifference) + Math.abs(columnDifference) != 1)
        {
            throw new IllegalArgumentException(
                    "Cells must be directly adjacent.");
        }

        if (rowDifference == -1)
        {
            first.removeTopWall();
            second.removeBottomWall();
        }
        else if (rowDifference == 1)
        {
            first.removeBottomWall();
            second.removeTopWall();
        }
        else if (columnDifference == -1)
        {
            first.removeLeftWall();
            second.removeRightWall();
        }
        else
        {
            first.removeRightWall();
            second.removeLeftWall();
        }
    }
}