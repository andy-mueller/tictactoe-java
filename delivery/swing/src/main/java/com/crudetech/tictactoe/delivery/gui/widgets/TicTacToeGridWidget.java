package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.crudetech.query.Query.from;

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
            add(centeredGridWidget());
            add(debugInfoWidget());
        }};
    }

    private TicTacToeCenteredGridWidget centeredGridWidget() {
        return new TicTacToeCenteredGridWidget(bounds, style, model);
    }

    private Widget backgroundWidget() {
        Rectangle boundary = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height + 500);
        return new FilledRectangleWidget(boundary, style.getBackgroundColor());
    }


    private Widget debugInfoWidget() {
        return isDebugMode ? new DebugWidget() : new EmptyWidget();
    }

    public GridCellHit gridCellHit(Point hitInWorld, Coordinates coordinates) {
        Point hitInEcs = coordinates.toWidgetCoordinates(this, hitInWorld);
        return centeredGridWidget().hitTest(hitInEcs, Coordinates.World);
    }

    public Iterable<Rectangle> getCellBoundaries(Iterable<Grid.Location> changedCells) {
        return from(centeredGridWidget().getCellBoundaries(changedCells)).select(toWorldCoordinates());
    }

    private UnaryFunction<? super Rectangle, Rectangle> toWorldCoordinates() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle r) {
                return widgetCoordinates().toWorldCoordinates(r);
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
