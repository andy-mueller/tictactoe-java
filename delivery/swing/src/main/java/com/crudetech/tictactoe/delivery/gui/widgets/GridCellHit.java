package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.collections.Pair;
import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.game.Grid;

import static com.crudetech.query.Query.from;

public class GridCellHit {
    private final Pair<Rectangle, Grid.Location> hitInfo;
    private static final Pair<Rectangle, Grid.Location> NoHit =
            new Pair<>(new Rectangle(-1, -1, -1, -1), null);

    GridCellHit(Iterable<Grid.Cell> cells, int x, int y, Rectangle[][] cellBoundaries) {
        hitInfo = from(cells).select(isContainedIn(x, y, cellBoundaries)).where(notNoHit()).firstOr(NoHit);
    }
    GridCellHit(Iterable<Grid.Cell> cells, Point hit, Rectangle[][] cellBoundaries) {
        this(cells, hit.x, hit.y, cellBoundaries);
    }

    public Grid.Location getHit() {
        return hitInfo.getSecond();
    }

    public boolean hasHit() {
        return !NoHit.equals(hitInfo);
    }


    private UnaryFunction<Pair<Rectangle, Grid.Location>, Boolean> notNoHit() {
        return new UnaryFunction<Pair<Rectangle, Grid.Location>, Boolean>() {
            @Override
            public Boolean execute(Pair<Rectangle, Grid.Location> rectangleLocationPair) {
                return !rectangleLocationPair.equals(NoHit);
            }
        };
    }

    private UnaryFunction<Grid.Cell, Pair<Rectangle, Grid.Location>> isContainedIn(final int x, final int y, final Rectangle[][] gridMarkLocations) {
        return new UnaryFunction<Grid.Cell, Pair<Rectangle, Grid.Location>>() {
            @Override
            public Pair<Rectangle, Grid.Location> execute(Grid.Cell cell) {
                Grid.Location location = cell.getLocation();
                Rectangle hitRect = gridMarkLocations[location.getRow().ordinal()][location.getColumn().ordinal()];
                if (hitRect.contains(x, y)) {
                    return new Pair<>(hitRect, location);
                }
                return NoHit;
            }
        };
    }
}
