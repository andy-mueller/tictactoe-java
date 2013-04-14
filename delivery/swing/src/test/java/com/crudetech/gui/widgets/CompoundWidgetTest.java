package com.crudetech.gui.widgets;


import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompoundWidgetTest {
    private GraphicsStream pipe;
    private Widget subWidget;
    private CompoundWidget widget;

    @Before
    public void setUp() throws Exception {
        pipe = mock(GraphicsStream.class);
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
        widget.widgetCoordinates().setLocation(Point.of(42, 42));

        widget.paint(pipe);

        verify(pipe).pushCoordinateSystem(widget.widgetCoordinates());
    }
}
