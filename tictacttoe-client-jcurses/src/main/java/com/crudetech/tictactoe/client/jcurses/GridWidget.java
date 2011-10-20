package com.crudetech.tictactoe.client.jcurses;


import com.crudetech.tictactoe.game.Enums;
import com.crudetech.tictactoe.game.Grid;
import jcurses.system.InputChar;
import jcurses.widgets.TextComponent;


public class GridWidget extends TextComponent {
    private final static String TicTacToe =
            " O |   | X " + "\n" +
                    "---+---+---" + "\n" +
                    " X | O | O " + "\n" +
                    "---+---+---" + "\n" +
                    " X | O |   ";
    //  01234567890      2

    private final Cursor cursor;

    GridWidget(Cursor cursor) {
        super(TicTacToe);
        this.cursor = cursor;
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
    }

    static class Cursor {
        private Grid.Location location;


        public void setLocation(Grid.Location location) {
            this.location = location;
        }

        public void moveDown() {
            int currentRow = getLocation().getRow().ordinal();
            int row = currentRow < Grid.Row.Third.ordinal() ? currentRow + 1 : 0;
            Grid.Row newRow = Enums.ofOrdinal(Grid.Row.class, row);
            setLocation(Grid.Location.of(newRow, getLocation().getColumn()));
        }

        public void moveUp() {
            int currentRow = getLocation().getRow().ordinal();
            int row = currentRow > 0 ? currentRow - 1 : Grid.Row.Third.ordinal();
            Grid.Row newRow = Enums.ofOrdinal(Grid.Row.class, row);
            setLocation(Grid.Location.of(newRow, getLocation().getColumn()));
        }

        public void moveLeft() {
            int col = getLocation().getColumn().ordinal();
            int newColumn = col > 0 ? col - 1 : Grid.Column.Third.ordinal();
            Grid.Column newCol = Enums.ofOrdinal(Grid.Column.class, newColumn);
            setLocation(Grid.Location.of(getLocation().getRow(), newCol));
        }

        public void moveRight() {
            int col = getLocation().getColumn().ordinal();
            int newColumn = col < Grid.Column.Third.ordinal() ? col + 1 : 0;
            Grid.Column newCol = Enums.ofOrdinal(Grid.Column.class, newColumn);
            setLocation(Grid.Location.of(getLocation().getRow(), newCol));
        }

        public Grid.Location getLocation() {
            return location;
        }

        private static final int[] textLocationsX = {1, 5, 9};
        private static final int[] textLocationsY = {0, 2, 4};

        public void setOn(TextComponent textWidget) {
            textWidget.setCursorLocation(textLocationsX[location.getRow().ordinal()], textLocationsY[location.getColumn().ordinal()]);
        }
    }

    @Override
    protected boolean handleInput(InputChar ch) {

        if (ch.getCode() == InputChar.KEY_DOWN) {
            cursor.moveDown();
        } else if (ch.getCode() == InputChar.KEY_UP) {
            cursor.moveUp();
        } else if (ch.getCode() == InputChar.KEY_LEFT) {
            cursor.moveLeft();
        } else if (ch.getCode() == InputChar.KEY_RIGHT) {
            cursor.moveRight();
        }

        doRepaint();
        return true;
    }
}
