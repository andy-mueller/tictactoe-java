package com.crudetech.gui.widgets;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class EmptyWidgetTest {
    @Test
    public void paintDoesNothing() {
        EmptyWidget empty = new EmptyWidget();
        GraphicsStream g2d = mock(GraphicsStream.class);

        empty.paint(g2d);

        verifyZeroInteractions(g2d);
    }

    @Test
    public void equals() {
        EmptyWidget empty = new EmptyWidget();
        assertThat(empty, is(new EmptyWidget()));
    }
}
