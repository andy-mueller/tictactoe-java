package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.collections.Iterables;
import com.crudetech.gui.widgets.EcsWidget;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.game.Grid;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.max;

class TicTacToeGridWidget extends EcsWidget {
    private final Rectangle bounds;
    private final Style style;
    private final Grid.ThreeInARow threeInARow;
    private final Iterable<Grid.Cell> cells;
    private final Grid.Location highlightedCell;
    private final boolean isDebugMode;


    TicTacToeGridWidget(Rectangle bounds, Style style, Grid.ThreeInARow threeInARow, Iterable<Grid.Cell> cells, Grid.Location hasHighlightedCell, boolean debugMode) {
        this.bounds = bounds;
        this.style = style;
        this.threeInARow = threeInARow;
        this.cells = cells;
        this.highlightedCell = hasHighlightedCell;
        this.isDebugMode = debugMode;
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
        Rectangle bounds = new Rectangle(this.bounds);
        bounds.height += 500;
        return new FilledRectangleWidget(bounds, style.getBackgroundColor());
    }

    private Widget backgroundImageWidget() {
        BufferedImage backgroundImage = style.getBackgroundImage();

        ImageWidget imageWidget = new ImageWidget(getUiOrigin(), backgroundImage);

        return hasHighlightedTriple() ? new CompositeDecoratorWidget(imageWidget, TicTacToeGridUI.WinningTripleAlpha) : imageWidget;
    }

    private boolean hasHighlightedTriple() {
        return threeInARow != null;
    }

    Point getUiOrigin() {
        BufferedImage backgroundImage = style.getBackgroundImage();
        int x = max((bounds.width - backgroundImage.getWidth()) / 2, 0);
        int y = max((bounds.height - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }

    java.util.List<Widget> gridMarkWidgetList() {
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
        return new CompositeDecoratorWidget(widget, TicTacToeGridUI.WinningTripleAlpha);
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
                return new ImageWidget(bounds.getLocation(), style.getCrossImage());
            case Nought:
                return new ImageWidget(bounds.getLocation(), style.getNoughtImage());
            case None:
                return new EmptyWidget();
            default:
                throw new RuntimeException("This is no sensible state!");
        }
    }

    private Widget highlightWidget() {
        Point origin = getUiOrigin();
        if (hasHighlightedCell()) {
            Rectangle boundaryForLocation = (Rectangle) getBoundaryForLocation(highlightedCell).clone();
            boundaryForLocation.translate(origin.x, origin.y);
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
            pipe.pushColor(Color.ORANGE);
            for (int row = 0; row < bounds.width; row += 50) {
                pipe.drawLine(0, row, bounds.height, row);
            }
            for (int col = 0; col < bounds.width; col += 50) {
                pipe.drawLine(col, 0, col, bounds.height);
            }
        }
    }
}
