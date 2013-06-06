package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.GridCells;

import static com.crudetech.query.Query.from;

public class TicTacToeGridCellsWidget extends CompoundWidget {
    private final TicTacToeGridModel model;
    private final Style style;
    private static final AlphaValue alphaValue = new AlphaValue(0.4f);


    public static abstract class CellWidget<TWidget extends Widget> extends DecoratorWidget<TWidget> {
        private final Grid.Cell model;

        private CellWidget(TWidget decorated, Grid.Location location, TicTacToeGridModel model) {
            super(decorated);
            this.model = model.getGrid().getCellAt(location);
        }

        public abstract boolean isTransparent();

        public abstract boolean hasImage(Image image);
    }

    public static class CellImageWidget extends CellWidget<StatefulTransparencyImageWidget> {
        public CellImageWidget(Grid.Location location, TicTacToeGridModel model, Image image) {
            super(
                    new StatefulTransparencyImageWidget(
                            new ThreeInARowHighlightTransparencyState(model, location, alphaValue),
                            image),
                    location, model
            );
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

    public static class Cross extends CellImageWidget {
        public Cross(Grid.Location location, TicTacToeGridModel model, Style style) {
            super(location, model, style.getCrossImage());
        }
    }

    public static class Nought extends CellImageWidget {
        public Nought(Grid.Location location, TicTacToeGridModel model, Style style) {
            super(location, model, style.getNoughtImage());
        }
    }

    public static class None extends CellWidget<Widget> {
        None(Grid.Location location, TicTacToeGridModel model) {
            super(new EmptyWidget(), location, model);
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
                return new Cross(cell.getLocation(), model, style);
            case Nought:
                return new Nought(cell.getLocation(), model, style);
            case None:
                return new None(cell.getLocation(), model);
            default:
                throw new IllegalStateException("Unexpected cell mark");
        }
    }

    private UnaryFunction<CellWidget<?>, CellWidget<?>> toCorrectCoordinates() {
        return new UnaryFunction<CellWidget<?>, CellWidget<?>>() {
            @Override
            public CellWidget<?> execute(CellWidget<?> cellWidget) {
                Rectangle cellBoundary = GridCells.getAtLocation(style.getGridMarkLocations(), cellWidget.model.getLocation());
                cellWidget.widgetCoordinates().setLocation(cellBoundary.getLocation());
                return cellWidget;
            }
        };
    }
}
