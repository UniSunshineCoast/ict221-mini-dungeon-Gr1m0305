package dungeon.engine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.util.Random;
import java.util.Scanner;

public class GameEngine {

    private final Cell[][] map;
    private final int size;
    private final Random random = new Random();
    private int difficulty;

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
     *a
     * @param size the width and height.
     */
    public GameEngine(int size) {
        this.size = size;
        map = new Cell[size][size];

        // Get difficulty input from player
        getDifficultyFromPlayer();

        // Initialize grid with empty cells
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell();
                Text text = new Text(i + "," + j);
                text.setOpacity(0);
                cell.getChildren().add(text);
                map[i][j] = cell;
            }
        }

        // Entry point at bottom-left corner
        map[size-1][0].setStyle("-fx-background-color: rgb(0,42,255)");
        map[size-1][0].getChildren().add(new Text("E"));

        // Generate walls for a structured maze
        generateWalls();

        // Place Ladder at a random location
        placeRandomLadder();

        // Place 5 traps at random locations
        placeRandomTraps();

        // Place 5 gold pieces at random locations
        placeRandomGold();

        // Place 3 mutants at random locations
        placeRandomMutants();

        // Place 2 health potions at random positions
        placeRandomHealthPotions();

        // Place Rangers based on difficulty level
        placeRandomRangers();

    }

    /**
     * Gets difficulty input from the player (0-10).
     */
    private void getDifficultyFromPlayer() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Enter difficulty level (0-10): ");
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number (0-10): ");
                scanner.next();
            }
            difficulty = scanner.nextInt();
        } while (difficulty < 0 || difficulty > 10);

        System.out.println("Difficulty set to: " + difficulty);
    }

    /**
     * Places the ladder at a random position on the grid, avoiding walls.
     */
    private void placeRandomLadder() {
        int ladderX, ladderY;

        do {
            ladderX = random.nextInt(size);
            ladderY = random.nextInt(size);
        } while (
                ladderX == size-1 && ladderY == 0 ||
                        isWall(ladderX, ladderY)
        );

        map[ladderX][ladderY].setStyle("-fx-background-color: rgb(255,111,0)");
        map[ladderX][ladderY].getChildren().add(new Text("L"));
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
                map[trapX][trapY].setStyle("-fx-background-color: rgb(255, 0, 0)");

                try {
                    Image trapImage = new Image(getClass().getResourceAsStream("/trap_icon.png"));
                    ImageView trapIcon = new ImageView(trapImage);

                    trapIcon.setFitWidth(20);
                    trapIcon.setFitHeight(20);
                    trapIcon.setPreserveRatio(true);

                    map[trapX][trapY].getChildren().add(trapIcon);
                } catch (Exception e) {
                    System.err.println("Could not load trap_icon.png: " + e.getMessage());
                }

                trapsPlaced++;
            }
        }
    }

    /**
     * Places 5 gold pieces in random positions on the grid, avoiding walls, traps, ladder, and entry.
     */
    private void placeRandomGold() {
        int goldPlaced = 0;

        while (goldPlaced < 5) {
            int goldX = random.nextInt(size);
            int goldY = random.nextInt(size);

            // Ensure the gold doesn't spawn on entry, walls, traps, or ladder
            if (!isWall(goldX, goldY) && !isTrap(goldX, goldY) && !isLadder(goldX, goldY) && !isEntry(goldX, goldY)) {
                map[goldX][goldY].setStyle("-fx-background-color: rgb(255, 223, 0)");
                map[goldX][goldY].getChildren().add(new Text("G"));
                goldPlaced++;
            }
        }
    }

    /**
     * Places 3 mutants in random positions on the grid, avoiding walls, traps, ladder, gold, and entry.
     */
    private void placeRandomMutants() {
        int mutantsPlaced = 0;

        while (mutantsPlaced < 3) {
            int mutantX = random.nextInt(size);
            int mutantY = random.nextInt(size);

            // Ensure the mutant doesn't spawn on entry, walls, traps, ladder, or gold
            if (!isWall(mutantX, mutantY) && !isTrap(mutantX, mutantY) &&
                    !isLadder(mutantX, mutantY) && !isGold(mutantX, mutantY) &&
                    !isEntry(mutantX, mutantY)) {

                map[mutantX][mutantY].setStyle("-fx-background-color: rgb(0, 200, 0)");
                map[mutantX][mutantY].getChildren().add(new Text("M"));
                mutantsPlaced++;
            }
        }
    }

    /**
     * Places 2 health potions in random positions on the grid, avoiding all other objects.
     */
    private void placeRandomHealthPotions() {
        int potionsPlaced = 0;

        while (potionsPlaced < 2) {
            int potionX = random.nextInt(size);
            int potionY = random.nextInt(size);

            // Ensure health potions don't overlap any other object
            if (!isWall(potionX, potionY) && !isTrap(potionX, potionY) &&
                    !isLadder(potionX, potionY) && !isGold(potionX, potionY) &&
                    !isMutant(potionX, potionY) && !isEntry(potionX, potionY)) {

                map[potionX][potionY].setStyle("-fx-background-color: rgb(255, 105, 180)");
                map[potionX][potionY].getChildren().add(new Text("H"));
                potionsPlaced++;
            }
        }
    }

    /**
     * Places Rangers based on difficulty level in random positions on the grid, avoiding all other objects.
     */
    private void placeRandomRangers() {
        int rangersPlaced = 0;

        while (rangersPlaced < difficulty) {
            int rangerX = random.nextInt(size);
            int rangerY = random.nextInt(size);

            // Ensure Rangers don't overlap any other object
            if (!isWall(rangerX, rangerY) && !isTrap(rangerX, rangerY) &&
                    !isLadder(rangerX, rangerY) && !isGold(rangerX, rangerY) &&
                    !isMutant(rangerX, rangerY) && !isEntry(rangerX, rangerY) &&
                    !isHealthPotion(rangerX, rangerY) && !isRanger(rangerX, rangerY)) {

                map[rangerX][rangerY].setStyle("-fx-background-color: rgb(0, 200, 0)");
                map[rangerX][rangerY].getChildren().add(new Text("R"));
                rangersPlaced++;
            }
        }
    }

    /**
     * Checks if the given position contains a health potion.
     */
    private boolean isHealthPotion(int x, int y) {
        return containsSymbol(x, y, "H");
    }

    /**
     * Gets the current difficulty level.
     *
     * @return the difficulty level (0-10)
     */
    public int getDifficulty() {
        return difficulty;
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
     * Checks if the given position contains a trap.
     */
    private boolean isTrap(int x, int y) {
        return containsSymbol(x, y, "T");
    }

    /**
     * Checks if the given position contains the ladder.
     */
    private boolean isLadder(int x, int y) {
        return containsSymbol(x, y, "L");
    }

    /**
     * Checks if the given position contains the entry point.
     */
    private boolean isEntry(int x, int y) {
        return (x == size-1 && y == 0);
    }

    /**
     * Checks if the given position contains a gold.
     */
    private boolean isGold(int x, int y) {
        return containsSymbol(x, y, "G");
    }

    /**
     * Checks if the given position contains a melee mutant.
     */
    private boolean isMutant(int x, int y) {
        return containsSymbol(x, y, "M");
    }

    /**
     * Checks if the given position contains a ranger mutant.
     */
    private boolean isRanger(int x, int y) {
        return containsSymbol(x, y, "R");
    }

    /**
     * Method to check for a symbol at a given position.
     */
    private boolean containsSymbol(int x, int y, String symbol) {
        return map[x][y].getChildren().stream()
                .anyMatch(node -> node instanceof Text && ((Text) node).getText().equals(symbol));
    }

    /**
     * Generates walls to create a structured maze.
     */
    private void generateWalls() {
        for (int[] pos : wallPositions) {
            int x = pos[0];
            int y = pos[1];
            map[x][y].getChildren().add(new Text("#"));
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