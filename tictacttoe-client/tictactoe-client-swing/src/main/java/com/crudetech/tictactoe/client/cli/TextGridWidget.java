package com.crudetech.tictactoe.client.cli;

import com.crudetech.functional.BinaryFunction;
import com.crudetech.tictactoe.game.Grid;

import java.util.Objects;

/**
 *
 */
public class TextGridWidget {
    private final static String TicTacToeTextRepresentationTemplate =
                    "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   ";
    private Grid grid;

    static class Cursor {
        private Grid.Location location;
        private static final int[] textLocationsX = {1, 5, 9};
        private static final int[] textLocationsY = {0, 2, 4};
        Cursor(Grid.Location location) {

            setLocation(location);
        }

        Cursor() {
            this(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
        }


        void setLocation(Grid.Location location) {
            this.location = location;
        }

        void moveUp() {
            Grid.Row newRow = getLocation().getRow().previousOrFlip();
            setLocation(Grid.Location.of(newRow, getLocation().getColumn()));
        }


        void moveDown() {
            Grid.Row newRow = getLocation().getRow().nextOrFlip();
            setLocation(Grid.Location.of(newRow, getLocation().getColumn()));
        }

        void moveLeft() {
            Grid.Column newCol = getLocation().getColumn().previousOrFlip();
            setLocation(Grid.Location.of(getLocation().getRow(), newCol));
        }

        void moveRight() {
            Grid.Column newCol = getLocation().getColumn().nextOrFlip();
            setLocation(Grid.Location.of(getLocation().getRow(), newCol));
        }

        Grid.Location getLocation() {
            return location;
        }
        int getTextPositionX() {
            return textLocationsX[location.getColumn().ordinal()];
        }

        int getTextPositionY() {
            return textLocationsY[location.getRow().ordinal()];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cursor cursor = (Cursor) o;

            return Objects.equals(location, cursor.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
        }
    }

    public TextGridWidget() {

    }

    public void setModel(Grid grid) {
        this.grid = grid;
    }

    public String createTextRepresentation() {
        StringBuilder textRepresentation = new StringBuilder(TicTacToeTextRepresentationTemplate);

        return accumulate(textRepresentation, grid.getCells(), replacer()).toString() ;
    }

    private BinaryFunction<StringBuilder, Grid.Cell, StringBuilder> replacer() {
        return new BinaryFunction<StringBuilder, Grid.Cell, StringBuilder>() {
            @Override
            public StringBuilder execute(StringBuilder textRepresentation, Grid.Cell cell) {
                return replaceMarkAtLocation(textRepresentation, cell.getLocation(), characterSymbolOf(cell.getMark()));
            }
        };
    }

    private static <TResult, TSource> TResult accumulate(TResult start,
                                                        Iterable<? extends TSource> range,
                                                        BinaryFunction<? super TResult, ? super TSource, TResult> accumulator) {
        for (TSource item : range) {
            start = accumulator.execute(start, item);
        }
        return start;
    }

    private StringBuilder replaceMarkAtLocation(StringBuilder builder, Grid.Location location, char mark) {
        Cursor tmpCursor = new Cursor(location);
        int positionInLinearText = tmpCursor.getTextPositionX() + tmpCursor.getTextPositionY() * 12;
        return builder.replace(positionInLinearText, positionInLinearText + 1, String.valueOf(mark));
    }

    private char characterSymbolOf(Grid.Mark mark) {
        switch (mark) {
            case Cross:
                return 'X';
            case Nought:
                return 'O';
            case None:
                return ' ';
            default:
                throw new IllegalArgumentException(String.format("Wrong mark %s! Must be either %s, %s or %s", mark, Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None));
        }
    }
}
