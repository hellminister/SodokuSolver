package ca.dauqui.sodokusolver.game;

import java.util.Set;

/**
 * This class represents a line, column or 3x3 block of the grid
 */
public class Group {
    /**
     * the cells in this group
     */
    private final Cell[] cells;

    public Group() {
        cells = new Cell[9];
    }

    /**
     * sets a cell in this group
     * @param pos the position of the cell in the group
     * @param cell the cell in that position
     */
    public void setCell(int pos, Cell cell) {
        cells[pos] = cell;
    }

    /**
     * @param pos the position of the cell we want
     * @return the cell in the asked position
     */
    public Cell getCell(int pos) {
        return cells[pos];
    }

    /**
     * removes a value from all other cells in the group
     * @param value the value to remove
     * @param cell the cells that will keep the number
     */
    public void removeValueFromOtherCells(int value, Cell... cell) {
        Set<Cell> set = Set.of(cell);
        for (Cell cell1 : cells) {
            if (!set.contains(cell1) && cell1.getPossibilities().size() != 1) {
                cell1.removeCellValue(value);
            }
        }
    }
}