package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;


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
    public CoordinateSystem coordinateSystem() {
        return ecs;
    }

    @Override
    public void paint(GraphicsStream pipe) {
        try (GraphicsStream.Context ctx = pipe.newContext()){
            ctx.pushCoordinateSystem(ecs);
            super.paint(pipe);
        }
    }

    @Override
    protected Widget getDecorated() {
        return state.isHighlighted()
                ? makeRectangleWidget()
                : makeEmptyWidget();
    }

    private Widget makeEmptyWidget() {
        return new EmptyWidget();
    }

    private Widget makeRectangleWidget() {
        Rectangle bound = style.getGridMarkBoundary(state.getLocation());
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
