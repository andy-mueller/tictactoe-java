package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.delivery.swing.grid.RectangleWidget;
import com.crudetech.tictactoe.delivery.swing.grid.Widget;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Features.class)
public class RectangleWidgetTest {
    @Test
    public void paintingIsInEcs() {
        Widget w = new RectangleWidget(new Rectangle(0, 0, 84, 966), Color.ORANGE);
        w.setLocation(42, 42);
        Graphics2D g2d = mock(Graphics2D.class);

        w.paintEcs(g2d);

        verify(g2d).draw(new Rectangle(0, 0, 84, 966));
    }

    @Test
    public void colorIsSetForPainting() {
        RectangleWidget w = new RectangleWidget(new Rectangle(0, 0, 84, 966), Color.ORANGE);
        Graphics2D g2d = mock(Graphics2D.class);

        w.paintEcs(g2d);

        verify(g2d).setPaint(Color.ORANGE);
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<RectangleWidget> equivalentFixture() {
        return new Equivalent.Factory<RectangleWidget>() {
            @Override
            public RectangleWidget createItem() {
                return new RectangleWidget(new Rectangle(0, 0, 84, 966), Color.ORANGE);
            }

            @Override
            public List<RectangleWidget> createOtherItems() {
                return asList(
                        new RectangleWidget(new Rectangle(0, 0, 42, 966), Color.ORANGE),
                        new RectangleWidget(new Rectangle(0, 0, 84, 42), Color.ORANGE),
                        new RectangleWidget(new Rectangle(0, 0, 84, 966), Color.CYAN)
                );
            }
        };
    }
}
