package com.crudetech.tictactoe.game;

import com.crudetech.lang.Enums;

import java.util.Objects;

import static com.crudetech.matcher.Verify.verifyThat;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public interface Grid {
    Mark getAt(Location location);

    Iterable<Cell> getCells();

    Iterable<Cell> difference(Grid rhs);

    public enum Mark {
        Cross {
            @Override
            public Mark getOpposite() {
                return Nought;
            }
        },
        Nought {
            @Override
            public Mark getOpposite() {
                return Cross;
            }
        },
        None {
            @Override
            public Mark getOpposite() {
                throw new IllegalStateException("Can't be done for None!");
            }
        };
        public abstract Mark getOpposite();
    }

    public static enum Row {
        First, Second, Third;

        static Row of(int row) {
            return Enums.ofOrdinal(Row.class, row);
        }

        public Row nextOrFlip() {
            return compareTo(Third) < 0
                    ? Enums.ofOrdinal(Row.class, ordinal() + 1)
                    : First;
        }

        public Row previousOrFlip() {
            return compareTo(First) > 0 ? Enums.ofOrdinal(Row.class, ordinal() - 1) : Third;
        }
    }

    public static enum Column {
        First, Second, Third;

        static Column of(int row) {
            return Enums.ofOrdinal(Column.class, row);
        }

        public Column nextOrFlip() {
            return compareTo(Third) < 0
                    ? Enums.ofOrdinal(Column.class, ordinal() + 1)
                    : First;
        }

        public Column previousOrFlip() {
            return compareTo(First) > 0
                    ? Enums.ofOrdinal(Column.class, ordinal() - 1)
                    : Third;

        }
    }

    public class Location {
        private final Row row;
        private final Column column;

        Location(Row row, Column column) {
            verifyThat(row, is(notNullValue()));
            verifyThat(column, is(notNullValue()));
            this.row = row;
            this.column = column;
        }

        public Row getRow() {
            return row;
        }

        public Column getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Location location = (Location) o;

            return Objects.equals(column, location.column)
                    && Objects.equals(row, location.row);

        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        @Override
        public String toString() {
            return "Location{" +
                    "row=" + row +
                    ", column=" + column +
                    '}';
        }

        public static Location of(Row row, Column column) {
            return new Location(row, column);
        }
    }

    public class Cell {
        private final Mark mark;
        private final Location location;

        public Cell(Location location, Mark mark) {
            verifyThat(location, is(notNullValue()));
            verifyThat(mark, is(notNullValue()));
            this.mark = mark;
            this.location = location;
        }

        public Mark getMark() {
            return mark;
        }

        public Location getLocation() {
            return location;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cell cell = (Cell) o;

            return Objects.equals(location, cell.location)
                    && Objects.equals(mark, cell.mark);

        }

        @Override
        public int hashCode() {
            return Objects.hash(mark, location);
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "mark=" + mark +
                    ", location=" + location +
                    '}';
        }
    }

    public class ThreeInARow {
        public static final ThreeInARow Empty = new ThreeInARow(Mark.None, null, null, null);

        private final Mark mark;
        private final Location first;
        private final Location second;
        private final Location third;

        ThreeInARow(Mark mark, Location first, Location second, Location third) {
            verifyThat(mark, is(notNullValue()));

            this.mark = mark;
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public static ThreeInARow of(Mark mark, Location first, Location second, Location third) {
            return new ThreeInARow(mark, first, second, third);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ThreeInARow triple = (ThreeInARow) o;

            return Objects.equals(mark, triple.mark)
                    && Objects.equals(first, triple.first)
                    && Objects.equals(second, triple.second)
                    && Objects.equals(third, triple.third);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mark, first, second, third);
        }

        @Override
        public String toString() {
            return "ThreeInARow{" +
                    "mark=" + mark +
                    ", first=" + first +
                    ", second=" + second +
                    ", third=" + third +
                    '}';
        }

        public Iterable<Location> getLocations() {
            return asList(first, second, third);
        }

        public boolean isWinForMark(Mark mark) {
            return !equals(Empty)
                    && this.mark == mark;
        }
    }
}
