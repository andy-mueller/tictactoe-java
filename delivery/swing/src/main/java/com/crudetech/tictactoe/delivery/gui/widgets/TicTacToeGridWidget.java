package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.crudetech.query.Query.from;
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
        this.bounds = bounds.setLocation(0, 0);
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
        return new ArrayList<Widget>() {{
            add(backgroundWidget());
            add(backgroundImageWidget());
            add(gridCellsWidget());
            add(highlightWidget());
            add(debugInfoWidget());
        }};
    }

    public GridCellHit gridCellHit(Point hitInWorld, Coordinates coordinates) {
        Point hitInEcs = coordinates.toWidgetCoordinates(this, hitInWorld);
        return gridCellsWidget().hitTest(hitInEcs, Coordinates.World);
    }

    private Widget backgroundWidget() {
        Rectangle boundary = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height + 500);
        return new FilledRectangleWidget(boundary, style.getBackgroundColor());
    }

    Widget backgroundImageWidget() {
        Image backgroundImage = style.getBackgroundImage();


        StatefulTransparencyImageWidget.TransparencyState state =
                backgroundImageTransparencyState();
        Widget backgroundImageWidget = new StatefulTransparencyImageWidget(state, backgroundImage);

        backgroundImageWidget = widgetAtBackgroundImageLocation(backgroundImageWidget);

        return backgroundImageWidget;
    }

    private StatefulTransparencyImageWidget.TransparencyState backgroundImageTransparencyState() {
        return new StatefulTransparencyImageWidget.TransparencyState() {
            @Override
            public boolean isTransparent() {
                return model.hasHighlightedThreeInARow();
            }

            @Override
            public AlphaValue transparency() {
                return WinningTripleAlpha;
            }
        };
    }


    Point getBackgroundImageOrigin() {
        Image backgroundImage = style.getBackgroundImage();
        int x = max((bounds.width - backgroundImage.getWidth()) / 2, 0);
        int y = max((bounds.height - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }

    TicTacToeGridCellsWidget gridCellsWidget() {
        TicTacToeGridCellsWidget widget = new TicTacToeGridCellsWidget(model, style);
        return widgetAtBackgroundImageLocation(widget);
    }

    private <T extends Widget> T widgetAtBackgroundImageLocation(T widget) {
        widget.widgetCoordinates().setLocation(getBackgroundImageOrigin());
        return widget;
    }


    private Widget highlightWidget() {
        TicTacToeGridHighlightedCellWidget.HighlightState state = highlightWidgetState();

        Widget highlightWidget = new TicTacToeGridHighlightedCellWidget(state, style);
        return widgetAtBackgroundImageLocation(highlightWidget);
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

    private Widget debugInfoWidget() {
        return isDebugMode ? new DebugWidget() : new EmptyWidget();
    }

    public Iterable<Rectangle> getCellBoundaries(Iterable<Grid.Location> changedCells) {
        return from(changedCells).select(toBoundaryRectangle()).select(toComponentCoos()).select(toWorldCoordinates());
    }

    private UnaryFunction<? super Rectangle, Rectangle> toWorldCoordinates() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle r) {
                return widgetCoordinates().toWorldCoordinates(r);
            }
        };
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
                return style.getGridMarkLocation(location);
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
            pipe.popColor();
        }
    }
}
