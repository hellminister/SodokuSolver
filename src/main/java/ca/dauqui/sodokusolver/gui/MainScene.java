package ca.dauqui.sodokusolver.gui;

import ca.dauqui.sodokusolver.game.Grid;
import ca.dauqui.sodokusolver.game.solvers.Solver;
import ca.dauqui.sodokusolver.game.solvers.Solvers;
import ca.dauqui.sodokusolver.localization.LocalizationChoiceBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

/**
 * Main class that creates the layout of the program
 * Also takes care of the keyboard inputs of the program
 */
public class MainScene extends Scene {

    /**
     * The sodoku grid
     */
    private final Grid grid;

    /**
     * the grid of the cell node objects because we cannot access to a specific node using x,y position from the gridPane
     */
    private final CellShow[][] gridAccess;

    /**
     * The cell currently selected
     */
    private CellShow focusedCell;

    public MainScene() {
        super(new BorderPane());
        BorderPane root = (BorderPane) getRoot();
        grid = new Grid();

        GridPane gridPane  = new GridPane();
        // this is to have a direct access to the cellshow that are in the gridpane since we cannot access the data in the gridpane in a fast way
        gridAccess = new CellShow[9][9];

        gridPane.minHeight(675);
        gridPane.maxHeight(675);
        gridPane.prefHeight(675);

        gridPane.minWidth(675);
        gridPane.maxWidth(675);
        gridPane.prefWidth(675);

        for(int i = 0; i < 9; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(12); // adds up to more than 100% so each column will be equals
            gridPane.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(12); // adds up to more than 100% so each column will be equals
            gridPane.getRowConstraints().add(rowConstraints);
        }

        // Populating the gridPane, linking each gui cell with the data cell
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                CellShow cellShow = new CellShow(grid.getCell(i, j), this);
                gridPane.add(cellShow, i, j);
                gridAccess[i][j] = cellShow;
            }
        }

        // Selecting the first cell of the grid
        focusedCell = gridAccess[0][0];
        focusedCell.setCellStyle(CellShow.SELECTED_COLOR);


        root.setCenter(gridPane);


        HBox buttonLine = new HBox();

        // This button starts the solving of the puzzle
        // it runs each solver iteratively
        // it continues running the solvers until no more changes is made to the grid
        Button solveButton = new Button();
        solveButton.textProperty().bind(LocalizedText.SOLVE_BUTTON.localizedProperty());
        solveButton.setOnAction(_ -> {
            boolean changed;
            do {
                changed = false;
                for (Solver solver : Solvers.values()) {
                    changed |= solver.solve(grid);
                }
            } while (changed);
        });
        buttonLine.getChildren().add(solveButton);

        // This button reset the puzzle to empty
        Button resetButton = new Button();
        resetButton.textProperty().bind(LocalizedText.RESET.localizedProperty());
        resetButton.setOnAction(_ -> grid.reset());
        resetButton.setFocusTraversable(false);
        buttonLine.getChildren().add(resetButton);

        // simply shows if the puzzle is solved
        Label resolved = new Label();
        resolved.textProperty().bind(grid.isResolvedProperty().asString());
        buttonLine.getChildren().add(resolved);

        LocalizationChoiceBox languageChoiceBox = new LocalizationChoiceBox();
        buttonLine.getChildren().add(languageChoiceBox);

        root.setTop(buttonLine);

        // these line are there to permit navigating in the sodoku grid with the arrows
        solveButton.setFocusTraversable(false);
        resetButton.setFocusTraversable(false);
        gridPane.setFocusTraversable(true);

        // Takes care of treating the key presses
        // lets you set the number in a cell
        // lets you navigate the grid with the arrows and wasd
        setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case KeyCode.DIGIT1 :
                    focusedCell.setValueTo(1);
                    break;
                case KeyCode.DIGIT2:
                    focusedCell.setValueTo(2);
                    break;
                case KeyCode.DIGIT3:
                    focusedCell.setValueTo(3);
                    break;
                case KeyCode.DIGIT4:
                    focusedCell.setValueTo(4);
                    break;
                case KeyCode.DIGIT5:
                    focusedCell.setValueTo(5);
                    break;
                case KeyCode.DIGIT6:
                    focusedCell.setValueTo(6);
                    break;
                case KeyCode.DIGIT7:
                    focusedCell.setValueTo(7);
                    break;
                case KeyCode.DIGIT8:
                    focusedCell.setValueTo(8);
                    break;
                case KeyCode.DIGIT9:
                    focusedCell.setValueTo(9);
                    break;
                case KeyCode.UP, KeyCode.W:
                    moveFocusedCell(Direction.UP);
                    break;
                case KeyCode.DOWN, KeyCode.S:
                    moveFocusedCell(Direction.DOWN);
                    break;
                case KeyCode.LEFT, KeyCode.A:
                    moveFocusedCell(Direction.LEFT);
                    break;
                case KeyCode.RIGHT, KeyCode.D:
                    moveFocusedCell(Direction.RIGHT);
                    break;
                default:
                    // do nothing
            }
        });
    }

    private void moveFocusedCell(Direction dir){
        int y = getCellYPos(focusedCell);
        int x = getCellXPos(focusedCell);

        switch (dir){
            case UP:
                y = y == 0 ? 0 : y-1;
                break;
            case DOWN:
                y = y == 8 ? 8 : y+1;
                break;
            case LEFT:
                x = x == 0 ? 0 : x-1;
                break;
            case RIGHT:
                x = x == 8 ? 8 : x+1;
                break;
        }

        setFocusedCell(gridAccess[x][y]);
        focusedCell.setCellStyle(CellShow.SELECTED_COLOR);
    }

    private int getCellXPos(CellShow focusedCell) {
        return focusedCell.getCell().getPosX();
    }

    private int getCellYPos(CellShow focusedCell) {
        return focusedCell.getCell().getPosY();

    }

    void setFocusedCell(CellShow cellShow) {
        focusedCell.setCellStyle(CellShow.UNSELECTED_COLOR);
        focusedCell = cellShow;
    }

    enum Direction{
        UP, DOWN, LEFT, RIGHT
    }
}