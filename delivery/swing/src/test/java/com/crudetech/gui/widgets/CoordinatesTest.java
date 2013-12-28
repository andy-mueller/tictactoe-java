package com.crudetech.gui.widgets;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CoordinatesTest {
    @Test
    public void givenWorldInput_PointIsTransformedToWidgetCoordinates() throws Exception {
        Widget widget = createWidgetWithLocation();

        Point actual = Coordinates.World.toWidgetCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.Origin));
    }

    private Widget createWidgetWithLocation() {
        EmptyWidget widget = new EmptyWidget();
        WidgetJig jig = new WidgetJig(widget);
        jig.setLocation(Point.of(42, 42));
        return widget;
    }

    @Test
    public void givenWorldInput_PointIsNotTransformed() throws Exception {
        Widget widget = createWidgetWithLocation();

        Point actual = Coordinates.World.toWorldCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.of(42, 42)));
    }

    @Test
    public void givenWidgetInput_PointIsNotTransformed() throws Exception {
        Widget widget = createWidgetWithLocation();

        Point actual = Coordinates.Widget.toWidgetCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.of(42, 42)));
    }

    @Test
    public void givenWidgetInput_PointIsTransformedToWorld() throws Exception {
        Widget widget = createWidgetWithLocation();

        Point actual = Coordinates.Widget.toWorldCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.of(84, 84)));
    }
}
