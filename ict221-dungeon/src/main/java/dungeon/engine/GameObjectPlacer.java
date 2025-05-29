package dungeon.engine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.util.Random;

/**
 * Handles placement of game objects on the map.
 */
public class GameObjectPlacer {

    private final Cell[][] map;
    private final int size;
    private final Random random;

    public GameObjectPlacer(Cell[][] map, int size, Random random) {
        this.map = map;
        this.size = size;
        this.random = random;
    }

    /**
     * Places the ladder at a random position on the grid, avoiding walls.
     */
    public void placeRandomLadder() {
        int ladderX, ladderY;

        do {
            ladderX = random.nextInt(size);
            ladderY = random.nextInt(size);
        } while (
                ladderX == size-1 && ladderY == 0 ||
                        MazeGenerator.isWall(ladderX, ladderY)
        );

        map[ladderX][ladderY].setStyle(GameConstants.LADDER_COLOR);
        map[ladderX][ladderY].getChildren().add(new Text(GameConstants.LADDER_SYMBOL));
    }

    /**
     * Places traps in random positions on the grid, avoiding walls and the ladder.
     */
    public void placeRandomTraps() {
        int trapsPlaced = 0;

        while (trapsPlaced < GameConstants.TRAP_COUNT) {
            int trapX = random.nextInt(size);
            int trapY = random.nextInt(size);

            if ((trapX != size - 1 || trapY != 0) && !MazeGenerator.isWall(trapX, trapY) && !isLadder(trapX, trapY)) {
                map[trapX][trapY].setStyle(GameConstants.TRAP_COLOR);

                try {
                    Image trapImage = new Image(getClass().getResourceAsStream(GameConstants.TRAP_ICON_PATH));
                    ImageView trapIcon = new ImageView(trapImage);

                    trapIcon.setFitWidth(GameConstants.ICON_SIZE);
                    trapIcon.setFitHeight(GameConstants.ICON_SIZE);
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
     * Places gold pieces in random positions on the grid, avoiding walls, traps, ladder, and entry.
     */
    public void placeRandomGold() {
        int goldPlaced = 0;

        while (goldPlaced < GameConstants.GOLD_COUNT) {
            int goldX = random.nextInt(size);
            int goldY = random.nextInt(size);

            if (!MazeGenerator.isWall(goldX, goldY) && !isTrap(goldX, goldY) && !isLadder(goldX, goldY) && !isEntry(goldX, goldY)) {
                map[goldX][goldY].setStyle(GameConstants.GOLD_COLOR);
                map[goldX][goldY].getChildren().add(new Text(GameConstants.GOLD_SYMBOL));
                goldPlaced++;
            }
        }
    }

    /**
     * Places mutants in random positions on the grid, avoiding walls, traps, ladder, gold, and entry.
     */
    public void placeRandomMutants() {
        int mutantsPlaced = 0;

        while (mutantsPlaced < GameConstants.MUTANT_COUNT) {
            int mutantX = random.nextInt(size);
            int mutantY = random.nextInt(size);

            if (!MazeGenerator.isWall(mutantX, mutantY) && !isTrap(mutantX, mutantY) &&
                    !isLadder(mutantX, mutantY) && !isGold(mutantX, mutantY) &&
                    !isEntry(mutantX, mutantY)) {

                map[mutantX][mutantY].setStyle(GameConstants.MUTANT_COLOR);
                map[mutantX][mutantY].getChildren().add(new Text(GameConstants.MUTANT_SYMBOL));
                mutantsPlaced++;
            }
        }
    }

    /**
     * Places health potions in random positions on the grid, avoiding all other objects.
     */
    public void placeRandomHealthPotions() {
        int potionsPlaced = 0;

        while (potionsPlaced < GameConstants.HEALTH_POTION_COUNT) {
            int potionX = random.nextInt(size);
            int potionY = random.nextInt(size);

            if (!MazeGenerator.isWall(potionX, potionY) && !isTrap(potionX, potionY) &&
                    !isLadder(potionX, potionY) && !isGold(potionX, potionY) &&
                    !isMutant(potionX, potionY) && !isEntry(potionX, potionY)) {

                map[potionX][potionY].setStyle(GameConstants.HEALTH_POTION_COLOR);
                map[potionX][potionY].getChildren().add(new Text(GameConstants.HEALTH_POTION_SYMBOL));
                potionsPlaced++;
            }
        }
    }

    /**
     * Places Rangers based on difficulty level in random positions on the grid, avoiding all other objects.
     */
    public void placeRandomRangers(int difficulty) {
        int rangersPlaced = 0;

        while (rangersPlaced < difficulty) {
            int rangerX = random.nextInt(size);
            int rangerY = random.nextInt(size);

            if (!MazeGenerator.isWall(rangerX, rangerY) && !isTrap(rangerX, rangerY) &&
                    !isLadder(rangerX, rangerY) && !isGold(rangerX, rangerY) &&
                    !isMutant(rangerX, rangerY) && !isEntry(rangerX, rangerY) &&
                    !isHealthPotion(rangerX, rangerY) && !isRanger(rangerX, rangerY)) {

                map[rangerX][rangerY].setStyle(GameConstants.RANGER_COLOR);
                map[rangerX][rangerY].getChildren().add(new Text(GameConstants.RANGER_SYMBOL));
                rangersPlaced++;
            }
        }
    }

    private boolean isTrap(int x, int y) {
        return map[x][y].getChildren().stream()
                .anyMatch(node -> node instanceof ImageView);
    }

    private boolean isLadder(int x, int y) {
        return containsSymbol(x, y, GameConstants.LADDER_SYMBOL);
    }

    private boolean isEntry(int x, int y) {
        return (x == size-1 && y == 0);
    }

    private boolean isGold(int x, int y) {
        return containsSymbol(x, y, GameConstants.GOLD_SYMBOL);
    }

    private boolean isMutant(int x, int y) {
        return containsSymbol(x, y, GameConstants.MUTANT_SYMBOL);
    }

    private boolean isRanger(int x, int y) {
        return containsSymbol(x, y, GameConstants.RANGER_SYMBOL);
    }

    private boolean isHealthPotion(int x, int y) {
        return containsSymbol(x, y, GameConstants.HEALTH_POTION_SYMBOL);
    }

    /**
     * Method to check for a symbol at a given position.
     */
    private boolean containsSymbol(int x, int y, String symbol) {
        return map[x][y].getChildren().stream()
                .anyMatch(node -> node instanceof Text && ((Text) node).getText().equals(symbol));
    }
}