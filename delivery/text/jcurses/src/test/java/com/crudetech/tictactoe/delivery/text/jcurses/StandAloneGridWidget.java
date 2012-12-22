package com.crudetech.tictactoe.delivery.text.jcurses;

import com.crudetech.tictactoe.delivery.text.cli.TextGridWidget;
import jcurses.util.Rectangle;

/**
 * A GridWidget that is disconnected from the jcurses toolkit
 */
class StandAloneGridWidget extends GridWidget {
    private int repaints = 0;

    StandAloneGridWidget(TextGridWidget.Cursor cursor) {
        super(cursor);
    }

    StandAloneGridWidget(TextGridWidget.Cursor cursor, TextGridWidget textWidget) {
        super(cursor, textWidget);
    }

    @Override
    protected void doRepaint() {
        repaints++;
    }

    @Override
    protected void repaint() {
        doRepaint();
    }

    int getRepaints() {
        return repaints;
    }

    @Override
    protected Rectangle getSize() {
        return new Rectangle(0, 0, 1, 1);
    }
}
