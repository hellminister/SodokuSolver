package ca.dauqui.sodokusolver.game.solvers;

import ca.dauqui.sodokusolver.game.Grid;

/**
 * an enum that keeps a copy of each solver to be easily accessible
 */
public enum Solvers implements Solver{
    SINGLE_POSS_IN_GROUP(new SinglePossiblePlaceInGroup()),
    TWO_CELLS_WITH_ONLY_TWO_SAME_NUMBER(new TwoCellWithExactlySameTwoNumber()),
    TWO_NUMBER_IN_TWO_CELL(new TwoNumbersInOnlyTwoSameCells()),
    ;

    private final Solver solver;

    Solvers(Solver solver) {
        this.solver = solver;
    }

    @Override
    public boolean solve(Grid grid) {
        return solver.solve(grid);
    }
}