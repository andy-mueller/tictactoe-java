package com.crudetech.tictactoe.client.jcurses;


import com.crudetech.event.Event;
import com.crudetech.event.EventObject;
import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.UiView;
import jcurses.system.InputChar;
import jcurses.widgets.TextComponent;

import java.util.Objects;

import static com.crudetech.matcher.Verify.verifyThat;
import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


class GridWidget extends TextComponent implements UiView {
    private final static String TicTacToeTextRepresentationTemplate =
            "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   ";

    private final Cursor cursor;

    GridWidget(Cursor cursor) {
        super(TicTacToeTextRepresentationTemplate);
        this.cursor = cursor;
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
    }

    GridWidget() {
        this(new Cursor());
    }

    @Override
    public void setGrid(Grid grid) {
        verifyThat(grid, is(notNullValue()));

        String gridAsText = buildTextRepresentation(grid);
        setText(gridAsText);
        doRepaint();
    }

    private String buildTextRepresentation(Grid grid) {
        StringBuilder textRepresentation = new StringBuilder(TicTacToeTextRepresentationTemplate);

        for (Grid.Cell cell : grid.getCells()) {
            textRepresentation = replaceAtLocation(textRepresentation, cell.getLocation(), characterSymbolOf(cell.getMark()));
        }
        return textRepresentation.toString();
    }

        private static StringBuilder replaceAtLocation(StringBuilder builder, Grid.Location location, char newValue) {
            Cursor tmpCursor = new Cursor(location);
            int positionInLinearText = tmpCursor.getTextPositionX() + tmpCursor.getTextPositionY() * 12;
            return builder.replace(positionInLinearText, positionInLinearText + 1, String.valueOf(newValue));
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

    void moveCursorToFirstMarkedCell(Grid grid) {
        if(currentCursorPositionIsOnMarkedCell(grid))                 {
            return;
        }

        Grid.Location cursorLocation =
                from(grid.getCells())
                        .where(markIsEqualTo(Grid.Mark.None))
                        .select(location())
                        .firstOr(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        cursor.setLocation(cursorLocation);
    }

        private boolean currentCursorPositionIsOnMarkedCell(Grid grid) {
            return grid.getAt(cursor.getLocation()).equals(Grid.Mark.None);
        }

    @Override
    public void highlight(Grid.Triple triple) {
        StringBuilder textRepresentation = new StringBuilder(getText());

        for (Grid.Location location : triple.getLocations()) {
            textRepresentation = replaceAtLocation(textRepresentation, location, '#');
        }

        setText(textRepresentation.toString());
        repaint();
    }

    static class KeyDownEventObject extends EventObject<GridWidget> {
        private final char character;
        private final Grid.Location location;

        public KeyDownEventObject(GridWidget gridWidget, char character, Grid.Location location) {
            super(gridWidget);
            verifyThat(location, is(notNullValue()));

            this.character = character;
            this.location = location;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            KeyDownEventObject that = (KeyDownEventObject) o;

            return character == that.character
                    && location.equals(that.location);

        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), (int) character, location);
        }

        @Override
        public String toString() {
            return "KeyDownEventObject{" +
                    "source=" + getSource() +
                    "character=" + character +
                    ", location=" + location +
                    '}';
        }

        Grid.Location getLocation() {
            return location;
        }
    }

    private final EventSupport<KeyDownEventObject> keyDownEvent = new EventSupport<>();

    Event<KeyDownEventObject> keyDownEvent() {
        return keyDownEvent;
    }

    static class Cursor {
        private Grid.Location location;

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

        private static final int[] textLocationsX = {1, 5, 9};
        private static final int[] textLocationsY = {0, 2, 4};

        void setOn(TextComponent textWidget) {
            textWidget.setCursorLocation(getTextPositionX(), getTextPositionY());
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

    @Override
    protected boolean handleInput(InputChar ch) {
        if (ch.isSpecialCode()) {
            dispatchSpecialKeyInput(ch.getCode());
        } else {
            keyDownEvent.fireEvent(new KeyDownEventObject(this, ch.getCharacter(), cursor.getLocation()));
        }
        invalidate();
        return true;
    }

        private void invalidate() {
            doRepaint();
        }

    private void dispatchSpecialKeyInput(int keyCode) {
            if (keyCode == InputChar.KEY_DOWN) {
                cursor.moveDown();
            } else if (keyCode == InputChar.KEY_UP) {
                cursor.moveUp();
            } else if (keyCode == InputChar.KEY_LEFT) {
                cursor.moveLeft();
            } else if (keyCode == InputChar.KEY_RIGHT) {
                cursor.moveRight();
            }
        }

    @Override
    protected void doPaint() {
        cursor.setOn(this);
        super.doPaint();
    }
}
