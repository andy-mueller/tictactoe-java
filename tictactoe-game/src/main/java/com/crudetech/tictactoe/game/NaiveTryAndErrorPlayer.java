package com.crudetech.tictactoe.game;

import java.util.List;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;
import static java.util.Collections.shuffle;

public class NaiveTryAndErrorPlayer extends ComputerPlayer{

    @Override
    public void yourTurn(Grid actualGrid) {
        List<Grid.Location> cells =
                from(actualGrid.getCells()).where(markIsEqualTo(Grid.Mark.None)).select(location()).toList();
        if (!cells.isEmpty()) {
            shuffle(cells);
            addMark(cells.get(0));
        }
    }
}
