package ca.dauqui.sodokusolver.game.solvers;

import ca.dauqui.sodokusolver.game.Cell;
import ca.dauqui.sodokusolver.game.Grid;
import ca.dauqui.sodokusolver.game.Group;

/**
 * This solver find all cells that contains exactly the same 2 possible value and removes those value from all other cells
 * ex.:
 * if cell 1 and cell 2 both have possible values of 4 5 only and cell 3 have possible values of 4 5 6 7
 * then the values 4 and 5 will be removed from cell 3
 */
public class TwoCellWithExactlySameTwoNumber implements Solver{
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

    private void doGroup(Group group) {
        for (int i = 0; i < 9; i++) {
            Cell cell1 = group.getCell(i);
            if (cell1.getPossibilities().size() == 2) {
                for (int j = i + 1; j < 9; j++) {
                    Cell cell2 = group.getCell(j);
                    if (cell1.getPossibilities().equals(cell2.getPossibilities())) {
                        for(Integer value : cell1.getPossibilities()){
                            group.removeValueFromOtherCells(value, cell1, cell2);
                        }
                    }
                }
            }
        }
    }
}