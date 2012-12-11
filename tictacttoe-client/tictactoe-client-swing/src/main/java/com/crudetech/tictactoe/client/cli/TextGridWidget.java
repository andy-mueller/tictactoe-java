package com.crudetech.tictactoe.client.cli;

import com.crudetech.functional.BinaryFunction;
import com.crudetech.tictactoe.game.Grid;

import java.io.PrintWriter;
import java.util.Objects;

import static com.crudetech.collections.Iterables.accumulate;

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
    private static final char HighlightSymbol = '#';
    private Grid grid;
    private Grid.Triple triple = Grid.Triple.Empty;

    static class Cursor {
        private static final int[] textLocationsX = {1, 5, 9};
        //   O |   | X    <0
        //  ---+---+---    1
        //   X | O | O    <2
        //  ---+---+---    3
        //   X | O |      <4
        //   ^   ^   ^
        //  01234567890
        private Grid.Location location;
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

    public void highlight(Grid.Triple triple) {
        this.triple = triple;
    }

    public void render(PrintWriter pipe) {
        pipe.print(createTextRepresentation());
    }

    private String createTextRepresentation() {
        StringBuilder textRepresentation = applyGrid();
        textRepresentation = applyCellContent(textRepresentation);
        textRepresentation = applyHighlighting(textRepresentation);
        return textRepresentation.toString();
    }

    private StringBuilder applyGrid() {
        return new StringBuilder(TicTacToeTextRepresentationTemplate);
    }

    private StringBuilder applyCellContent(StringBuilder textRepresentation) {
        return accumulate(textRepresentation, grid.getCells(), withSymbols());
    }

    private StringBuilder applyHighlighting(StringBuilder textRepresentation) {
        if (!triple.equals(Grid.Triple.Empty)) {
            textRepresentation = accumulate(textRepresentation, triple.getLocations(), highlightSymbol());
        }
        return textRepresentation;
    }

    private BinaryFunction<? super StringBuilder, ? super Grid.Location, ? extends StringBuilder> highlightSymbol() {
        return new BinaryFunction<StringBuilder, Grid.Location, StringBuilder>() {
            @Override
            public StringBuilder execute(StringBuilder textRepresentation, Grid.Location tripleLocation) {
                return replaceMarkAtLocation(textRepresentation, tripleLocation, HighlightSymbol);
            }
        };
    }

    private BinaryFunction<StringBuilder, Grid.Cell, StringBuilder> withSymbols() {
        return new BinaryFunction<StringBuilder, Grid.Cell, StringBuilder>() {
            @Override
            public StringBuilder execute(StringBuilder textRepresentation, Grid.Cell cell) {
                return replaceMarkAtLocation(textRepresentation, cell.getLocation(), characterSymbolOf(cell.getMark()));
            }
        };
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
