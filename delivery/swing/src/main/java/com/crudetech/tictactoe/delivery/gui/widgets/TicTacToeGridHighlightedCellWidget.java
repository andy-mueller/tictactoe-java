package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.GridCells;


public class TicTacToeGridHighlightedCellWidget extends DecoratorTemplateWidget {
    private final HighlightState state;
    private final Style style;
    private final Grid.Location location;

    public interface HighlightState {
    }

    public TicTacToeGridHighlightedCellWidget(HighlightState state, Style style, Grid.Location location) {
        this.state = state;
        this.style = style;
        this.location = location;
    }

    @Override
    Widget getDecorated() {
        Rectangle bound = GridCells.getAtLocation(style.getGridMarkLocations(), location);
        return new RectangleWidget(bound, style.getHighlightColor());
    }
}
