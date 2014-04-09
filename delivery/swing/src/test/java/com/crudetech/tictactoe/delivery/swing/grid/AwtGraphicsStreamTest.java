package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.Point;
import com.crudetech.tictactoe.delivery.swing.LastCallOnMethod;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AwtGraphicsStreamTest {
    @Test
    public void pushedCoordinatesAreAppliedToGraphics() throws Exception {
        Graphics2D g2d = mock(Graphics2D.class);
        AwtGraphicsStream awtStream = new AwtGraphicsStream(g2d);
        CoordinateSystem coordinates = new CoordinateSystem(Point.of(42, 42), 0.5);

        awtStream.pushCoordinateSystem(coordinates);

        verify(g2d).translate(42, 42);
        verify(g2d).scale(0.5, 0.5);
    }

    @Ignore
    @Test
    public void givenCoordinatesArePopped_PreviousCoordinatesAreRestoredOnGraphics() throws Exception {
        Graphics2D g2d = mock(Graphics2D.class);
        AwtGraphicsStream awtStream = new AwtGraphicsStream(g2d);
        CoordinateSystem coordinates = new CoordinateSystem(Point.of(42, 42), 0.5);

        awtStream.pushCoordinateSystem(coordinates);
        awtStream.pushCoordinateSystem(CoordinateSystem.identity());
        awtStream.popCoordinateSystem();

        verify(g2d, new LastCallOnMethod()).translate(42, 42);
        verify(g2d, new LastCallOnMethod()).scale(0.5, 0.5);
    }
}
