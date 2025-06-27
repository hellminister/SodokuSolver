package ca.dauqui.sodokusolver.game.solvers;

import ca.dauqui.sodokusolver.game.Cell;
import ca.dauqui.sodokusolver.game.Group;

import java.util.HashSet;
import java.util.Set;

/**
 * A simple class containing some utilities methods
 */
public final class Util {
    private Util() {}

    /**
     * group all cells based on the number they contain
     * ie all cells that can be 1 are grouped together, all those with 2s together, etc
     * @param group the cell group to analyze
     * @return an array of sets of the grouped cells each position in the array represents a value-1
     */
    @SuppressWarnings("unchecked")
    public static Set<Cell>[] sortCellsByNumbers(Group group) {
        // the array of cell groups
        // each position represent the value - 1 (so position 0 is for value 1, etc.)
        Set<Cell>[] numbers = new Set[9];
        for (int i = 0; i < 9; i++) {
            numbers[i] = new HashSet<>();
        }

        // this sorts the cells in the group by possible values
        for (int i = 0; i < 9; i++) {
            Cell cell = group.getCell(i);

            for (Integer number : cell.getPossibilities()){
                numbers[number-1].add(cell);
            }
        }
        return numbers;
    }


}