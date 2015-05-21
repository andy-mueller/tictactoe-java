package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import com.crudetech.tictactoe.game.Player;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GameReferenceTest {

    private final String movingPlayerId = "__movingPlayerId__";

    @Test
    public void givenPlayerMakesMove_ResultIsPresented() throws Exception {
        Player startPlayer = new HumanPlayer();
        Player otherPlayer = mock(Player.class);
        GameReference gameRef = GameReference.builder()
                .withStartPlayer(startPlayer)
                .withStartPlayerMark(Grid.Mark.Cross)
                .withOtherPlayer(otherPlayer)
                .build();

        Grid.Location move = Grid.Location.of(Grid.Row.First, Grid.Column.Second);
        GameReference.Presenter presenter = mock(GameReference.Presenter.class);

        gameRef.makeMove(movingPlayerId, move, presenter);

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None
        );
        verify(presenter).display(expectedGrid);
        verify(presenter, never()).highlight(any(Grid.ThreeInARow.class));
        verify(presenter, never()).finished();
    }
}