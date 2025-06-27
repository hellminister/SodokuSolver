package ca.dauqui.sodokusolver.game.solvers;

import ca.dauqui.sodokusolver.game.Cell;
import ca.dauqui.sodokusolver.game.Grid;
import ca.dauqui.sodokusolver.game.Group;

import java.util.Set;

import static ca.dauqui.sodokusolver.game.solvers.Util.sortCellsByNumbers;

/**
 * This solver simply finds each value that can only be placed in a single cell in a group and set that value in the cell
 */
public class SinglePossiblePlaceInGroup implements Solver{
    @Override
    public void solve(Grid grid) {
        for (Group group : grid.getVerticalLines()){
            doGroup(group);
        }
        for (Group group : grid.getHorizontalLines()){
            doGroup(group);
        }
        for (Group group : grid.getBlocks()){
            doGroup(group);
        }
    }

    /**
     * group all cells by possible numbers and find all numbers that can be in only 1 cell and sets those cells
     * @param group the group to treat
     */
    private void doGroup(Group group) {
        Set<Cell>[] numbers = sortCellsByNumbers(group);

        for (int i = 0; i < 9; i++) {
            Set<Cell> cells = numbers[i];
            if (cells.size() == 1){
                for (Cell cell : cells){
                    if (!cell.isChosenProperty().get()) {
                        cell.setCellValue(i + 1, false);
                    }
                }
            }
        }
    }


}