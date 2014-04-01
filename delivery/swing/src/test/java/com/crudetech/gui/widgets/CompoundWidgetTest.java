package com.crudetech.gui.widgets;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class CompoundWidgetTest {
    private GraphicsStream pipe;
    private Widget subWidget;
    private CompoundWidget widget;

    @Rule
    public GraphicStreamMockery mockery = GraphicStreamMockery.withOnlyOneSubContext();

    @Before
    public void setUp() throws Exception {
        pipe = mockery.stream();
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

        mockery.verifyOnlyOneSubContextCreated();

        verify(subWidget).paint(mockery.lastContextMock());
    }
}
