package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.event.Event;
import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.CellEventObject;
import com.crudetech.tictactoe.ui.HumanPlayer;

import java.io.PrintWriter;

class TextHumanPlayer extends HumanPlayer {
    final EventSupport<CellEventObject<TextGridWidget>> cellEnterEvent = new EventSupport<CellEventObject<TextGridWidget>>();
    private final PrintWriter out;
    private final TextUserInput input;
    private final TextGridWidget widget;

    public TextHumanPlayer(TextGridWidgetUiView textGridWidgetUiView, TextUiFeedbackChannel textUiFeedbackChannel, PrintWriter out, TextUserInput input, TextGridWidget widget) {
        super(textGridWidgetUiView, textUiFeedbackChannel);
        this.out = out;
        this.input = input;
        this.widget = widget;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        super.yourTurn(actualGrid);
        out.println("Make your move");
        Grid.Location location = input.nextLocation();
        cellEnterEvent.fireEvent(new CellEventObject<TextGridWidget>(widget, location));
    }

    @Override
    public Event<? extends CellEventObject<?>> makeMove() {
        return cellEnterEvent;
    }
}
