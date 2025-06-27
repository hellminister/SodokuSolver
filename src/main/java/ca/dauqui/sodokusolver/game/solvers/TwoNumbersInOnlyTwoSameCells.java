package ca.dauqui.sodokusolver.game.solvers;

import ca.dauqui.sodokusolver.game.Cell;
import ca.dauqui.sodokusolver.game.Grid;
import ca.dauqui.sodokusolver.game.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ca.dauqui.sodokusolver.game.solvers.Util.sortCellsByNumbers;

/**
 * This solver finds cells were only 2 cells shares the same 2 numbers and sets those cells as having only those 2 numbers
 * <p>
 * Example:
 * cells    0     1     2    3   4    5    6   7   8
 * values   4567  4567  678  12  236  789  38  189 1289
 * results  45    45    678  12  236  789  38  189 1289
 *
 */
public class TwoNumbersInOnlyTwoSameCells implements Solver{

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
        Set<Cell>[] numbers = sortCellsByNumbers(group);
        List<Integer> setOfTwo = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            if (numbers[i].size() == 2) {
                setOfTwo.add(i);
            }
        }

        if (setOfTwo.size() > 1) {
            for (Integer i : setOfTwo){
                Set<Cell> cells1 = numbers[i];
                for (Integer j : setOfTwo){
                    // this condition is to prevent comparing b with a when a with b was already compared
                    if (i.compareTo(j) < 0) {
                        Set<Cell> cells2 = numbers[j];
                        if (cells1.equals(cells2)){
                            for(Cell cell : cells1){
                                cell.keepValues(i+1, j+1);
                            }
                        }
                    }
                }
            }
        }
    }
}