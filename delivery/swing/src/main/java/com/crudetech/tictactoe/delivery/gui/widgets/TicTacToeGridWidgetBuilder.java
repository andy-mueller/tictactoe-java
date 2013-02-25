package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.delivery.swing.grid.TicTacToeGridModel;
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

    public TicTacToeGridWidgetBuilder withModel(TicTacToeGridModel model) {
        this.cells = model.getGrid().getCells();
        this.threeInARow = model.getHighlightedThreeInARow();
        this.highlightedCell = model.getHighlightedCell();
        return this;
    }

    public TicTacToeGridWidgetBuilder setDebugModeOn(Color debugColor) {
        this.debugMode = true;
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