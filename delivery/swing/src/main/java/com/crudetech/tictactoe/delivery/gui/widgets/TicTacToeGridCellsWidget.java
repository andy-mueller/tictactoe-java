package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.collections.Pair;
import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.GridCells;

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

    public GridCellHit hitTest(Point hitPoint, Coordinates coordinates) {
        Point hitPointInWidgetCoordinates = coordinates.toWidgetCoordinates(this, hitPoint);

        Iterable<Grid.Cell> allCells = model.getGrid().getCells();
        Rectangle[][] hitBoundaries = style.getGridMarkLocations();
        return new GridCellHit(allCells, hitPointInWidgetCoordinates, hitBoundaries);
    }

    @Override
    protected Iterable<CellWidget<?>> subWidgets() {
        return getCells();
    }

    Iterable<CellWidget<?>> getCells() {
        return from(model.getGrid().getCells()).select(toWidget()).select(toCorrectCoordinates());
    }

    private UnaryFunction<Grid.Cell, Pair<CellWidget<?>, Grid.Cell>> toWidget() {
        return new UnaryFunction<Grid.Cell, Pair<CellWidget<?>, Grid.Cell>>() {
            @Override
            public Pair<CellWidget<?>, Grid.Cell> execute(Grid.Cell cell) {
                return new Pair<CellWidget<?>, Grid.Cell>(createCellWidgetForMark(cell), cell);
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

    private UnaryFunction<Pair<CellWidget<?>, Grid.Cell>, CellWidget<?>> toCorrectCoordinates() {
        return new UnaryFunction<Pair<CellWidget<?>, Grid.Cell>, CellWidget<?>>() {
            @Override
            public CellWidget<?> execute(Pair<CellWidget<?>, Grid.Cell> cellWidget) {
                Rectangle cellBoundary = GridCells.getAtLocation(style.getGridMarkLocations(), cellWidget.getSecond().getLocation());
                cellWidget.getFirst().widgetCoordinates().setLocation(cellBoundary.getLocation());
                return cellWidget.getFirst();
            }
        };
    }
}
