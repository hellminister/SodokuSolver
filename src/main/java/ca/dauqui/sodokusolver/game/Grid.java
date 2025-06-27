package ca.dauqui.sodokusolver.game;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the sodoku grid
 */
public class Grid {
    /**
     * All the cells of the grid
     */
    private final Cell[][] sodokuGrid = new Cell[9][9];

    /**
     * Each vertical line of the grid
     */
    private final Group[] verticalLines = new Group[9];
    /**
     * Each horizontal line of the grid
     */
    private final Group[] horizontalLines = new Group[9];
    /**
     * each 3x3 block of the grid
     */
    private final Group[] blocks = new Group[9];
    /**
     * whether the grid is resolved or not
     */
    private final BooleanProperty resolved = new SimpleBooleanProperty(false);

    public Grid(){
        for (int i = 0; i < 9; i++) {
            verticalLines[i] = new Group();
            horizontalLines[i] = new Group();
            blocks[i] = new Group();
        }

        // binds the resolve boolean to the chosen state of each cell
        // resolve is true if and only if all cells have a chosen value
        var resolveBinding = new BooleanBinding() {
            final List<ReadOnlyBooleanProperty> chosens = new ArrayList<>();

            public void addToBind(ReadOnlyBooleanProperty chosen){
                bind(chosen);
                chosens.add(chosen);
            }

            @Override
            protected boolean computeValue() {
                for (ReadOnlyBooleanProperty chosen : chosens) {
                    if (!chosen.get()){
                        return false;
                    }
                }
                return true;
            }
        };

        resolved.bind(resolveBinding);

        // initialize the sodoku grid
        for (int i = 0; i < sodokuGrid.length; i++) {
            for (int j = 0; j < sodokuGrid[i].length; j++) {
                var cell = new Cell(i, j);
                sodokuGrid[i][j] = cell;
                resolveBinding.addToBind(cell.isChosenProperty());

                verticalLines[i].setCell(j, cell);
                cell.setVerticalGroup(verticalLines[i]);
                horizontalLines[j].setCell(i, cell);
                cell.setHorizontalGroup(horizontalLines[j]);

                blocks[calculateBlock(i,j)].setCell(calculateCell(i,j), cell);
                cell.setBlockGroup(blocks[calculateBlock(i,j)]);

            }
        }
    }

    /**
     * resets the grid to empty
     */
    public void reset(){
        for (Cell[] cells : sodokuGrid) {
            for (Cell cell : cells) {
                cell.reset();
            }
        }
    }

    /**
     * Calcule in which block the cell goes in
     * the block numbers goes from left to right and up to down :
     * 0 1 2
     * 3 4 5
     * 6 7 8
     * @param i the x position of the cell
     * @param j the y position of the cell
     * @return the block number the cell goes in
     */
    private static int calculateBlock(int i, int j) {
        int a = i / 3;
        int b = j / 3;

        return b + (a * 3);
    }

    /**
     * Calcule in which cell of the block the cell goes in
     * the cell numbers goes from left to right and up to down :
     * 0 1 2
     * 3 4 5
     * 6 7 8
     * @param i the x position of the cell
     * @param j the y position of the cell
     * @return the cell number inside the block the cell goes in
     */
    private static int calculateCell(int i, int j) {
        int a = i % 3;
        int b = j % 3;

        return b + (a * 3);
    }

    /**
     * return the cell at the requested position
     * @param i the x position
     * @param j the y position
     * @return the cell at the given position
     */
    public Cell getCell(int i, int j) {
        return sodokuGrid[i][j];
    }

    /**
     * @return the property containing the resolved state of the grid
     */
    public ReadOnlyBooleanProperty isResolvedProperty() {
        return resolved;
    }

    /**
     * @return all vertical lines of the grid
     */
    public Group[] getVerticalLines() {
        return  verticalLines;
    }

    /**
     * @return all horizontal lines of the grid
     */
    public Group[] getHorizontalLines() {
        return  horizontalLines;
    }

    /**
     * @return all 3x3 blocks of the grid
     */
    public Group[] getBlocks() {
        return blocks;
    }
}