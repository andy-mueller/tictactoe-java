package com.crudetech.tictactoe.game;

import java.util.List;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;
import static java.util.Collections.shuffle;

public class NaiveTryAndErrorPlayer extends ComputerPlayer{
    @Override
    protected Grid.Location computeNextMove(Grid actualGrid) {
        List<Grid.Location> cells =
                from(actualGrid.getCells()).where(markIsEqualTo(Grid.Mark.None)).select(location()).toList();
        verifyListNotEmpty(cells);
        shuffle(cells);
        return cells.get(0);
    }

    private void verifyListNotEmpty(List<Grid.Location> cells) {
        if (cells.isEmpty()) {
            throw new IllegalStateException();
        }
    }
}
