package com.crudetech.tictactoe.delivery.text.jcurses;

import jcurses.system.InputChar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class GridWidgetRaisesRepaintOnKeyInputTest {
    private final InputChar inputKey;

    public GridWidgetRaisesRepaintOnKeyInputTest(InputChar inputKey) {
        this.inputKey = inputKey;
    }

    @Parameterized.Parameters
    public static Collection<InputChar[]> parameters(){
        return asList(new InputChar[][]{
                {new InputChar(InputChar.KEY_RIGHT)},
                {new InputChar(InputChar.KEY_DOWN)},
                {new InputChar(InputChar.KEY_LEFT)},
                {new InputChar(InputChar.KEY_UP)},
                {new InputChar('X')},
                {new InputChar('O')},
        });
    }

    @Test
    public void anyCharInputRaisesRepaint() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);

        w.handleInput(inputKey);

        assertThat(w.getRepaints(), is(1));
    }
}
