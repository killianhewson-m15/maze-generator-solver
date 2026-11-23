# Maze Generator and Solver

Java Swing project for the Data Structures and Algorithms assignment.

The application generates a random maze and then finds a path from the top-left cell to the bottom-right cell.

## How to Run

Open the project in IntelliJ and run `src/maze/Main.java`.

Alternatively, with a JDK installed, run these commands from the project folder:

```shell
javac -d out -sourcepath src src/maze/Main.java src/maze/tests/MazeGeneratorTest.java src/maze/tests/MazeSolverTest.java src/maze/tests/QueueArrayBasedTest.java
java -cp out maze.Main
```

## Using the Application

- Enter the rows and columns, between 1 and 100.
- Click Generate to create a maze.
- Click Solve once generation has finished.
- Click Reset to clear the maze or stop an animation.
- Use the scrollbars to view larger mazes.

Orange shows the current cell, blue shows cells discovered during solving, and green shows the solution path.

## Implementation

The maze uses a two-dimensional array of cells. Each cell stores its position, four walls and whether it has been visited.

Generation uses randomized depth-first search with backtracking. The stack from the labs stores the current route and allows the generator to backtrack when it reaches a dead end.

Solving uses breadth-first search with the array-based queue from the labs. Predecessors are recorded so the solution path can be reconstructed when the exit is found.

The queue was adapted to accept a capacity based on the number of cells in the maze.

The interface uses Swing, with a timer to animate generation and solving.

### Class Structure and Coordinates

Cell stores the information for one cell, while Maze manages the grid and
wall removal. MazeGenerator and MazeSolver contain the algorithms.
MazePanel draws the maze, and MazeAppPanel handles the controls.

Grid coordinates are zero-based and stored as [row][column].
The top-left cell is (0, 0), and the bottom-right cell is
(rows - 1, columns - 1). Rows increase downwards and columns increase
to the right.

Core classes are in maze, interface classes in maze.ui, lab data
structures in maze.structures, and tests in maze.tests.

## Testing

The three test classes can be run individually in IntelliJ:

- MazeGeneratorTest
- MazeSolverTest
- QueueArrayBasedTest

From a terminal, after compiling:

```shell
java -cp out maze.tests.MazeGeneratorTest
java -cp out maze.tests.MazeSolverTest
java -cp out maze.tests.QueueArrayBasedTest
```

Generator and solver tests cover 1×1, 1×10, 10×1, 3×5, 10×10 and 30×30 mazes.

Checks include connected cells, consistent walls, valid solution paths and a blocked maze with no solution. Queue tests check FIFO order, wraparound, capacity limits and clearing the queue.

Manual checks also covered invalid input, scrolling, repeated solves and resetting during animations.