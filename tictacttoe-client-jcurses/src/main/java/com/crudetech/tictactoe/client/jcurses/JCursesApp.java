package com.crudetech.tictactoe.client.jcurses;

import jcurses.system.CharColor;
import jcurses.widgets.DefaultLayoutManager;
import jcurses.widgets.WidgetsConstants;
import jcurses.widgets.Window;

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


        GridWidget area = new GridWidget();
        area.setCursorColors(new CharColor(CharColor.RED, CharColor.WHITE));
        mgr.addWidget(area, 1, 1, 12, 5, WidgetsConstants.ALIGNMENT_TOP, WidgetsConstants.ALIGNMENT_CENTER);
        w.show();
    }
}
