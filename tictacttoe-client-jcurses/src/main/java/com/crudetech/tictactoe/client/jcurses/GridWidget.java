package com.crudetech.tictactoe.client.jcurses;


import com.crudetech.event.Event;
import com.crudetech.event.EventObject;
import com.crudetech.event.EventSupport;
import com.crudetech.functional.UnaryFunction;
import com.crudetech.tictactoe.game.Grid;
import jcurses.system.InputChar;
import jcurses.widgets.TextComponent;

import java.util.Objects;

import static com.crudetech.matcher.Verify.verifyThat;
import static com.crudetech.query.Query.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


class GridWidget extends TextComponent implements GridWidgetView {
    private final static String TicTacToe =
            "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   ";

    private final Cursor cursor;

    GridWidget(Cursor cursor) {
        super(TicTacToe);
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
        StringBuilder b = new StringBuilder(getText());

        for (Grid.Cell cell : grid.getCells()) {
            Cursor tmpCursor = new Cursor(cell.getLocation());
            int positionInLinearText = tmpCursor.getTextPositionX() + tmpCursor.getTextPositionY() * 12;
            b.replace(positionInLinearText, positionInLinearText + 1, String.valueOf(characterSymbolOf(cell.getMark())));
        }
        return b.toString();
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

    @Override
    public void moveCursorToFirstEmptyCell(Grid grid) {

        Grid.Location cursorLocation =
                from(grid.getCells())
                        .where(markIsEqualTo(Grid.Mark.None))
                        .select(location())
                        .firstOr(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        cursor.setLocation(cursorLocation);
    }

    private UnaryFunction<Grid.Cell, Grid.Location> location() {
        return new UnaryFunction<Grid.Cell, Grid.Location>() {
            @Override
            public Grid.Location execute(Grid.Cell cell) {
                return cell.getLocation();
            }
        };
    }

    private UnaryFunction<? super Grid.Cell, Boolean> markIsEqualTo(final Grid.Mark mark) {
        return new UnaryFunction<Grid.Cell, Boolean>() {
            @Override
            public Boolean execute(Grid.Cell cell) {
                return cell.getMark().equals(mark);
            }
        };
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
            int result = super.hashCode();
            result = 31 * result + (int) character;
            result = 31 * result + location.hashCode();
            return result;
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
        doRepaint();
        return true;
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
