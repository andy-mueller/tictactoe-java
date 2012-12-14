package com.crudetech.tictactoe.ui;

import com.crudetech.event.Event;

/**
* Represents a human player that plays the game through a UI.
*/
public class HumanPlayer extends UiPlayer {
    private final Event<? extends CellEventObject<?>> makeMove;

    public HumanPlayer(UiView view, UiFeedbackChannel uiFeedback, Event<? extends CellEventObject<?>> makeMove) {
        super(view, uiFeedback);
        this.makeMove = makeMove;
    }

    public Event<? extends CellEventObject<?>> makeMove() {
        return makeMove;
    }
}
