package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Features.class)
public class FilledRectangleWidgetTest {
    @Test
    public void paintingIsInEcs() {
        Widget w = new FilledRectangleWidget(new Rectangle(0, 0, 84, 966), Color.ORANGE);
        w.setLocation(42, 42);
        GraphicsStream g2d = mock(GraphicsStream.class);

        w.paint(g2d);

        verify(g2d).fillRectangle(new Rectangle(0, 0, 84, 966));
    }

    @Test
    public void colorIsSetForPainting() {
        FilledRectangleWidget w = new FilledRectangleWidget(new Rectangle(0, 0, 84, 966), Color.ORANGE);
        GraphicsStream g2d = mock(GraphicsStream.class);

        w.paintEcs(g2d);

        verify(g2d).pushColor(Color.ORANGE);
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<FilledRectangleWidget> equivalentFixture() {
        return new Equivalent.Factory<FilledRectangleWidget>() {
            @Override
            public FilledRectangleWidget createItem() {
                return new FilledRectangleWidget(new Rectangle(0, 0, 84, 966), Color.ORANGE);
            }

            @Override
            public List<FilledRectangleWidget> createOtherItems() {
                return asList(
                        new FilledRectangleWidget(new Rectangle(0, 0, 42, 966), Color.ORANGE),
                        new FilledRectangleWidget(new Rectangle(0, 0, 84, 42), Color.ORANGE),
                        new FilledRectangleWidget(new Rectangle(0, 0, 84, 966), Color.CYAN)
                );
            }
        };
    }
}
