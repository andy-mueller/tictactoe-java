package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.GridCells;


public class TicTacToeGridHighlightedCellWidget extends DecoratorTemplateWidget {
    private final HighlightState state;
    private final Style style;
    private final CoordinateSystem ecs = CoordinateSystem.world();


    public interface HighlightState {
        boolean isHighlighted();

        Grid.Location getLocation();
    }

    public TicTacToeGridHighlightedCellWidget(HighlightState state, Style style) {
        this.state = state;
        this.style = style;
    }

    @Override
    public CoordinateSystem widgetCoordinates() {
        return ecs;
    }

    @Override
    public void paint(GraphicsStream pipe) {
        pipe.pushCoordinateSystem(ecs);
        try {
            super.paint(pipe);
        } finally {
            pipe.popAlpha();
        }
    }

    @Override
    Widget getDecorated() {
        return state.isHighlighted()
                ? makeRectangleWidget()
                : makeEmptyWidget();
    }

    private Widget makeEmptyWidget() {
        return new EmptyWidget();
    }

    private Widget makeRectangleWidget() {
        Rectangle bound = GridCells.getAtLocation(style.getGridMarkLocations(), state.getLocation());
        return new RectangleWidget(bound, style.getHighlightColor());
    }

    @Override
    public String toString() {
        return "TicTacToeGridHighlightedCellWidget{" +
                "decorated=" + getDecorated() +
                ", isHighlighted=" + state.isHighlighted() +
                ", location=" + state.getLocation() +
                ", style=" + style +
                '}';
    }
}
