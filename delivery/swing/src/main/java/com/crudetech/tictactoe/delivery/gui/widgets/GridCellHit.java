package com.crudetech.tictactoe.delivery.gui.widgets;


import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.game.Grid;

public class GridCellHit {

    static GridCellHit NoHit = new GridCellHit(null, false);
    private final boolean hasHit;


    private final Grid.Location location;

    GridCellHit(Grid.Location location, Rectangle boundary, Point hitPoint) {
        this(location, boundary.contains(hitPoint));
    }

    GridCellHit(Grid.Location location, boolean hasHit) {
        this.location = location;
        this.hasHit = hasHit;
    }

    public Grid.Location getHit() {
        return location;
    }

    public boolean hasHit() {
        return hasHit;
    }
}
