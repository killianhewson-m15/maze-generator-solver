public class Maze
{
    private final int rows;
    private final int columns;
    private final Cell[][] grid;

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

    public boolean isInBounds(int row, int column)
    {
        return row >= 0 && row < rows
                && column >= 0 && column < columns;
    }

    public Cell getCell(int row, int column)
    {
        if (!isInBounds(row, column))
        {
            return null;
        }

        return grid[row][column];
    }

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