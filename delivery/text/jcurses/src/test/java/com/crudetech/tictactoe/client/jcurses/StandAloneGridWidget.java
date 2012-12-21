package com.crudetech.tictactoe.client.jcurses;

/**
 * A GridWidget that is disconnected from the jcurses toolkit
 */
class StandAloneGridWidget extends GridWidget {
    private int repaints = 0;

    StandAloneGridWidget(Cursor cursor) {
        super(cursor);
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
}
