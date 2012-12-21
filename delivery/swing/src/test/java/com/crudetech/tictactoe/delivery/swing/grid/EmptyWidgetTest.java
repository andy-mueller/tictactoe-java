package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.tictactoe.delivery.swing.grid.EmptyWidget;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class EmptyWidgetTest {
    @Test
    public void paintDoesNothing() {
        EmptyWidget empty = new EmptyWidget();
        Graphics2D g2d = mock(Graphics2D.class);

        empty.paintEcs(g2d);

        verifyZeroInteractions(g2d);
    }

    @Test
    public void equals() {
        EmptyWidget empty = new EmptyWidget();
        assertThat(empty, is(new EmptyWidget()));
    }
}
