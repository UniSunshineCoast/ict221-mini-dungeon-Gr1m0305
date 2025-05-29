package dungeon.engine;

import javafx.scene.text.Text;
import java.util.Random;
import java.util.Scanner;

public class GameEngine {

    private final Cell[][] map;
    private final int size;
    private final Random random = new Random();
    private int difficulty;
    private int maxSteps = GameConstants.DEFAULT_MAX_STEPS;
    private int playerHP = GameConstants.DEFAULT_PLAYER_HP;
    private int score = GameConstants.DEFAULT_SCORE;
    private GameObjectPlacer objectPlacer;

    /**
     * Creates a square game board.
     *
     * @param size the width and height.
     */
    public GameEngine(int size) {
        this.size = size;
        map = new Cell[size][size];

        getDifficultyFromPlayer();

        initializeGrid();

        setupEntryPoint();

        objectPlacer = new GameObjectPlacer(map, size, random);

        MazeGenerator.generateWalls(map);

        placeGameObjects();
    }

    /**
     * Initializes the grid with empty cells.
     */
    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell();
                Text text = new Text(i + "," + j);
                text.setOpacity(0);
                cell.getChildren().add(text);
                map[i][j] = cell;
            }
        }
    }

    /**
     * Sets up the entry point at the bottom-left corner.
     */
    private void setupEntryPoint() {
        map[size-1][0].setStyle(GameConstants.ENTRY_COLOR);
        map[size-1][0].getChildren().add(new Text(GameConstants.ENTRY_SYMBOL));
    }

    /**
     * Places all game objects on the map.
     */
    private void placeGameObjects() {
        objectPlacer.placeRandomLadder();
        objectPlacer.placeRandomTraps();
        objectPlacer.placeRandomGold();
        objectPlacer.placeRandomMutants();
        objectPlacer.placeRandomHealthPotions();
        objectPlacer.placeRandomRangers(difficulty);
    }

    /**
     * Gets difficulty input from the player (0-10).
     */
    private void getDifficultyFromPlayer() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Enter difficulty level (" + GameConstants.MIN_DIFFICULTY + "-" + GameConstants.MAX_DIFFICULTY + "): ");
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number (" + GameConstants.MIN_DIFFICULTY + "-" + GameConstants.MAX_DIFFICULTY + "): ");
                scanner.next();
            }
            difficulty = scanner.nextInt();
        } while (difficulty < GameConstants.MIN_DIFFICULTY || difficulty > GameConstants.MAX_DIFFICULTY);

        System.out.println("Difficulty set to: " + difficulty);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public int getPlayerHP() {
        return playerHP;
    }

    public int getScore() {
        return score;
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public void setPlayerHP(int playerHP) {
        this.playerHP = playerHP;
    }

    public void setScore(int score) {
        this.score = score;
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