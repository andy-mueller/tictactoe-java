package com.crudetech.tictactoe.ui;

import com.crudetech.event.Event;

/**
* Represents a human player that plays the game through a UI.
*/
public abstract class HumanPlayer extends UiPlayer {

    public HumanPlayer(UiView view, UiFeedbackChannel uiFeedback) {
        super(view, uiFeedback);
    }

    public abstract Event<? extends CellEventObject<?>> makeMove();
}
