package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.collections.Iterables;
import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.EcsWidget;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.max;

public class TicTacToeGridWidget extends EcsWidget {
    private final Rectangle bounds;
    private final Style style;
    private final Grid.ThreeInARow threeInARow;
    private final Iterable<Grid.Cell> cells;
    private final Grid.Location highlightedCell;
    private final boolean isDebugMode;
    private final Color debugColor;

    static final AlphaValue WinningTripleAlpha = new AlphaValue(0.4f);


    public TicTacToeGridWidget(Rectangle bounds,
                               Style style,
                               Grid.ThreeInARow threeInARow,
                               Iterable<Grid.Cell> cells,
                               Grid.Location highlightedCell,
                               boolean debugMode, Color debugColor) {
        this.bounds = bounds;
        this.style = style;
        this.threeInARow = threeInARow;
        this.cells = cells;
        this.highlightedCell = highlightedCell;
        this.isDebugMode = debugMode;
        this.debugColor = debugColor;
    }

    @Override
    public void paintEcs(GraphicsStream pipe) {
        for (Widget widget : buildPaintList()) {
            widget.paint(pipe);
        }

    }


    public List<Widget> buildPaintList() {
        java.util.List<Widget> paintList = new ArrayList<>();

        paintList.add(backgroundWidget());
        paintList.add(backgroundImageWidget());
        paintList.addAll(gridMarkWidgetList());
        paintList.add(highlightWidget());
        paintList.add(debugInfoWidget());


        return paintList;
    }

    private Widget backgroundWidget() {
        Rectangle boundary = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height + 500);
        return new FilledRectangleWidget(boundary, style.getBackgroundColor());
    }

    private Widget backgroundImageWidget() {
        Image backgroundImage = style.getBackgroundImage();

        Point p = getUiOrigin();
        ImageWidget imageWidget = new ImageWidget(p.x, p.y, backgroundImage);

        return hasHighlightedTriple() ? new CompositeDecoratorWidget(imageWidget, WinningTripleAlpha) : imageWidget;
    }

    private boolean hasHighlightedTriple() {
        return !threeInARow.equals(Grid.ThreeInARow.Empty);
    }

    public Point getUiOrigin() {
        Image backgroundImage = style.getBackgroundImage();
        int x = max((bounds.width - backgroundImage.getWidth()) / 2, 0);
        int y = max((bounds.height - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }

    public java.util.List<Widget> gridMarkWidgetList() {
        Point gridOrigin = getUiOrigin();
        java.util.List<Widget> gridMArks = new ArrayList<>(9);
        for (Grid.Cell cell : cells) {
            Rectangle bounds = getBoundaryForLocation(cell.getLocation());
            Widget widget = wrapTransparentIfIsNotInHighlightedWinningTriple(createMarkWidget(cell.getMark(), bounds), cell.getLocation());
            widget.moveBy(gridOrigin.x, gridOrigin.y);
            gridMArks.add(widget);
        }
        return gridMArks;
    }

    private Rectangle getBoundaryForLocation(Grid.Location location) {
        return style.getGridMarkLocations()[location.getRow().ordinal()][location.getColumn().ordinal()];
    }

    private Widget wrapTransparentIfIsNotInHighlightedWinningTriple(Widget widget, Grid.Location location) {
        if (noWinningTripleHighlighted() || isInWinningTriple(location)) {
            return widget;
        }
        return new CompositeDecoratorWidget(widget, WinningTripleAlpha);
    }

    private boolean noWinningTripleHighlighted() {
        return !hasHighlightedTriple();
    }

    private boolean isInWinningTriple(Grid.Location location) {
        return Iterables.contains(threeInARow.getLocations(), location);
    }

    private Widget createMarkWidget(Grid.Mark mark, Rectangle bounds) {
        switch (mark) {
            case Cross:
                return new ImageWidget(bounds.x, bounds.y, style.getCrossImage());
            case Nought:
                return new ImageWidget(bounds.x, bounds.y, style.getNoughtImage());
            case None:
                return new EmptyWidget();
            default:
                throw new RuntimeException("This is no sensible state!");
        }
    }

    private Widget highlightWidget() {
        Point origin = getUiOrigin();
        if (hasHighlightedCell()) {
            Rectangle boundaryForLocation = getBoundaryForLocation(highlightedCell);
            boundaryForLocation = boundaryForLocation.translate(origin.x, origin.y);
            return new RectangleWidget(boundaryForLocation, style.getHighlightColor());
        } else {
            return new EmptyWidget();
        }
    }

    private boolean hasHighlightedCell() {
        return highlightedCell != null;
    }

    private Widget debugInfoWidget() {
        return isDebugMode ? new DebugWidget() : new EmptyWidget();
    }


    class DebugWidget extends EcsWidget {
        @Override
        public void paintEcs(GraphicsStream pipe) {
            System.out.println("---->Painting @" + new Date());
            pipe.pushColor(debugColor);
            for (int row = 0; row < bounds.width; row += 50) {
                pipe.drawLine(Point.of(0, row), Point.of(bounds.height, row));
            }
            for (int col = 0; col < bounds.width; col += 50) {
                pipe.drawLine(Point.of(col, 0), Point.of(col, bounds.height));
            }
        }
    }
}
