package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.Grid;

public interface UiView {
    void setGrid(Grid grid);

    void moveCursorToFirstMarkedCell(Grid grid);

    void highlight(Grid.Triple triple);
}
