package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;

import static com.crudetech.query.Query.from;

public class TicTacToeGridCellsWidget extends CompoundWidget {
    private final TicTacToeGridModel model;
    private final Style style;
    private static final AlphaValue alphaValue = new AlphaValue(0.4f);


    public static abstract class CellWidget<TWidget extends Widget> extends DecoratorWidget<TWidget> {
        private final Rectangle boundary;
        private final Grid.Location location;

        private CellWidget(TWidget decorated, Grid.Location location, Style style) {
            super(decorated);
            this.location = location;
            Rectangle boundaryInWorld = style.getGridMarkBoundary(location);
            widgetCoordinates().setLocation(boundaryInWorld.getLocation());
            this.boundary = widgetCoordinates().toWidgetCoordinates(boundaryInWorld);
        }

        public abstract boolean isTransparent();

        public abstract boolean hasImage(Image image);

        public GridCellHit hitTest(final Point hitPoint, final Coordinates coordinates) {
            Point hitPointInCellCoos = coordinates.toWidgetCoordinates(CellWidget.this, hitPoint);
            return new GridCellHit(location, boundary, hitPointInCellCoos);
        }
    }

    public static class CellImageWidget extends CellWidget<StatefulTransparencyImageWidget> {
        public CellImageWidget(Grid.Location location, TicTacToeGridModel model, Image image, Style style) {
            super(
                    new StatefulTransparencyImageWidget(
                            new ThreeInARowHighlightTransparencyState(model, location, alphaValue),
                            image),
                    location, style
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
            super(location, model, style.getCrossImage(), style);
        }
    }

    public static class Nought extends CellImageWidget {
        public Nought(Grid.Location location, TicTacToeGridModel model, Style style) {
            super(location, model, style.getNoughtImage(), style);
        }
    }

    public static class None extends CellWidget<Widget> {
        None(Grid.Location location, Style style) {
            super(new EmptyWidget(), location, style);
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

        return from(getCells()).select(toGridCellHit(hitPointInWidgetCoordinates, Coordinates.World)).where(isHit()).firstOr(GridCellHit.NoHit);
    }

    private UnaryFunction<GridCellHit, Boolean> isHit() {
        return new UnaryFunction<GridCellHit, Boolean>() {
            @Override
            public Boolean execute(GridCellHit gridCellHit) {
                return gridCellHit.hasHit();
            }
        };
    }

    private UnaryFunction<CellWidget<?>, GridCellHit> toGridCellHit(final Point hitPoint, final Coordinates coordinates) {
        return new UnaryFunction<CellWidget<?>, GridCellHit>() {
            @Override
            public GridCellHit execute(CellWidget<?> cellWidget) {
                return cellWidget.hitTest(hitPoint, coordinates);
            }
        };
    }


    @Override
    protected Iterable<CellWidget<?>> subWidgets() {
        return getCells();
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
        Grid.Location location = cell.getLocation();
        switch (cell.getMark()) {
            case Cross:
                return new Cross(location, model, style);
            case Nought:
                return new Nought(location, model, style);
            case None:
                return new None(location, style);
            default:
                throw new IllegalStateException("Unexpected cell mark");
        }
    }
}
