package com.crudetech.tictactoe.game;

import com.crudetech.collections.AbstractIterable;
import com.crudetech.functional.UnaryFunction;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.crudetech.matcher.Verify.verifyThat;
import static com.crudetech.query.Query.from;
import static org.hamcrest.Matchers.*;

public class LinearRandomAccessGrid implements Grid {
    private static final int Dimension = 3;
    private final Mark[] matrix;

    LinearRandomAccessGrid() {
        this(new Mark[]{
                Mark.None, Mark.None, Mark.None,
                Mark.None, Mark.None, Mark.None,
                Mark.None, Mark.None, Mark.None,
        });
    }

    private LinearRandomAccessGrid(Mark[] matrix) {
        this.matrix = matrix;
    }
    LinearRandomAccessGrid(Grid src) {
        this(new Mark[9]);
        for (Cell cell : src.getCells()) {
            matrix[computeIndexFrom(cell.getLocation().getRow(), cell.getLocation().getColumn())] = cell.getMark();
        }
    }

    public static LinearRandomAccessGrid of(Mark[] marks) {
        return new LinearRandomAccessGrid(marks);
    }

    public Mark getAt(Row row, Column column) {
        verifyThat(row, is(not(nullValue())));
        verifyThat(column, is(not(nullValue())));

        return matrix[computeIndexFrom(row, column)];
    }

    @Override
    public Iterable<Cell> getCells() {
        return new AbstractIterable<Cell>() {
            @Override
            public Iterator<Cell> iterator() {
                return new Iterator<Cell>() {
                    private int idx = 0;

                    @Override
                    public boolean hasNext() {
                        return idx < matrix.length;
                    }

                    @Override
                    public Cell next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        Location location = locationOfIndex(idx++);
                        return new Cell(location, getAt(location));
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private int computeIndexFrom(Row row, Column column) {
        return row.ordinal() * Dimension + column.ordinal();
    }

    void setAt(Row row, Column column, Mark mark) {
        verifyThat(row, is(notNullValue()));
        verifyThat(column, is(notNullValue()));
        verifyThat(mark, is(allOf(not(Mark.None), notNullValue())));

        matrix[computeIndexFrom(row, column)] = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinearRandomAccessGrid that = (LinearRandomAccessGrid) o;

        return Arrays.equals(matrix, that.matrix);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(matrix);
    }

    boolean hasMarkAt(Row row, Column column) {
        return getAt(row, column) != Mark.None;
    }

    @Override
    public String toString() {
        return "LinearRandomAccessGrid{" +
                "matrix=" + Arrays.toString(matrix) +
                '}';
    }

    @Override
    public Mark getAt(Location location) {
        verifyThat(location, is(notNullValue()));
        return getAt(location.getRow(), location.getColumn());
    }

    void setAt(Location location, Mark mark) {
        setAt(location.getRow(), location.getColumn(), mark);
    }

    private Mark getMarkAt(int index) {
        return matrix[index];
    }

    Triple winningTriple() {
        final int[][] winningTriples = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
                {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {6, 4, 2},
        };

        for (int[] winningTriple : winningTriples) {
            Mark mark = getMarkAt(winningTriple[0]);
            if (mark.isNone()) {
                continue;
            }
            Mark mark2 = getMarkAt(winningTriple[1]);
            Mark mark3 = getMarkAt(winningTriple[2]);

            if (mark.equals(mark2)
                    && mark.equals(mark3)) {
                return Triple.of(mark, locationOfIndex(winningTriple[0]), locationOfIndex(winningTriple[1]), locationOfIndex(winningTriple[2]));
            }
        }
        return Triple.Empty;
    }

    private static Location locationOfIndex(int idx) {
        Row r = Row.of(idx / Dimension);
        Column c = Column.of(idx % Dimension);
        return new Location(r, c);
    }

    private boolean isWin() {
        return !winningTriple().equals(Triple.Empty);
    }

    boolean isTieForFirstPlayersMark(Mark firstMark) {
        if (isWin()) {
            return false;
        }

        List<Integer> nonMarkedCells = from(0, 1, 2, 3, 4, 5, 6, 7, 8).where(isNotMarked()).toList();
        if (nonMarkedCells.size() == 1) {
            LinearRandomAccessGrid grid = new LinearRandomAccessGrid(this.matrix.clone());
            grid.setAt(locationOfIndex(nonMarkedCells.get(0)), firstMark);
            return grid.isTieForFirstPlayersMark(firstMark);
        } else {
            return nonMarkedCells.isEmpty();
        }
    }

    private UnaryFunction<? super Integer, Boolean> isNotMarked() {
        return new UnaryFunction<Integer, Boolean>() {
            @Override
            public Boolean execute(Integer integer) {
                return matrix[integer].equals(Mark.None);
            }
        };
    }
}
