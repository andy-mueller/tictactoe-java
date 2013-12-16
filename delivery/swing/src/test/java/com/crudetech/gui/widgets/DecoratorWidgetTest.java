package com.crudetech.gui.widgets;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(Features.class)
public class DecoratorWidgetTest {
    @Test
    public void ecsIsTakenFromDecoratedWidget() {
        Widget w = mock(Widget.class);
        DecoratorTemplateWidget<Widget> dec = new DecoratorWidget<>(w);

        dec.coordinateSystem();

        verify(w).coordinateSystem();
    }


    @Test
    public void paintIsForwardedToDecorated() {
        Widget w = mock(Widget.class);
        DecoratorTemplateWidget<Widget> dec = new DecoratorWidget<>(w);
        GraphicsStream g2d = mock(GraphicsStream.class);

        dec.paint(g2d);

        verify(w).paint(g2d);
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<DecoratorTemplateWidget<Widget>> decoratorIsEquivalent() {
        return new Equivalent.Factory<DecoratorTemplateWidget<Widget>>() {
            private Widget widget = mock(Widget.class);

            @Override
            public DecoratorTemplateWidget<Widget> createItem() {
                return new DecoratorWidget<>(widget);
            }

            @Override
            public List<DecoratorTemplateWidget<Widget>> createOtherItems() {
                return Arrays.<DecoratorTemplateWidget<Widget>>asList(new DecoratorWidget<>(mock(Widget.class)));
            }
        };
    }
}
