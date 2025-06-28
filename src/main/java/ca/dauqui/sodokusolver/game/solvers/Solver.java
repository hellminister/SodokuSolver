package ca.dauqui.sodokusolver.game.solvers;

import ca.dauqui.sodokusolver.game.Grid;

/**
 * Interface of a solver
 * this represents a strategy to be able to choose or remove numbers from cells
 */
public interface Solver {
    /**
     * runs a solving strategy on the grid
     *
     * @param grid the grid to solve
     * @return whether the call changed the grid
     */
    boolean solve(Grid grid);
}