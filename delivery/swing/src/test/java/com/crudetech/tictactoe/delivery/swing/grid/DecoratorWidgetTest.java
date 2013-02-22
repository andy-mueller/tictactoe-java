package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


@RunWith(Features.class)
public class DecoratorWidgetTest {
    @Test
    public void locationForwardToDecoratedWidget() {
        Widget w = mock(Widget.class);
        DecoratorWidget dec = new DecoratorWidget(w);

        dec.setLocation(42, 42);

        verify(w).setLocation(42, 42);
    }

    @Test
    public void moveIsForwardedToDecorated() {
        Widget w = mock(Widget.class);
        DecoratorWidget dec = new DecoratorWidget(w);

        dec.moveBy(42, 42);

        verify(w).moveBy(42, 42);
    }
    @Test
    public void paintIsForwardedToDecorated() {
        Widget w = mock(Widget.class);
        DecoratorWidget dec = new DecoratorWidget(w);
        GraphicsStream g2d = mock(GraphicsStream.class);

        dec.paint(g2d);

        verify(w).paint(g2d);
    }
    @Test
    public void getLocationIsTakenFromDecorated() {
        Widget w = mock(Widget.class);
        when(w.getLocation()).thenReturn(Point.of(42, 42));
        DecoratorWidget dec = new DecoratorWidget(w);

        assertThat(dec.getLocation(), is(Point.of(42, 42)));
    }
    @Feature(Equivalent.class)
    public static Equivalent.Factory<DecoratorWidget> decoratorIsEquivalent(){
        return new Equivalent.Factory<DecoratorWidget>(){
            private Widget widget = mock(Widget.class);

            @Override
            public DecoratorWidget createItem() {
                return new DecoratorWidget(widget);
            }

            @Override
            public List<DecoratorWidget> createOtherItems() {
                return asList(new DecoratorWidget(mock(Widget.class)));
            }
        };
    }
}
