package com.crudetech.tictactoe.game;

import com.crudetech.collections.Pair;
import com.crudetech.functional.BinaryFunction;
import com.crudetech.functional.UnaryFunction;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        verifyThat(matrix, is(allOf(notNullValue(), arrayWithSize(9))));
        this.matrix = matrix;
    }

    LinearRandomAccessGrid(Grid src) {
        this(new Mark[9]);
        for (Cell cell : src.getCells()) {
            matrix[computeIndexFrom(cell.getLocation().getRow(), cell.getLocation().getColumn())] = cell.getMark();
        }
    }

    public static Grid empty() {
        return new LinearRandomAccessGrid();
    }

    public static LinearRandomAccessGrid of(Mark... marks) {
        return new LinearRandomAccessGrid(marks);
    }

    public static LinearRandomAccessGrid of(Grid grid) {
        return new LinearRandomAccessGrid(grid);
    }

    public Mark getAt(Row row, Column column) {
        verifyThat(row, is(not(nullValue())));
        verifyThat(column, is(not(nullValue())));

        return matrix[computeIndexFrom(row, column)];
    }

    @Override
    public Iterable<Cell> getCells() {
        return from(matrix).select(toCell());
    }

    private static BinaryFunction<Mark, Integer, Cell> toCell() {
        return new BinaryFunction<Mark, Integer, Cell>() {
            @Override
            public Cell execute(Mark mark, Integer pos) {
                return new Cell(locationOfIndex(pos), mark);
            }
        };
    }

    private static int computeIndexFrom(Row row, Column column) {
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

    @Override
    public Iterable<Cell> difference(final Grid right) {
        return from(right.getCells()).where(differentFromThis());
    }

    private UnaryFunction<? super Cell, Boolean> differentFromThis() {
        return new UnaryFunction<Cell, Boolean>() {
            @Override
            public Boolean execute(Cell cell) {
                return !Objects.equals(cell, getCellAt(cell.getLocation()));
            }
        };
    }

    private Cell getCellAt(Location location) {
        return new Cell(location, getAt(location));
    }

    private static class AllPossibleThreeInARow extends AbstractList<Pair<Mark[], Location[]>> {
        private static final int[][] triples = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
                {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {6, 4, 2}
        };
        private final Mark[] matrix;

        AllPossibleThreeInARow(Mark[] matrix) {
            this.matrix = matrix;
        }
        static AllPossibleThreeInARow of(Mark[] matrix){
            return new AllPossibleThreeInARow(matrix);
        }

        @Override
        public Pair<Mark[], Location[]> get(int index) {
            int[] indices = triples[index];
            Mark[] marks = new Mark[indices.length];
            Location[] locations = new Location[indices.length];
            for (int i = 0; i < indices.length; ++i) {
                final int idx = indices[i];
                marks[i] = getMarkAt(idx);
                locations[i] = locationOfIndex(idx);
            }

            return new Pair<>(marks, locations);
        }

        private Mark getMarkAt(int index) {
            return matrix[index];
        }

        @Override
        public int size() {
            return triples.length;
        }
    }

    ThreeInARow getThreeInARow() {
        for (Pair<Mark[], Location[]> triple : AllPossibleThreeInARow.of(matrix)) {
            if (isWin(triple.getFirst())) {
                Location[] locations = triple.getSecond();
                return ThreeInARow.of(triple.getFirst()[0], locations[0], locations[1], locations[2]);
            }
        }
        return ThreeInARow.Empty;
    }

    private static boolean isWin(Mark... marks) {
        return containsNoNoneMarks(marks)
                && allItemsAreEqual(marks);
    }

    private static boolean allItemsAreEqual(Mark[] marks) {
        return !from(marks).where(notEqualTo(marks[0])).any();
    }

    private static boolean containsNoNoneMarks(Mark[] marks) {
        return !from(marks).where(isEqualTo(Mark.None)).any();
    }

    private static <T> UnaryFunction<T, Boolean> notEqualTo(final T item) {
        return new UnaryFunction<T, Boolean>() {
            @Override
            public Boolean execute(T t) {
                return !Objects.equals(item, t);
            }
        };
    }

    private static <T> UnaryFunction<T, Boolean> isEqualTo(final T item) {
        return new UnaryFunction<T, Boolean>() {
            @Override
            public Boolean execute(T t) {
                return Objects.equals(t, item);
            }
        };
    }

    private static Location locationOfIndex(int idx) {
        Row r = Row.of(idx / Dimension);
        Column c = Column.of(idx % Dimension);
        return new Location(r, c);
    }

    private boolean isWin() {
        return !getThreeInARow().equals(ThreeInARow.Empty);
    }

    boolean isWinForMark(Mark startPlayerMark) {
        ThreeInARow triple = getThreeInARow();
        return triple.isWinForMark(startPlayerMark);
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
