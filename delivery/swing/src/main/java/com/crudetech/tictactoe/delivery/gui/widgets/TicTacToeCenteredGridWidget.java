package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;

import java.util.List;

import static com.crudetech.query.Query.from;
import static java.lang.Math.max;
import static java.util.Arrays.asList;


class TicTacToeCenteredGridWidget extends CompoundWidget {
    private final Style style;
    private final TicTacToeGridModel model;
    private final List<Widget> widgets;

    TicTacToeCenteredGridWidget(Rectangle bounds, Style style, TicTacToeGridModel model) {
        this.style = style;
        this.model = model;
        widgets = createSubWidgets();
        coordinateSystem().setLocation(getBackgroundImageOrigin(bounds));
    }

    private List<Widget> createSubWidgets() {
        return asList(
                createBackgroundImageWidget(),
                createGridCellsWidget(),
                createHighlightWidget()
        );
    }

    private Point getBackgroundImageOrigin(Rectangle bounds) {
        Image backgroundImage = style.getBackgroundImage();
        int x = max((bounds.width - backgroundImage.getWidth()) / 2, 0);
        int y = max((bounds.height - backgroundImage.getHeight()) / 2, 0);
        return Point.of(x, y);
    }

    @Override
    protected Iterable<? extends Widget> subWidgets() {
        return widgets;
    }

    private StatefulTransparencyImageWidget.TransparencyState backgroundImageTransparencyState() {
        return new StatefulTransparencyImageWidget.TransparencyState() {
            @Override
            public boolean isTransparent() {
                return model.hasHighlightedThreeInARow();
            }

            @Override
            public AlphaValue transparency() {
                return TicTacToeGridWidget.WinningTripleAlpha;
            }
        };
    }

    private Widget createBackgroundImageWidget() {
        Image backgroundImage = style.getBackgroundImage();


        StatefulTransparencyImageWidget.TransparencyState state =
                backgroundImageTransparencyState();

        return new StatefulTransparencyImageWidget(state, backgroundImage);
    }

    private TicTacToeGridCellsWidget createGridCellsWidget() {
        return new TicTacToeGridCellsWidget(model, style);
    }

    private Widget createHighlightWidget() {
        TicTacToeGridHighlightedCellWidget.HighlightState state = highlightWidgetState();

        return new TicTacToeGridHighlightedCellWidget(state, style);
    }

    private TicTacToeGridHighlightedCellWidget.HighlightState highlightWidgetState() {
        return new TicTacToeGridHighlightedCellWidget.HighlightState() {
            @Override
            public boolean isHighlighted() {
                return model.hasHighlightedCell();
            }

            @Override
            public Grid.Location getLocation() {
                return model.getHighlightedCell();
            }
        };
    }

    public GridCellHit hitTest(Point hitPoint, Coordinates coordinates) {
        Point hitInEcs = coordinates.toWidgetCoordinates(this, hitPoint);
        return getGridCellsWidget().hitTest(hitInEcs, Coordinates.World);
    }

    public Iterable<Rectangle> getCellBoundaries(Iterable<Grid.Location> changedCells) {
        return from(changedCells).select(toBoundaryRectangle()).select(toWorldCoordinates());
    }

    private UnaryFunction<Grid.Location, Rectangle> toBoundaryRectangle() {
        return new UnaryFunction<Grid.Location, Rectangle>() {
            @Override
            public Rectangle execute(Grid.Location location) {
                return style.getGridMarkBoundary(location);
            }
        };
    }

    private UnaryFunction<Rectangle, Rectangle> toWorldCoordinates() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle rectangle) {
                return coordinateSystem().toWorldCoordinates(rectangle);
            }
        };
    }

    private TicTacToeGridCellsWidget getGridCellsWidget() {
        return (TicTacToeGridCellsWidget) widgets.get(1);
    }
}
