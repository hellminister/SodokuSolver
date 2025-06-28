package ca.dauqui.sodokusolver.game.solvers;

import ca.dauqui.sodokusolver.game.Grid;
import ca.dauqui.sodokusolver.game.Group;

/**
 * This super class for solver is used for strategies that pertain to each group individually
 * It will run the strategy on each group
 */
public abstract class SingleGroupSolver implements Solver {

    @Override
    public boolean solve(Grid grid) {
        boolean changed = false;
        for (Group group : grid.getVerticalLines()) {
            changed |= doGroup(group);
        }
        for (Group group : grid.getHorizontalLines()) {
            changed |= doGroup(group);
        }
        for (Group group : grid.getBlocks()) {
            changed |= doGroup(group);
        }
        return changed;
    }

    /**
     * The strategy to run on the group
     * @param group the group on which to run the strategy
     * @return whether this call changed the grid
     */
    protected abstract boolean doGroup(Group group);
}