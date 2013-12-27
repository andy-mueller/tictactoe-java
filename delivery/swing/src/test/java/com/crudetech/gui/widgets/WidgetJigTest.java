package com.crudetech.gui.widgets;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WidgetJigTest {
    private EcsWidgetSpy widget;
    private WidgetJig jig;

    private static class EcsWidgetSpy extends EcsWidget {
        @Override
        protected void paintEcs(GraphicsStream pipe) {
        }
    }

    @Before
    public void setUp() throws Exception {
        CoordinateSystem startCoordinates = new CoordinateSystem(Point.of(42, 42), 0.5);
        widget = new EcsWidgetSpy();
        widget.setCoordinateSystem(startCoordinates);
        jig = new WidgetJig(widget);
    }

    @Test
    public void givenAWidget_translateMovesTheWidget() throws Exception {
        jig.translate(42, 42);

        assertThat(widget.coordinateSystem(), is(new CoordinateSystem(Point.of(84, 84), 0.5)));
    }
    @Test
    public void givenAWidget_setLocationPutsWidgetOnThatLocation() throws Exception {
        jig.setLocation(2, 2);

        assertThat(widget.coordinateSystem(), is(new CoordinateSystem(Point.of(2, 2), 0.5)));
    }

    @Test
    public void givenAWidget_scaleScalesTheWidget() throws Exception {
        jig.scale(0.5);

        assertThat(widget.coordinateSystem(), is(new CoordinateSystem(Point.of(42, 42), 0.25)));
    }
}
