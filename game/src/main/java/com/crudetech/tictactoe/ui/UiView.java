package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.Grid;

public interface UiView {
    void setModel(Grid grid);
    void highlight(Grid.ThreeInARow triple);
}
