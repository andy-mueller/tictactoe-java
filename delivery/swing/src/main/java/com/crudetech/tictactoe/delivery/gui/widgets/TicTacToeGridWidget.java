package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.getAtLocation;
import static java.lang.Math.max;

public class TicTacToeGridWidget extends EcsWidget {
    private final Rectangle bounds;
    private final Style style;

    private final TicTacToeGridModel model;
    private final boolean isDebugMode;
    private final Color debugColor;

    static final AlphaValue WinningTripleAlpha = new AlphaValue(0.4f);


    public TicTacToeGridWidget(Rectangle bounds,
                               Style style, TicTacToeGridModel model,
                               boolean debugMode, Color debugColor) {
        super(bounds.x, bounds.y);
        this.bounds = bounds.setLocation(0,0);
        this.style = style;
        this.model = model;
        this.isDebugMode = debugMode;
        this.debugColor = debugColor;
    }

    public static TicTacToeGridWidgetBuilder builder() {
        return new TicTacToeGridWidgetBuilder();
    }

    @Override
    public void paintEcs(GraphicsStream pipe) {
        for (Widget widget : buildPaintList()) {
            widget.paint(pipe);
        }

    }


    List<Widget> buildPaintList() {
        List<Widget> paintList = new ArrayList<>();

        paintList.add(backgroundWidget());
        paintList.add(backgroundImageWidget());
        paintList.addAll(gridMarkWidgetList());
        paintList.add(highlightWidget());
        paintList.add(debugInfoWidget());


        return paintList;
    }

    public GridCellHit checkGridCellHit(Point hitInWorld) {
        Point hitInEcs = widgetCoordinates().toWidgetCoordinates(hitInWorld);
        Point backgroundImageOrigin = getBackgroundImageOrigin();
        Point ptInUiCoordinates = hitInEcs.translate(-backgroundImageOrigin.x, -backgroundImageOrigin.y);
        Iterable<Grid.Cell> allCells = model.getGrid().getCells();
        Rectangle[][] hitBoundaries = style.getGridMarkLocations();
        return new GridCellHit(allCells, ptInUiCoordinates, hitBoundaries);
    }

    private Widget backgroundWidget() {
        Rectangle boundary = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height + 500);
        return new FilledRectangleWidget(boundary, style.getBackgroundColor());
    }

    private Widget backgroundImageWidget() {
        Image backgroundImage = style.getBackgroundImage();

        Point imageLocation = getBackgroundImageOrigin();
        ImageWidget imageWidget = new ImageWidget(imageLocation, backgroundImage);

        return model.hasHighlightedThreeInARow()
                ? new CompositeDecoratorWidget(imageWidget, WinningTripleAlpha)
                : imageWidget;
    }


    Point getBackgroundImageOrigin() {
        Image backgroundImage = style.getBackgroundImage();
        int x = max((bounds.width - backgroundImage.getWidth()) / 2, 0);
        int y = max((bounds.height - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }

    List<Widget> gridMarkWidgetList() {
        Point gridOrigin = getBackgroundImageOrigin();
        List<Widget> gridMArks = new ArrayList<>(9);
        for (Grid.Cell cell : model.getGrid().getCells()) {
            Rectangle bounds = getBoundaryForLocation(cell.getLocation());
            Widget widget = wrapTransparentIfIsNotInHighlightedWinningTriple(
                    createMarkWidget(cell.getMark(), bounds),
                    cell.getLocation()
            );
            widget.widgetCoordinates().translate(gridOrigin.x, gridOrigin.y);
            gridMArks.add(widget);
        }
        return gridMArks;
    }

    private Rectangle getBoundaryForLocation(Grid.Location location) {
        return getAtLocation(style.getGridMarkLocations(), location);
    }

    private Widget wrapTransparentIfIsNotInHighlightedWinningTriple(Widget widget, Grid.Location location) {
        if (noWinningTripleHighlighted() || isInWinningTriple(location)) {
            return widget;
        }
        return new CompositeDecoratorWidget(widget, WinningTripleAlpha);
    }

    private boolean noWinningTripleHighlighted() {
        return !model.hasHighlightedThreeInARow();
    }

    private boolean isInWinningTriple(Grid.Location location) {
        return model.getHighlightedThreeInARow().containsLocation(location);
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
        if (hasHighlightedCell()) {
            Point origin = getBackgroundImageOrigin();
            Rectangle boundaryForLocation = getBoundaryForLocation(model.getHighlightedCell());
            boundaryForLocation = boundaryForLocation.translate(origin.x, origin.y);
            return new RectangleWidget(boundaryForLocation, style.getHighlightColor());
        } else {
            return new EmptyWidget();
        }
    }

    private boolean hasHighlightedCell() {
        return model.hasHighlightedCell();
    }

    private Widget debugInfoWidget() {
        return isDebugMode ? new DebugWidget() : new EmptyWidget();
    }

    public Iterable<Rectangle> getCellBoundaries(Iterable<Grid.Location> changedCells) {
        return from(changedCells).select(toBoundaryRectangle()).select(toComponentCoos());

    }

    private UnaryFunction<Rectangle, Rectangle> toComponentCoos() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle rectangle) {
                CoordinateSystem backgroundImageCoos = backgroundImageWidget().widgetCoordinates();
                return backgroundImageCoos.toWorldCoordinates(rectangle);
            }
        };
    }

    private UnaryFunction<Grid.Location, Rectangle> toBoundaryRectangle() {
        return new UnaryFunction<Grid.Location, Rectangle>() {
            @Override
            public Rectangle execute(Grid.Location location) {
                return getAtLocation(style.getGridMarkLocations(),location);
            }
        };
    }

    class DebugWidget extends EcsWidget {
        @Override
        public void paintEcs(GraphicsStream pipe) {
            System.out.println("---->Painting @" + new Date());
            pipe.pushColor(debugColor);
            for (int row = 0; row < bounds.width; row += 50) {
                //noinspection SuspiciousNameCombination
                pipe.drawLine(Point.of(0, row), Point.of(bounds.height, row));
            }
            for (int col = 0; col < bounds.width; col += 50) {
                pipe.drawLine(Point.of(col, 0), Point.of(col, bounds.height));
            }
        }
    }
}
