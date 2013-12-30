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
    private final BoundaryAnchor anchor;

    TicTacToeCenteredGridWidget(Rectangle bounds, Style style, TicTacToeGridModel model) {
        this.style = style;
        this.model = model;
        widgets = createSubWidgets();
        anchor = new BoundaryAnchor(this, backgroundImageBoundary());
        anchor.positionWidget(bounds);
    }

    private Rectangle backgroundImageBoundary() {
        Image img = style.getBackgroundImage();
        return new Rectangle(Point.Origin, img.getWidth(), img.getHeight());
    }

    static class BoundaryAnchor {
        private final Rectangle anchoredItemBoundary;
        private final Widget anchoredWidget;

        private double horizontalScale;
        private double verticalScale;
        private double scale;
        private Point location;

        BoundaryAnchor(Widget anchoredWidget, Rectangle anchoredItemBoundary) {
            this.anchoredWidget = anchoredWidget;
            this.anchoredItemBoundary = anchoredItemBoundary;
        }

        public void positionWidget(Rectangle anchoredTargetBounds) {
            computeCoordinateSystem(anchoredTargetBounds);
            applyCoordinateSystemOnAnchoredWidget();
        }

        private void applyCoordinateSystemOnAnchoredWidget() {
            anchoredWidget.setCoordinateSystem(new CoordinateSystem(location, scale));
        }

        private void computeCoordinateSystem(Rectangle anchoredTargetBounds) {
            scale = computeScale(anchoredTargetBounds);
            location = getBackgroundImageOrigin(anchoredTargetBounds);
        }

        private double computeScale(Rectangle anchoredTargetBounds) {
            final double scale = 1.0;

            horizontalScale = horizontalScale(anchoredTargetBounds);
            verticalScale = verticalScale(anchoredTargetBounds);

            return scale * horizontalScale * verticalScale;
        }

        private double horizontalScale(Rectangle anchoredTargetBounds) {
            return anchoredIsWider(anchoredTargetBounds) ? ((double) anchoredTargetBounds.width) / anchoredItemBoundary.width : 1.0;
        }

        private boolean anchoredIsWider(Rectangle anchoredTargetBounds) {
            return anchoredItemBoundary.width > anchoredTargetBounds.width;
        }

        private double verticalScale(Rectangle bounds) {
            final int scaledImageHeight = (int) (anchoredItemBoundary.height * horizontalScale);
            return scaledImageHeight > bounds.height ? ((double) bounds.height) / (anchoredItemBoundary.height * horizontalScale) : 1.0;
        }

        private Point getBackgroundImageOrigin(Rectangle bounds) {
            int x = (int) max((bounds.width - anchoredItemBoundary.width * scale) / 2, 0);
            int y = (int) max((bounds.height - anchoredItemBoundary.height * scale) / 2, 0);
            return Point.of(x, y);
        }
    }

    private List<Widget> createSubWidgets() {
        return asList(
                createBackgroundImageWidget(),
                createGridCellsWidget(),
                createHighlightWidget()
        );
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
