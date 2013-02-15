package com.crudetech.tictactoe.delivery.swing.grid;


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

    private Composite alpha;
    private Composite oldAlpha;
    private CompositeDecoratorWidget dec;
    private Graphics2D g2d;
    private Widget decorated;

    @Before
    public void setUp() throws Exception {
        alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        oldAlpha = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.4f);
        decorated = mock(Widget.class);
        dec = new CompositeDecoratorWidget(decorated, alpha);
        g2d = mock(Graphics2D.class);
        when(g2d.getComposite()).thenReturn(oldAlpha);
    }

    @Test
    public void decoratorSetsTransparencyLevel() {
        dec.paintEcs(g2d);

        verify(g2d).setComposite(alpha);
    }

    @Test
    public void decoratorResetsTransparencyLevel() {
        dec.paintEcs(g2d);

        verify(g2d).setComposite(oldAlpha);
    }

    private static class BaBoomException extends RuntimeException {
    }
    @Test
    public void decoratorResetsTransparencyLevelOnException() {
        doThrow(new BaBoomException()).when(decorated).paintEcs(g2d);
        try {
            dec.paintEcs(g2d);
        } catch (BaBoomException e) {
        }
        verify(g2d).setComposite(oldAlpha);
    }
    @Feature(Equivalent.class)
    public static Equivalent.Factory<CompositeDecoratorWidget> decoratorIsEquivalent(){
        return new Equivalent.Factory<CompositeDecoratorWidget>(){
            Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
            Widget decorated = mock(Widget.class);

            @Override
            public CompositeDecoratorWidget createItem() {
                return new CompositeDecoratorWidget(decorated, alpha);
            }

            @Override
            public List<CompositeDecoratorWidget> createOtherItems() {
                return asList(
                        new CompositeDecoratorWidget(mock(Widget.class), alpha),
                        new CompositeDecoratorWidget(decorated, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f))
                );
            }
        };
    }
}
