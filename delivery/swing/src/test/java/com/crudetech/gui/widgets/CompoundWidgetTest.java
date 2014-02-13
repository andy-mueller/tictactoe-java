package com.crudetech.gui.widgets;


import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompoundWidgetTest {
    private GraphicsStream pipe;
    private Widget subWidget;
    private CompoundWidget widget;

    @Before
    public void setUp() throws Exception {
        pipe = mock(GraphicsStream.class, RETURNS_DEEP_STUBS);
        subWidget = mock(Widget.class);
        widget = new CompoundWidget() {
            @Override
            protected Iterable<Widget> subWidgets() {
                return asList(subWidget);
            }
        };
    }

    @Test
    public void givenSubWidgets_allArePainted() throws Exception {
        widget.paint(pipe);

        verify(subWidget).paint(pipe);
    }

    @Test
    public void widgetCoordinateSystemIsPushed() throws Exception {
        CoordinateSystem coordinates = new CoordinateSystem(Point.of(42, 42), CoordinateSystem.NoScale);
        widget.setCoordinateSystem(coordinates);

        widget.paint(pipe);

        verify(pipe.newContext()).pushCoordinateSystem(coordinates);
    }
}
