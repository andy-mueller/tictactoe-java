package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Rectangle;

public class TicTacToeGridWidgetBuilder {
    private Rectangle bounds;
    private Style style;
    private boolean debugMode = false;
    private Color debugColor;
    private TicTacToeGridModel model;

    public TicTacToeGridWidgetBuilder withBounds(Rectangle bounds) {
        this.bounds = bounds;
        return this;
    }

    public TicTacToeGridWidgetBuilder withStyle(Style style) {
        this.style = style;
        return this;
    }

    public TicTacToeGridWidgetBuilder withModel(TicTacToeGridModel model) {
        this.model = model;
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
        return new TicTacToeGridWidget(bounds, style, model, debugMode, debugColor);
    }
}