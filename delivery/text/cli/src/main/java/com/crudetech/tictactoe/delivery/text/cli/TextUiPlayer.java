package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.UiPlayer;

import java.io.PrintWriter;

class TextUiPlayer extends UiPlayer {
    private final PrintWriter out;
    private final TextGridLocationInput input;

    public TextUiPlayer(TextGridWidgetUiView textGridWidgetUiView, TextUiFeedbackChannel textUiFeedbackChannel, PrintWriter out, TextGridLocationInput input) {
        super(textGridWidgetUiView, textUiFeedbackChannel);
        this.out = out;
        this.input = input;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        super.yourTurn(actualGrid);
        out.println("Make your move");
        Grid.Location location = input.nextLocation();
        makeMove(location);
    }
}
