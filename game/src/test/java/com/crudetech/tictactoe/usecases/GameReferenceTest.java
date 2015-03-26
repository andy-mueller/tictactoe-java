package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import com.crudetech.tictactoe.game.Player;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class GameReferenceTest {
    @Ignore
    @Test
    public void bootstrap() throws Exception {
        Player startPlayer = new HumanPlayer();
        Player otherPlayer = mock(Player.class);
        GameReference gameRef = GameReference.builder()
                .withStartPlayer(startPlayer)
                .withStartPlayerMark(Grid.Mark.Cross)
                .withOtherPlayer(otherPlayer)
                .build();

        Grid.Location move = Grid.Location.of(Grid.Row.First, Grid.Column.Second);
        GameReference.Presenter presenter = mock(GameReference.Presenter.class);

        gameRef.makeMove("__movingPlayerId", move, presenter);

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