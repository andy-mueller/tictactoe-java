package com.crudetech.gui.widgets;

import com.crudetech.tictactoe.delivery.gui.widgets.EmptyWidget;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CoordinatesTest {
    @Test
    public void givenWorldInput_PointIsTransformedToWidgetCoordinates() throws Exception {
        EmptyWidget widget = new EmptyWidget();
        widget.widgetCoordinates().setLocation(Point.of(42, 42));

        Point actual = Coordinates.World.toWidgetCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.Origin));
    }

    @Test
    public void givenWorldInput_PointIsNotTransformed() throws Exception {
        EmptyWidget widget = new EmptyWidget();
        widget.widgetCoordinates().setLocation(Point.of(42, 42));

        Point actual = Coordinates.World.toWorldCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.of(42, 42)));
    }

    @Test
    public void givenWidgetInput_PointIsNotTransformed() throws Exception {
        EmptyWidget widget = new EmptyWidget();
        widget.widgetCoordinates().setLocation(Point.of(42, 42));

        Point actual = Coordinates.Widget.toWidgetCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.of(42, 42)));
    }

    @Test
    public void givenWidgetInput_PointIsTransformedToWorld() throws Exception {
        EmptyWidget widget = new EmptyWidget();
        widget.widgetCoordinates().setLocation(Point.of(42, 42));

        Point actual = Coordinates.Widget.toWorldCoordinates(widget, Point.of(42, 42));

        assertThat(actual, is(Point.of(84, 84)));
    }

}
