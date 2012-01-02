package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TicTacToeGridModelTest {
    @Test
    public void ctorSetsModelData() {
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross,Grid.Mark.Cross,Grid.Mark.Cross,
                Grid.Mark.Cross,Grid.Mark.Cross,Grid.Mark.Cross,
                Grid.Mark.Cross,Grid.Mark.Cross,Grid.Mark.Cross
                );
        TicTacToeGridModel model = new TicTacToeGridModel(grid);

        assertThat(model.getModelObject(), is(grid));
    }
    @Test
    public void setModelRaisesEvent(){
        TicTacToeGridModel model = new TicTacToeGridModel();
        @SuppressWarnings("unchecked")
        EventListener<Model.ChangedEventObject<Model<Grid>>> changedListener = mock(EventListener.class);
        model.changed().addListener(changedListener);
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross,Grid.Mark.Cross,Grid.Mark.Cross,
                Grid.Mark.Cross,Grid.Mark.Cross,Grid.Mark.Cross,
                Grid.Mark.Cross,Grid.Mark.Cross,Grid.Mark.Cross
        );

        model.setModelObject(grid);

        verify(changedListener).onEvent(new Model.ChangedEventObject<Model<Grid>>(model));
    }
    @Test
    public void setModelSetsModelObject(){
        TicTacToeGridModel model = new TicTacToeGridModel();
        Grid grid = LinearRandomAccessGrid.empty();

        model.setModelObject(grid);

        assertThat(model.getModelObject(), is(grid));
    }
    @Test
    public void setModelThrowsOnNull(){
        final TicTacToeGridModel model = new TicTacToeGridModel();

        Runnable setNullValue = new Runnable() {
            @Override
            public void run() {
                model.setModelObject(null);
            }
        };

        assertThat(setNullValue, doesThrow(IllegalArgumentException.class));
    }
}
