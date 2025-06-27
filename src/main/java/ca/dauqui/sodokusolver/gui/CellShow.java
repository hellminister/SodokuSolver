package ca.dauqui.sodokusolver.gui;


import ca.dauqui.sodokusolver.game.Cell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * This class shows the informations contained in a cell in the gui
 */
class CellShow extends StackPane {
    public static final String SELECTED_COLOR = "lightblue";
    public static final String UNSELECTED_COLOR = "white";

    /**
     * the cell shown by this object
     */
    private final Cell cell;

    /**
     * the insets value for the border
     * the grid lines are created by each cell to be able to do single and double border line width
     */
    private final String insets;

    public CellShow(Cell cell, MainScene gridPane) {
        this.cell = cell;

        setMinHeight(75);
        setPrefHeight(75);
        setMinWidth(75);
        setPrefWidth(75);

        insets = getInsets(cell);

        setCellStyle(UNSELECTED_COLOR);

        // This pane shows all possibilities that can be set in this cell
        GridPane possibilities = new GridPane();
        // Will be visible only if the cell value isnt chosen
        possibilities.visibleProperty().bind(cell.isChosenProperty().not());

        for(int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(34); // adds up to more than 100% so each column will be equals
            possibilities.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(34); // adds up to more than 100% so each column will be equals
            possibilities.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = i + (j * 3) + 1;

                Label label = new Label();
                label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                label.setText(value+"");
                // the value of this position is shown only if it is a valid value for the puzzle
                label.visibleProperty().bind(Bindings.createBooleanBinding( () -> cell.getPossibilities().contains( value ), cell.getPossibilities() ));

                StackPane toCenter = new StackPane();
                toCenter.getChildren().add(label);
                StackPane.setAlignment(label, Pos.CENTER);

                possibilities.add(toCenter, i, j);
            }
        }

        // This pane shows the number that is chosen for the cell
        StackPane chosenValue = new StackPane();
        chosenValue.visibleProperty().bind(cell.isChosenProperty());

        Text chosenValueLabel = new Text();
        chosenValueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));

        // sets the only valid number from the cell (ie: the chosen value)
        chosenValueLabel.textProperty().bind(new StringBinding() {
            {
                bind(cell.getPossibilities());
            }

            @Override
            protected String computeValue() {
                if (cell.getPossibilities().size() == 1) {
                    return cell.getPossibilities().toArray()[0].toString();
                }
                else
                    return "";
            }
        });

        // Sets the color of the chosen value depending on whether it was manually set or automatically found
        chosenValueLabel.fillProperty().bind(new ObjectBinding<>() {
            {
                bind(cell.isSetProperty());
            }
            @Override
            protected Paint computeValue() {
                if (cell.isSetProperty().get()){
                    return Color.BLACK;
                }
                return Color.BLUE;
            }
        });

        chosenValue.getChildren().add(chosenValueLabel);

        getChildren().addAll(possibilities, chosenValue);
        StackPane.setAlignment(possibilities, Pos.CENTER);
        StackPane.setAlignment(chosenValue, Pos.CENTER);

        // sets the focused cell based on a click event
        setOnMouseReleased((_ -> {
            gridPane.setFocusedCell(this);
            setCellStyle(SELECTED_COLOR);
        }));

    }

    /**
     * Calculates the type of border of the cell depending on the position of the cell in the grid
     * Makes small border inside the 3x3 grid and a thick border around each 3x3 grid
     */
    private static String getInsets(Cell cell) {
        int x = cell.getPosX() % 3;
        int y = cell.getPosY() % 3;

        String insets = "";
        if (y == 0){
            insets += "1.5 ";
        } else {
            insets += "0.5 ";
        }

        if (x == 2){
            insets += "1.5 ";
        } else {
            insets += "0.5 ";
        }

        if (y == 2){
            insets += "1.5 ";
        } else {
            insets += "0.5 ";
        }

        if (x == 0){
            insets += "1.5";
        } else {
            insets += "0.5";
        }
        return insets;
    }

    /**
     * sets the value of the cell if a value isnt already chosen
     * @param i the value to be set
     */
    public void setValueTo(int i) {
        if (!cell.isChosenProperty().get()) {
            cell.setCellValue(i, true);
        }
    }

    /**
     * Sets the background color of the cell and its border
     * @param color the color of the background
     */
    public void setCellStyle(String color){
        setStyle("-fx-background-color: black, "+ color +";" +
                "-fx-background-insets: 0, " + insets + " ;");
    }

    /**
     * @return the attached cell shown by this object
     */
    public Cell getCell() {
        return cell;
    }
}