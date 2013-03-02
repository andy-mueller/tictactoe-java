package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(Features.class)
public class DecoratorWidgetTest {
    @Test
    public void ecsIsTakenFromDecoratedWidget() {
        Widget w = mock(Widget.class);
        DecoratorWidget dec = new DecoratorWidget(w);

        dec.widgetCoordinates();

        verify(w).widgetCoordinates();
    }


    @Test
    public void paintIsForwardedToDecorated() {
        Widget w = mock(Widget.class);
        DecoratorWidget dec = new DecoratorWidget(w);
        GraphicsStream g2d = mock(GraphicsStream.class);

        dec.paint(g2d);

        verify(w).paint(g2d);
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
