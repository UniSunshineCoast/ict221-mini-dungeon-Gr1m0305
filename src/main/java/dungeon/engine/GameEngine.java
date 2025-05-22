package dungeon.engine;

import javafx.scene.text.Text;
import java.util.Random;

public class GameEngine {

    private Cell[][] map;
    private int size;
    private Random random = new Random();

    /**
     * Predefined wall positions for the maze.
     */
    private final int[][] wallPositions = {
            {0, 0}, {0, 4}, {0, 6}, {1, 2}, {1, 8}, {2, 0},
            {2, 3}, {2, 5}, {2, 6}, {2, 7}, {3, 1}, {3, 3},
            {3, 6}, {4, 4}, {4, 8}, {5, 0}, {5, 1}, {5, 3},
            {5, 5}, {5, 6}, {6, 8}, {7, 0}, {7, 1}, {7, 3},
            {7, 5}, {7, 6}, {7, 8}, {8, 1}, {8, 3}, {8, 5},
            {8, 6}, {8, 8}, {8, 9}, {9, 4}
    };

    /**
     * Creates a square game board.
     *
     * @param size the width and height.
     */
    public GameEngine(int size) {
        this.size = size;
        map = new Cell[size][size];

        // Initialize grid with empty cells
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell();
                Text text = new Text(i + "," + j);
                text.setOpacity(0); // Hide coordinates
                cell.getChildren().add(text);
                map[i][j] = cell;
            }
        }

        // Entry point at bottom-left corner
        map[size-1][0].setStyle("-fx-background-color: rgb(0,42,255)");
        map[size-1][0].getChildren().add(new Text("E"));

        // Generate walls for a structured maze
        generateWalls();

        // Place Ladder at a random location (ensuring it doesn't overlap a wall)
        placeRandomLadder();

        // Place 5 traps at random locations (avoiding walls and the ladder)
        placeRandomTraps();
    }

    /**
     * Places the ladder at a random position on the grid, avoiding walls and traps.
     */
    private void placeRandomLadder() {
        int ladderX, ladderY;

        do {
            ladderX = random.nextInt(size);
            ladderY = random.nextInt(size);
        } while (
                ladderX == size-1 && ladderY == 0 || // Avoid Entry
                        isWall(ladderX, ladderY) ||          // Avoid Walls
                        isTrap(ladderX, ladderY)             // Avoid Traps
        );

        map[ladderX][ladderY].setStyle("-fx-background-color: rgb(255,111,0)"); // Ladder color
        map[ladderX][ladderY].getChildren().add(new Text("L")); // Ladder symbol
    }

    /**
     * Checks if the given position contains a trap.
     */
    private boolean isTrap(int x, int y) {
        return map[x][y].getChildren().stream()
                .anyMatch(node -> node instanceof Text && ((Text) node).getText().equals("T"));
    }

    /**
     * Places 5 traps in random positions on the grid, avoiding walls and the ladder.
     */
    private void placeRandomTraps() {
        int trapsPlaced = 0;

        while (trapsPlaced < 5) {
            int trapX = random.nextInt(size);
            int trapY = random.nextInt(size);

            // Ensure the trap doesn't spawn on entry, walls, or ladder
            if ((trapX != size - 1 || trapY != 0) && !isWall(trapX, trapY) && !isLadder(trapX, trapY)) {
                map[trapX][trapY].setStyle("-fx-background-color: rgb(255, 0, 0)"); // Red color
                map[trapX][trapY].getChildren().add(new Text("T")); // Trap symbol
                trapsPlaced++;
            }
        }
    }

    /**
     * Checks if the given position contains a wall.
     */
    private boolean isWall(int x, int y) {
        for (int[] pos : wallPositions) {
            if (pos[0] == x && pos[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given position contains the ladder.
     */
    private boolean isLadder(int x, int y) {
        return map[x][y].getChildren().stream()
                .anyMatch(node -> node instanceof Text && ((Text) node).getText().equals("L"));
    }

    /**
     * Generates walls to create a structured maze.
     */
    private void generateWalls() {
        for (int[] pos : wallPositions) {
            int x = pos[0];
            int y = pos[1];
            map[x][y].getChildren().add(new Text("#")); // Wall symbol
        }
    }

    /**
     * The size of the current game.
     *
     * @return this is both the width and the height.
     */
    public int getSize() {
        return map.length;
    }

    /**
     * The map of the current game.
     *
     * @return the map, which is a 2D array.
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Plays a text-based game.
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("The size of the map is %d * %d\n", engine.getSize(), engine.getSize());
    }
}
