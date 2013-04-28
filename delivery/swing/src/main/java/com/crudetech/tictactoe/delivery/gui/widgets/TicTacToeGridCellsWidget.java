package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.CompoundWidget;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import sun.plugin.dom.exception.InvalidStateException;

import static com.crudetech.collections.Iterables.emptyListOf;
import static com.crudetech.query.Query.from;

public class TicTacToeGridCellsWidget extends CompoundWidget {
    private final TicTacToeGridModel model;
    private final Style style;
    private final AlphaValue alphaValue = new AlphaValue(0.4f);

    public static class Cross extends StatefulTransparencyImageWidget {
        public Cross(Style style, TransparencyState state) {
            super(state, style.getCrossImage());
        }

    }

    public static class None extends EmptyWidget {
    }

    public static class Nought extends StatefulTransparencyImageWidget {
        public Nought(Style style, TransparencyState state) {
            super(state, style.getNoughtImage());
        }
    }

    static class ThreeInARowHighlightTransparencyState implements StatefulTransparencyImageWidget.TransparencyState {
        private final TicTacToeGridModel model;
        private final Grid.Location location;
        private final AlphaValue alphaValue;

        public ThreeInARowHighlightTransparencyState(TicTacToeGridModel model, Grid.Location location, AlphaValue alphaValue) {
            this.model = model;
            this.location = location;
            this.alphaValue = alphaValue;
        }

        @Override
        public boolean isTransparent() {
            return model.hasHighlightedThreeInARow()
                    && !model.getHighlightedThreeInARow().containsLocation(location);
        }

        @Override
        public AlphaValue transparency() {
            return alphaValue;
        }
    }

    public TicTacToeGridCellsWidget(TicTacToeGridModel model, Style style) {
        this.model = model;
        this.style = style;
    }

    @Override
    protected Iterable<Widget> subWidgets() {
        return emptyListOf(Widget.class);
    }

    Iterable<Widget> getCells() {
        return from(model.getGrid().getCells()).select(toWidget());
    }

    private UnaryFunction<Grid.Cell, Widget> toWidget() {
        return new UnaryFunction<Grid.Cell, Widget>() {
            @Override
            public Widget execute(Grid.Cell cell) {
                return createCellWidgetForMark(cell);
            }
        };
    }

    private Widget createCellWidgetForMark(Grid.Cell cell) {
        switch (cell.getMark()) {
            case Cross:
                return new Cross(style, createTransparencyState(cell));
            case Nought:
                return new Nought(style, createTransparencyState(cell));
            case None:
                return new None();
            default:
                throw new InvalidStateException("Unexpected cell mark");
        }
    }

    private ThreeInARowHighlightTransparencyState createTransparencyState(Grid.Cell cell) {
        return new ThreeInARowHighlightTransparencyState(model, cell.getLocation(), alphaValue);
    }


}
