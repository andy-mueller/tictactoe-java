package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.CompoundWidget;
import com.crudetech.gui.widgets.Widget;

import static com.crudetech.collections.Iterables.emptyListOf;
import static java.util.Arrays.asList;

public class TicTacToeGridCellsWidget extends CompoundWidget{
    public class Cross extends EmptyWidget{
    }

    public class None extends EmptyWidget{
    }

    public class Nought extends EmptyWidget{
    }

    public TicTacToeGridCellsWidget(TicTacToeGridModel model, Style style) {
    }

    @Override
    protected Iterable<Widget> subWidgets() {
        return emptyListOf(Widget.class);
    }

    Iterable<Widget> getCells() {
        return asList(
                (Widget)new Cross(), new None(),new Nought(),
                new None(), new Nought(),new None(),
                new None(), new Cross(),new Cross()
        );
    }
}
