package com.crudetech.tictactoe.delivery.gui.widgets;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.matcher.RangeIsEqual;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.crudetech.query.Query.from;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridCellsWidgetTest {
    @Test
    public void cellsAreSetupAccordingToModel() throws Exception {
        Style style = StyleStub.builder()
                .withBackgroundImageSize(500, 400)
                .withCellSize(100, 100)
                .build();
        TicTacToeGridModel model = new TicTacToeGridModel(
                LinearRandomAccessGrid.of(
                        Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                        Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
                        Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Cross
                )
        );

        TicTacToeGridCellsWidget widget = new TicTacToeGridCellsWidget(model, style);

        Iterable<Class<?>> cellWidgetsClasses = from(widget.getCells()).select(toClass());

        List<Class<?>> expectedCellTypes = new ArrayList<>();
        expectedCellTypes.addAll(asList(TicTacToeGridCellsWidget.Cross.class, TicTacToeGridCellsWidget.None.class, TicTacToeGridCellsWidget.Nought.class));
        expectedCellTypes.addAll(asList(TicTacToeGridCellsWidget.None.class, TicTacToeGridCellsWidget.Nought.class, TicTacToeGridCellsWidget.None.class));
        expectedCellTypes.addAll(asList(TicTacToeGridCellsWidget.None.class, TicTacToeGridCellsWidget.Cross.class, TicTacToeGridCellsWidget.Cross.class));

        assertThat(cellWidgetsClasses, is(RangeIsEqual.equalTo(expectedCellTypes)));
    }

    private UnaryFunction<Object, Class<?>> toClass() {
        return new UnaryFunction<Object, Class<?>>() {
            @Override
            public Class<?> execute(Object o) {
                return o.getClass();
            }
        };
    }
}
