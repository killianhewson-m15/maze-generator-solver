package maze;

public class Cell
{
    private final int row;
    private final int column;

    private boolean topWall = true;
    private boolean bottomWall = true;
    private boolean leftWall = true;
    private boolean rightWall = true;

    private boolean visited = false;

    public Cell(int row, int column)
    {
        this.row = row;
        this.column = column;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public boolean hasTopWall()
    {
        return topWall;
    }

    public boolean hasBottomWall()
    {
        return bottomWall;
    }

    public boolean hasLeftWall()
    {
        return leftWall;
    }

    public boolean hasRightWall()
    {
        return rightWall;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    public void removeTopWall()
    {
        topWall = false;
    }

    public void removeBottomWall()
    {
        bottomWall = false;
    }

    public void removeLeftWall()
    {
        leftWall = false;
    }

    public void removeRightWall()
    {
        rightWall = false;
    }
}