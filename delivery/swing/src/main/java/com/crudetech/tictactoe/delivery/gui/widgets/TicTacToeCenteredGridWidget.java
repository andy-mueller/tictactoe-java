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
        centerWidget(bounds);
    }

    private List<Widget> createSubWidgets() {
        return asList(
                createBackgroundImageWidget(),
                createGridCellsWidget(),
                createHighlightWidget()
        );
    }

    private void centerWidget(Rectangle bounds) {
        WidgetJig jig = new WidgetJig(this);
        jig.setScale(computeScale(bounds));
        jig.setLocation(getBackgroundImageOrigin(bounds));
    }

    private double computeScale(Rectangle bounds) {
        Image background = style.getBackgroundImage();
        final double scale = 1.0;

        final double horizontalScale = horizontalScale(bounds, background);
        final double verticalScale = verticalScale(bounds, background, horizontalScale);

        return scale * horizontalScale * verticalScale;
    }

    private double verticalScale(Rectangle bounds, Image background, double scaleW) {
        final int scaledImageHeight = (int) (background.getHeight() * scaleW);
        return scaledImageHeight > bounds.height ? ((double) bounds.height) / (background.getHeight() * scaleW) : 1.0;
    }

    private double horizontalScale(Rectangle bounds, Image background) {
        return imageIsWider(bounds, background) ? ((double) bounds.width) / background.getWidth() : 1.0;
    }

    private boolean imageIsWider(Rectangle bounds, Image background) {
        return background.getWidth() > bounds.width;
    }

    private Point getBackgroundImageOrigin(Rectangle bounds) {
        final double scale = coordinateSystem().getScale();
        Image backgroundImage = style.getBackgroundImage();
        int x = (int) max((bounds.width - backgroundImage.getWidth() * scale) / 2, 0);
        int y = (int) max((bounds.height - backgroundImage.getHeight() * scale) / 2, 0);
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
