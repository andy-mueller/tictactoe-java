package com.crudetech.tictactoe.game;

import com.crudetech.functional.BinaryFunction;
import com.crudetech.functional.UnaryFunction;
import com.crudetech.query.Queryable;

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
        this.matrix = Arrays.copyOf(matrix, matrix.length);
    }

    LinearRandomAccessGrid(Grid src) {
        this(new Mark[9]);
        for (Cell cell : src.getCells()) {
            matrix[computeIndexFrom(cell)] = cell.getMark();
        }
    }

    LinearRandomAccessGrid(LinearRandomAccessGrid src) {
        this(src.matrix);
    }

    public static LinearRandomAccessGrid empty() {
        return new LinearRandomAccessGrid();
    }

    public static LinearRandomAccessGrid of(Mark... marks) {
        return new LinearRandomAccessGrid(marks);
    }

    public static LinearRandomAccessGrid of(Grid grid) {
        return new LinearRandomAccessGrid(grid);
    }


    public static LinearRandomAccessGrid of(LinearRandomAccessGrid grid) {
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
        return row.position() * Dimension + column.position();
    }

    private int computeIndexFrom(Location location) {
        return computeIndexFrom(location.getRow(), location.getColumn());
    }

    private int computeIndexFrom(Cell cell) {
        return computeIndexFrom(cell.getLocation());
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

    void setAt(Row row, Column column, Mark mark) {
        verifyThat(row, is(notNullValue()));
        verifyThat(column, is(notNullValue()));
        verifyThat(mark, is(allOf(not(Mark.None), notNullValue())));

        matrix[computeIndexFrom(row, column)] = mark;
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

    @Override
    public Cell getCellAt(Location location) {
        return new Cell(location, getAt(location));
    }

    private static final Integer[][] allPossibleThreeInARows = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
            {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {6, 4, 2}
    };

    ThreeInARow getThreeInARow() {
        return from(allPossibleThreeInARows)
                .select(toLocation())
                .where(markIsSet())
                .where(isWinningThreeInARow())
                .select(toThreeInARow())
                .firstOr(ThreeInARow.Empty);

    }

    private UnaryFunction<Iterable<Location>, ThreeInARow> toThreeInARow() {
        return new UnaryFunction<Iterable<Location>, ThreeInARow>() {
            @Override
            public ThreeInARow execute(Iterable<Location> locations) {
                Location[] locations1 = from(locations).toArray(Location.class);
                return new ThreeInARow(getAt(locations1[0]), locations1[0], locations1[1], locations1[2]);
            }
        };
    }

    private UnaryFunction<Iterable<Location>, Boolean> isWinningThreeInARow() {
        return new UnaryFunction<Iterable<Location>, Boolean>() {
            @Override
            public Boolean execute(Iterable<Location> locations) {
                Queryable<Mark> marks = from(locations).select(toMark());
                return !marks.where(notEqualTo(marks.first())).any();
            }
        };
    }

    private UnaryFunction<Iterable<Location>, Boolean> markIsSet() {
        return new UnaryFunction<Iterable<Location>, Boolean>() {
            @Override
            public Boolean execute(Iterable<Location> locations) {
                return from(locations).select(toMark()).where(notEqualTo(Mark.None)).any();
            }
        };
    }

    private UnaryFunction<Location, Mark> toMark() {
        return new UnaryFunction<Location, Mark>() {
            @Override
            public Mark execute(Location location) {
                return getAt(location);
            }
        };
    }


    private UnaryFunction<Integer[], Iterable<Location>> toLocation() {
        return new UnaryFunction<Integer[], Iterable<Location>>() {
            @Override
            public Iterable<Location> execute(Integer[] indices) {
                return from(indices).select(indexToLocation());
            }
        };
    }

    private UnaryFunction<Integer, Location> indexToLocation() {
        return new UnaryFunction<Integer, Location>() {
            @Override
            public Location execute(Integer idx) {
                return locationOfIndex(idx);
            }
        };
    }

    private static <T> UnaryFunction<T, Boolean> notEqualTo(final T item) {
        return new UnaryFunction<T, Boolean>() {
            @Override
            public Boolean execute(T t) {
                return !Objects.equals(item, t);
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

    public Grid snapshot() {
        return LinearRandomAccessGrid.of(this);
    }
}
