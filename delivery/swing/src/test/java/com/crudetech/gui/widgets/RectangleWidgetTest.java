package com.crudetech.gui.widgets;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.delivery.swing.grid.AwtColor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Features.class)
public class RectangleWidgetTest {
    @Test
    public void paintingIsInEcs() {
        Widget w = new RectangleWidget(new Rectangle(0, 0, 84, 966), AwtColor.ORANGE);
        setLocation(w, Point.of(42, 42));
        GraphicsStream g2d = mock(GraphicsStream.class, RETURNS_DEEP_STUBS);

        w.paint(g2d);

        verify(g2d).drawRectangle(new Rectangle(0, 0, 84, 966));
    }

    private void setLocation(Widget w, Point location) {
        WidgetJig jig = new WidgetJig(w);
        jig.setLocation(location);
    }

    @Test
    public void colorIsSetForPainting() {
        RectangleWidget w = new RectangleWidget(new Rectangle(0, 0, 84, 966), AwtColor.ORANGE);
        GraphicsStream g2d = mock(GraphicsStream.class, RETURNS_DEEP_STUBS);

        w.paintEcs(g2d);

        verify(g2d.newContext()).pushColor(AwtColor.ORANGE);
    }

    @Test
    public void colorIsResetAfterPainting() {
        RectangleWidget w = new RectangleWidget(new Rectangle(0, 0, 84, 966), AwtColor.ORANGE);
        GraphicsStream g2d = mock(GraphicsStream.class, RETURNS_DEEP_STUBS);

        w.paintEcs(g2d);

        verify(g2d.newContext()).close();
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<RectangleWidget> equivalentFixture() {
        return new Equivalent.Factory<RectangleWidget>() {
            @Override
            public RectangleWidget createItem() {
                return new RectangleWidget(new Rectangle(0, 0, 84, 966), AwtColor.ORANGE);
            }

            @Override
            public List<RectangleWidget> createOtherItems() {
                return asList(
                        new RectangleWidget(new Rectangle(0, 0, 42, 966), AwtColor.ORANGE),
                        new RectangleWidget(new Rectangle(0, 0, 84, 42), AwtColor.ORANGE),
                        new RectangleWidget(new Rectangle(0, 0, 84, 966), AwtColor.CYAN)
                );
            }
        };
    }
}
