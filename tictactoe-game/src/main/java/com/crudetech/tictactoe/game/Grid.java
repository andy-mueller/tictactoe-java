package com.crudetech.tictactoe.game;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public interface Grid extends Iterable<Grid.Cell> {
    Mark getAt(Row row, Column column);

    public enum Mark {
        Cross, Nought, None;

        boolean isNone() {
            return this == None;
        }
    }

    public static enum Row {
        First, Second, Third;

        static Row of(int row) {
            return Enums.ofOrdinal(Row.class, row);
        }
    }

    public static enum Column {
        First, Second, Third;

        static Column of(int row) {
            return Enums.ofOrdinal(Column.class, row);
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

        Row getRow() {
            return row;
        }

        Column getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Location location = (Location) o;

            return column == location.column
                && row == location.row;

        }

        @Override
        public int hashCode() {
            int result = row.hashCode();
            result = 31 * result + column.hashCode();
            return result;
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

            return location.equals(cell.location)
                && mark == cell.mark;

        }

        @Override
        public int hashCode() {
            int result = mark.hashCode();
            result = 31 * result + location.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "mark=" + mark +
                    ", location=" + location +
                    '}';
        }
    }

    public class Triple {
        public static final Triple Empty = new Triple(Mark.None, null, null, null);

        private final Mark mark;
        private final Location first;
        private final Location second;
        private final Location third;

        Triple(Mark mark, Location first, Location second, Location third) {
            verifyThat(mark, is(notNullValue()));
            verifyThat(first, is(notNullValue()));
            verifyThat(second, is(notNullValue()));
            verifyThat(third, is(notNullValue()));

            this.mark = mark;
            this.first = first;
            this.second = second;
            this.third = third;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Triple triple = (Triple) o;

            return first.equals(triple.first)
                && mark.equals(triple.mark)
                && second.equals(triple.second)
                && third.equals(triple.third);

        }

        @Override
        public int hashCode() {
            int result = mark.hashCode();
            result = 31 * result + first.hashCode();
            result = 31 * result + second.hashCode();
            result = 31 * result + third.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Triple{" +
                    "mark=" + mark +
                    ", first=" + first +
                    ", second=" + second +
                    ", third=" + third +
                    '}';
        }
    }

}
