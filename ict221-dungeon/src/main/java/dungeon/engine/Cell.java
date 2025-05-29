package dungeon.engine;

import javafx.scene.layout.StackPane;

/**
 * Represents a single cell in the game grid.
 * Extends StackPane to allow stacking of visual elements.
 */
public class Cell extends StackPane {

    /**
     * Creates a new Cell with default styling.
     */
    public Cell() {
        super();
        setPrefSize(50, 50);
        setMinSize(50, 50);
        setMaxSize(50, 50);
        setStyle("-fx-border-color: black; -fx-border-width: 1;");
    }
}