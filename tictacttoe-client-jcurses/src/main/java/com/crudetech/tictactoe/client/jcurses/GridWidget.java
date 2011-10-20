package com.crudetech.tictactoe.client.jcurses;


import com.crudetech.event.Event;
import com.crudetech.event.EventObject;
import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;
import jcurses.system.InputChar;
import jcurses.widgets.TextComponent;


class GridWidget extends TextComponent {
    private final static String TicTacToe =
            "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   " + "\n" +
                    "---+---+---" + "\n" +
                    "   |   |   ";
    //  01234567890      2

    private final Cursor cursor;

    GridWidget(Cursor cursor) {
        super(TicTacToe);
        this.cursor = cursor;
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
    }

    public GridWidget() {
        this(new Cursor());
    }

    static class KeyDownEventObject extends EventObject<GridWidget> {
        private final char character;

        public KeyDownEventObject(GridWidget gridWidget, char character) {
            super(gridWidget);
            this.character = character;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            KeyDownEventObject that = (KeyDownEventObject) o;

            return character == that.character;

        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (int) character;
            return result;
        }
    }

    private final EventSupport<KeyDownEventObject> keyDownEvent = new EventSupport<>();

    Event<KeyDownEventObject> keyDownEvent() {
        return keyDownEvent;
    }

    static class Cursor {
        private Grid.Location location;


        void setLocation(Grid.Location location) {
            this.location = location;
        }

        void moveUp() {
            Grid.Row newRow = getLocation().getRow().previousOrFlip();
            setLocation(Grid.Location.of(newRow, getLocation().getColumn()));
            // The following code is commented out and left in here for documentation
            // purposes. This was the first version and drove the decision to
            //  moveDown  it into the Row.previousOrFlip() method, as it
            // relies on implementation details of the row
            //
//                  int currentRow = getLocation().getRow().ordinal();
//                  int row = currentRow > 0 ? currentRow - 1 : Grid.Row.Third.ordinal();
//                  Grid.Row newRow = Enums.ofOrdinal(Grid.Row.class, row);
//                  setLocation(Grid.Location.of(newRow, getLocation().getColumn()));
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
    }

    @Override
    protected boolean handleInput(InputChar ch) {
        if (ch.isSpecialCode()) {
            if (ch.getCode() == InputChar.KEY_DOWN) {
                cursor.moveDown();
            } else if (ch.getCode() == InputChar.KEY_UP) {
                cursor.moveUp();
            } else if (ch.getCode() == InputChar.KEY_LEFT) {
                cursor.moveLeft();
            } else if (ch.getCode() == InputChar.KEY_RIGHT) {
                cursor.moveRight();
            }
        } else {
            keyDownEvent.fireEvent(new KeyDownEventObject(this, ch.getCharacter()));

            StringBuilder b = new StringBuilder(getText());
            int pos = cursor.getTextPositionX() + cursor.getTextPositionY() * 12;
            b.replace(pos, pos + 1, String.valueOf(ch.getCharacter()));
            setText(b.toString());

        }
        doRepaint();
        return true;
    }

    @Override
    protected void doPaint() {
        cursor.setOn(this);
        super.doPaint();
    }
}
