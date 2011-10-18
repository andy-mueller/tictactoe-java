package com.crudetech.tictactoe.client.jcurses;

import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.widgets.DefaultLayoutManager;
import jcurses.widgets.TextComponent;
import jcurses.widgets.WidgetsConstants;
import jcurses.widgets.Window;

import static jcurses.system.InputChar.*;

public class JCursesApp {

    private final static String TicTacToe =

                    " O |   | X " + "\n" +
                    "---+---+---" + "\n" +
                    " X | O | O " + "\n" +
                    "---+---+---" + "\n" +
                    " X | O |   ";
                 //  01234567890      2
    public static void main(String[] args) {


        Window w = new Window(50, 30, true, "Tic Tac Toe");
        final DefaultLayoutManager mgr = new DefaultLayoutManager();
        mgr.bindToContainer(w.getRootPanel());


        final TextComponent area = new TextComponent(TicTacToe) {
            private int column = 0;
            private int row = 0;

            @Override
            protected boolean handleInput(InputChar ch) {
                if (ch.isSpecialCode()) {
                    if (ch.getCode() == KEY_RIGHT) {
                        moveRight();
                    }
                    if (ch.getCode() == KEY_LEFT) {
                        moveLeft();
                    }
                    if (ch.getCode() == KEY_DOWN) {
                        moveDown();
                    }
                    if (ch.getCode() == KEY_UP) {
                        moveUp();
                    }
                } else {
                    StringBuilder b = new StringBuilder(getText());
                    int pos = cursorPosX() + (cursorPosY()) * 12;
                    b.replace(pos, pos + 1, String.valueOf(ch.getCharacter()));
                    setText(b.toString());
                    setCursorPos();
                }
                doRepaint();
                return true;
            }

            private void setCursorPos() {
                int cursorPosX = cursorPosX();
                int cursorPosY = cursorPosY();
                setCursorLocation(cursorPosX, cursorPosY);
            }

            private final int[] yPositions = {0, 2, 4};

            private int cursorPosY() {
                return yPositions[row];
//                return row == 0 ? 0 : row == 1 ? 2 : 4;
            }

            private final int[] xPositions = {1, 5, 9};

            private int cursorPosX() {
                return xPositions[column];
//                return column == 0 ? 1 : column == 1 ? 5 : 9;
            }

            private void moveRight() {
                column = column >= 2 ? 0 : column + 1;
                setCursorPos();
            }

            private void moveLeft() {
                column = column == 0 ? 2 : column - 1;
                setCursorPos();
            }

            private void moveDown() {
                row = row >= 2 ? 0 : row + 1;
                setCursorPos();
            }

            private void moveUp() {
                row = row == 0 ? 2 : row - 1;
                setCursorPos();
            }


        };
        area.setCursorColors(new CharColor(CharColor.RED, CharColor.WHITE));
        mgr.addWidget(area, 1, 1, 12, 5, WidgetsConstants.ALIGNMENT_TOP, WidgetsConstants.ALIGNMENT_CENTER);
        w.show();
    }
}
