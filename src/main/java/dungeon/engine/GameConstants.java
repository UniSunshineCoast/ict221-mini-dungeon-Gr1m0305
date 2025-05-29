package dungeon.engine;

/**
 * Contains game constants and configuration values.
 */
public class GameConstants {

    public static final int DEFAULT_MAX_STEPS = 100;
    public static final int DEFAULT_PLAYER_HP = 10;
    public static final int DEFAULT_SCORE = 0;

    public static final int TRAP_COUNT = 5;
    public static final int GOLD_COUNT = 5;
    public static final int MUTANT_COUNT = 3;
    public static final int HEALTH_POTION_COUNT = 2;

    public static final int MIN_DIFFICULTY = 0;
    public static final int MAX_DIFFICULTY = 10;

    public static final int ICON_SIZE = 20;

    public static final String ENTRY_COLOR = "-fx-background-color: rgb(0,42,255)";
    public static final String LADDER_COLOR = "-fx-background-color: rgb(255,111,0)";
    public static final String TRAP_COLOR = "-fx-background-color: rgb(255, 0, 0)";
    public static final String GOLD_COLOR = "-fx-background-color: rgb(255, 223, 0)";
    public static final String MUTANT_COLOR = "-fx-background-color: rgb(0, 200, 0)";
    public static final String HEALTH_POTION_COLOR = "-fx-background-color: rgb(255, 105, 180)";
    public static final String RANGER_COLOR = "-fx-background-color: rgb(0, 200, 0)";

    public static final String ENTRY_SYMBOL = "E";
    public static final String LADDER_SYMBOL = "L";
    public static final String TRAP_SYMBOL = "T";
    public static final String GOLD_SYMBOL = "G";
    public static final String MUTANT_SYMBOL = "M";
    public static final String HEALTH_POTION_SYMBOL = "H";
    public static final String RANGER_SYMBOL = "R";
    public static final String WALL_SYMBOL = "#";

    public static final String TRAP_ICON_PATH = "/trap_icon.png";
    
    private GameConstants() {
    }
}