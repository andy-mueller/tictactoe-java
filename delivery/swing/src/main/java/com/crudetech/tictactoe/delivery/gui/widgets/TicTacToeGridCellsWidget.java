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

        private CellWidget(TWidget decorated, Grid.Cell model) {
            super(decorated);
            this.model = model;
        }

        public abstract boolean isTransparent();

        public abstract boolean hasImage(Image image);
    }

    public static class CellImageWidget extends CellWidget<StatefulTransparencyImageWidget> {
        public CellImageWidget(Grid.Cell model, Image image, StatefulTransparencyImageWidget.TransparencyState state) {
            super(new StatefulTransparencyImageWidget(state,  image), model);
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
        public Cross(Grid.Cell model, Style style, StatefulTransparencyImageWidget.TransparencyState state) {
            super(model, style.getCrossImage(), state);
        }
    }

    public static class None extends CellWidget<Widget> {
        None(Grid.Cell model) {
            super(new EmptyWidget(), model);
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
        public Nought(Grid.Cell model, Style style, StatefulTransparencyImageWidget.TransparencyState state) {
            super(model, style.getNoughtImage(), state);
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
                return new Cross(cell, style, createTransparencyStateFor(cell));
            case Nought:
                return new Nought(cell, style, createTransparencyStateFor(cell));
            case None:
                return new None(cell);
            default:
                throw new IllegalStateException("Unexpected cell mark");
        }
    }

    private ThreeInARowHighlightTransparencyState createTransparencyStateFor(Grid.Cell cell) {
        return new ThreeInARowHighlightTransparencyState(model, cell.getLocation(), alphaValue);
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
