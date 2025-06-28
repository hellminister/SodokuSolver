package ca.dauqui.sodokusolver.game;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This class represents a cell of the sodoku grids
 * It knows all values that are possible in this position
 */
public class Cell {
    private static final Logger LOG = Logger.getLogger(Cell.class.getName());

    /**
     * contains all values that are possible in this cell
     */
    private final SetProperty<Integer> possibilities;

    /**
     * so other classes can look into, ot listen to the set without risking modification from the outside
     */
    private final ObservableSet<Integer> readOnlyPossibilities;
    /**
     * whether the value of the cell is chosen
     */
    private final BooleanProperty chosen;
    /**
     * whether the value of the cell was set manually
     */
    private final BooleanProperty set;
    /**
     * the x position of the cell in the grid
     */
    private final int xPos;
    /**
     * the y position of the cell in the grid
     */
    private final int yPos;

    /**
     * the different groups this cell is part of
     */
    private Group horizontalGroup;
    private Group verticalGroup;
    private Group blockGroup;

    public Cell(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        possibilities = new SimpleSetProperty<>(FXCollections.observableSet());
        readOnlyPossibilities = FXCollections.unmodifiableObservableSet(possibilities.get());
        chosen = new SimpleBooleanProperty(false);
        set = new SimpleBooleanProperty(false);
        for (int i = 1; i <= 9; i++) {
            getPossibilitiesInternal().add(i);
        }

        // a value is chosen if it is the last possible value of the cell
        chosen.bind(Bindings.equal(1, possibilities.sizeProperty()));
    }

    /**
     * @return the possible values of the cell
     */
    private ObservableSet<Integer> getPossibilitiesInternal() {
        return possibilities.get();
    }

    /**
     * @return the possible values of the cell (read-only)
     */
    public ObservableSet<Integer> getReadOnlyPossibilities() {
        return readOnlyPossibilities;
    }

    /**
     * Sets the value of the cell
     * @param value the value to be set
     * @param manual whether it was set by the user or the program
     * @return whether the call made a change to the grid
     */
    public boolean setCellValue(int value, boolean manual) {
        // keep only the value to be set
        boolean changed = getPossibilitiesInternal().retainAll(Set.of(value));
        if (getPossibilitiesInternal().isEmpty()) {
            LOG.severe(() -> "Illegal value " + value + " for cell (" + xPos + ", " + yPos + "). It was already removed.");
        }
        set.set(manual);
        // remove the set value from all other cells of the groups this cell is a member of
        changed |= horizontalGroup.removeValueFromOtherCells(value, this);
        changed |= verticalGroup.removeValueFromOtherCells(value, this);
        changed |= blockGroup.removeValueFromOtherCells(value, this);

        return changed;
    }

    /**
     * remove a value from the cell
     * @param value the value to be removed
     * @return whether the call made a change to the grid
     */
    public boolean removeCellValue(int value) {
        boolean changed = getPossibilitiesInternal().remove(value);
        if (getPossibilitiesInternal().isEmpty()) {
            LOG.severe(() -> "Illegal value " + value + " for cell (" + xPos + ", " + yPos + "). All values were removed.");
        } else if (getPossibilitiesInternal().size() == 1) { // only 1 value left
            int lastValue = getPossibilitiesInternal().iterator().next();
            // we remove the last value from all cells from the groups this cell is a member of
            // we do this since no other cells in those groups can be with this value
            changed |= horizontalGroup.removeValueFromOtherCells(lastValue, this);
            changed |= verticalGroup.removeValueFromOtherCells(lastValue, this);
            changed |= blockGroup.removeValueFromOtherCells(lastValue, this);
        }
        return changed;
    }

    /**
     * @return the horizontal line this cell is a member of
     */
    public Group getHorizontalGroup() {
        return horizontalGroup;
    }

    /**
     * @param horizontalGroup the horizontal line this cell is a member of
     */
    public void setHorizontalGroup(Group horizontalGroup) {
        this.horizontalGroup = horizontalGroup;
    }

    /**
     * @return the vertical line this cell is a member of
     */
    public Group getVerticalGroup() {
        return verticalGroup;
    }

    /**
     * @param verticalGroup the vertical line this cell is a member of
     */
    public void setVerticalGroup(Group verticalGroup) {
        this.verticalGroup = verticalGroup;
    }

    /**
     * @return the 3x3 block this cell is a member of
     */
    public Group getBlockGroup() {
        return blockGroup;
    }

    /**
     * @param blockGroup the 3x3 block this cell is a member of
     */
    public void setBlockGroup(Group blockGroup) {
        this.blockGroup = blockGroup;
    }

    /**
     * @return the property containing the chosen state
     */
    public ReadOnlyBooleanProperty isChosenProperty() {
        return chosen;
    }

    /**
     * @return the property containing the manually set property
     */
    public ReadOnlyBooleanProperty isSetProperty() {
        return set;
    }

    /**
     * resets the cell to possess all possible values
     */
    public void reset() {
        ObservableSet<Integer> poss = getPossibilitiesInternal();
        set.set(false);
        poss.clear();
        for (int i = 1; i <= 9; i++) {
            poss.add(i);
        }
    }

    /**
     * @return a string with the cell (x, y) position
     */
    public String getPositionString(){
        return "(" + xPos + ", " + yPos + ")";
    }

    /**
     * @return the x position of the cell in the grid
     */
    public int getPosX() {
        return xPos;
    }

    /**
     * @return the y position of the cell in the grid
     */
    public int getPosY() {
        return yPos;
    }

    /**
     * keeps only the values given in the parameters
     * @param i the list of values to keep
     * @return whether the call mage a change to the grid
     */
    public boolean keepValues(int... i) {
        Set<Integer> toKeep = new HashSet<>();
        for (int value : i) {
            toKeep.add(value);
        }
        return getPossibilitiesInternal().retainAll(toKeep);
    }

}