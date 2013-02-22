package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@RunWith(Features.class)
public class CompositeDecoratorWidgetTest {

    private AlphaValue alpha;
    private CompositeDecoratorWidget dec;
    private GraphicsStream g2d;
    private Widget decorated;

    @Before
    public void setUp() throws Exception {
        alpha = new AlphaValue(0.4f);
        decorated = mock(Widget.class);
        dec = new CompositeDecoratorWidget(decorated, alpha);
        g2d = mock(GraphicsStream.class);
    }

    @Test
    public void decoratorSetsTransparencyLevel() {
        dec.paint(g2d);

        verify(g2d).pushAlpha(alpha);
    }

    @Test
    public void decoratorResetsTransparencyLevel() {
        dec.paint(g2d);

        verify(g2d).popAlpha();
    }

    private static class BaBoomException extends RuntimeException {
    }
    @Test
    public void decoratorResetsTransparencyLevelOnException() {
        doThrow(new BaBoomException()).when(decorated).paint(g2d);
        try {
            dec.paint(g2d);
        } catch (BaBoomException e) {
        }
        verify(g2d).popAlpha();
    }
    @Feature(Equivalent.class)
    public static Equivalent.Factory<CompositeDecoratorWidget> decoratorIsEquivalent(){
        return new Equivalent.Factory<CompositeDecoratorWidget>(){
            AlphaValue alpha = new AlphaValue(0.4f);
            Widget decorated = mock(Widget.class);

            @Override
            public CompositeDecoratorWidget createItem() {
                return new CompositeDecoratorWidget(decorated, alpha);
            }

            @Override
            public List<CompositeDecoratorWidget> createOtherItems() {
                return asList(
                        new CompositeDecoratorWidget(mock(Widget.class), alpha),
                        new CompositeDecoratorWidget(decorated, new AlphaValue(0.1f))
                );
            }
        };
    }
}
