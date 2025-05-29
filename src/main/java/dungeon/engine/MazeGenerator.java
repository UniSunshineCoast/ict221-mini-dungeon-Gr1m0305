package dungeon.engine;

import javafx.scene.text.Text;

/**
 * Handles maze generation and wall placement.
 */
public class MazeGenerator {
    
    /**
     * Predefined wall positions for the maze.
     */
    private static final int[][] WALL_POSITIONS = {
            {0, 0}, {0, 4}, {0, 6}, {1, 2}, {1, 8}, {2, 0},
            {2, 3}, {2, 5}, {2, 6}, {2, 7}, {3, 1}, {3, 3},
            {3, 6}, {4, 4}, {4, 8}, {5, 0}, {5, 1}, {5, 3},
            {5, 5}, {5, 6}, {6, 8}, {7, 0}, {7, 1}, {7, 3},
            {7, 5}, {7, 6}, {7, 8}, {8, 1}, {8, 3}, {8, 5},
            {8, 6}, {8, 8}, {8, 9}, {9, 4}
    };
    
    /**
     * Generates walls on the provided map.
     *
     * @param map the game map to place walls on
     */
    public static void generateWalls(Cell[][] map) {
        for (int[] pos : WALL_POSITIONS) {
            int x = pos[0];
            int y = pos[1];
            map[x][y].getChildren().add(new Text(GameConstants.WALL_SYMBOL));
        }
    }
    
    /**
     * Checks if the given position contains a wall.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the position contains a wall
     */
    public static boolean isWall(int x, int y) {
        for (int[] pos : WALL_POSITIONS) {
            if (pos[0] == x && pos[1] == y) {
                return true;
            }
        }
        return false;
    }
}