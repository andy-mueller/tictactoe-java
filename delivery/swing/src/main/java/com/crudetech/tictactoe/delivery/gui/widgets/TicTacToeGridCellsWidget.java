package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.CompoundWidget;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;

import static com.crudetech.collections.Iterables.emptyListOf;
import static com.crudetech.query.Query.from;

public class TicTacToeGridCellsWidget extends CompoundWidget {
    private final TicTacToeGridModel model;
    private final Style style;
    private final AlphaValue alphaValue = new AlphaValue(0.4f);

    public static abstract class CellWidget<TWidget extends Widget> extends DecoratorWidget<TWidget> {
        private CellWidget(TWidget decorated) {
            super(decorated);
        }

        public abstract boolean isTransparent();

        public abstract boolean hasImage(Image image);
    }

    public static class CellImageWidget extends CellWidget<StatefulTransparencyImageWidget> {
        public CellImageWidget(Image image, StatefulTransparencyImageWidget.TransparencyState state) {
            super(new StatefulTransparencyImageWidget(state, image));
        }

        @Override
        public boolean isTransparent() {
            return getDecorated().isTransparent();
        }

        @Override
        public boolean hasImage(Image image) {
            return getDecorated().hasImage(image);
        }
    }

    public static class Cross extends CellImageWidget {
        public Cross(Style style, StatefulTransparencyImageWidget.TransparencyState state) {
            super(style.getCrossImage(), state);
        }
    }

    public static class None extends CellWidget<Widget> {
        None() {
            super(new EmptyWidget());
        }

        @Override
        public boolean isTransparent() {
            return true;
        }

        @Override
        public boolean hasImage(Image image) {
            return false;
        }
    }

    public static class Nought extends CellImageWidget {
        public Nought(Style style, StatefulTransparencyImageWidget.TransparencyState state) {
            super(style.getNoughtImage(), state);
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

    Iterable<CellWidget<?>> getCells() {
        return from(model.getGrid().getCells()).select(toWidget());
    }

    private UnaryFunction<Grid.Cell, CellWidget<?>> toWidget() {
        return new UnaryFunction<Grid.Cell, CellWidget<?>>() {
            @Override
            public CellWidget<?> execute(Grid.Cell cell) {
                return createCellWidgetForMark(cell);
            }
        };
    }

    private CellWidget<?> createCellWidgetForMark(Grid.Cell cell) {
        switch (cell.getMark()) {
            case Cross:
                return new Cross(style, createTransparencyState(cell));
            case Nought:
                return new Nought(style, createTransparencyState(cell));
            case None:
                return new None();
            default:
                throw new IllegalStateException("Unexpected cell mark");
        }
    }

    private ThreeInARowHighlightTransparencyState createTransparencyState(Grid.Cell cell) {
        return new ThreeInARowHighlightTransparencyState(model, cell.getLocation(), alphaValue);
    }
}
