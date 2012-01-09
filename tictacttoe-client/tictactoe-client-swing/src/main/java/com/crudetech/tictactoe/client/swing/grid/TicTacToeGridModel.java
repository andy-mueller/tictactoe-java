package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TicTacToeGridModel extends Model<Grid> {

    private Grid.Location highlightedCell;

    public TicTacToeGridModel(Grid grid) {
        super(grid);
    }

    public TicTacToeGridModel() {
        this(LinearRandomAccessGrid.empty());
    }

    public void highlight(Grid.Location highlightedCell) {
        verifyThat(highlightedCell, is(notNullValue()));
        this.highlightedCell = highlightedCell;
    }

    public Grid.Location getHighlighted() {
        return highlightedCell;
    }

    public boolean hasHighlight() {
        return highlightedCell != null;
    }

    public void unHighlight() {
        highlightedCell = null;
    }
}
