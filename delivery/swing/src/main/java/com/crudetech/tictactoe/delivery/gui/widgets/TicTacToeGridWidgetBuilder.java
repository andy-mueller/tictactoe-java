package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.game.Grid;

public class TicTacToeGridWidgetBuilder {
    private Rectangle bounds;
    private Style style;
    private Grid.ThreeInARow threeInARow = Grid.ThreeInARow.Empty;
    private Iterable<Grid.Cell> cells;
    private Grid.Location highlightedCell;
    private boolean debugMode = false;
    private Color debugColor;

    public TicTacToeGridWidgetBuilder withBounds(Rectangle bounds) {
        this.bounds = bounds;
        return this;
    }

    public TicTacToeGridWidgetBuilder withStyle(Style style) {
        this.style = style;
        return this;
    }

    public TicTacToeGridWidgetBuilder hasThreeInARow(Grid.ThreeInARow threeInARow) {
        this.threeInARow = threeInARow;
        return this;
    }

    public TicTacToeGridWidgetBuilder withCells(Iterable<Grid.Cell> cells) {
        this.cells = cells;
        return this;
    }

    public TicTacToeGridWidgetBuilder hasHighlightedCellAt(Grid.Location highlightedCell) {
        this.highlightedCell = highlightedCell;
        return this;
    }

    public TicTacToeGridWidgetBuilder setDebugMode(boolean debugMode, Color debugColor) {
        this.debugMode = debugMode;
        this.debugColor = debugColor;
        return this;
    }

    public TicTacToeGridWidgetBuilder noDebug() {
        this.debugMode = false;
        this.debugColor = null;
        return this;
    }



    public TicTacToeGridWidget createTicTacToeGridWidget() {
        return new TicTacToeGridWidget(bounds, style, threeInARow, cells, highlightedCell, debugMode, debugColor);
    }
}