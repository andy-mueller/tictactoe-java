package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.Point;
import com.crudetech.lang.Compare;
import com.crudetech.tictactoe.delivery.swing.LastCallOnMock;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static org.mockito.Mockito.*;

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

    @Test
    public void givenCoordinatesArePopped_PreviousCoordinatesAreRestoredOnGraphics() throws Exception {
        Graphics2D g2d = mock(Graphics2D.class);
        AffineTransform uniqueBaseTransform = createUniqueTransform();
        when(g2d.getTransform()).thenReturn(uniqueBaseTransform);

        AwtGraphicsStream awtStream = new AwtGraphicsStream(g2d);
        CoordinateSystem otherCoordinates = someCoordinates();

        awtStream.pushCoordinateSystem(otherCoordinates);
        awtStream.popCoordinateSystem();

        verify(g2d, new LastCallOnMock()).setTransform(uniqueBaseTransform);
    }

    private CoordinateSystem someCoordinates() {
        return new CoordinateSystem(Point.of(42, 42), 0.5);
    }

    private AffineTransform createUniqueTransform() {
        return new AffineTransform(new float[]{
                2f, 1f, 4f,
                6f, 1f, 1f,
                1f, 0.1f, 9f
        });
    }

    @Test
    public void givenAlphaIsPushed_ConvertedCompositeIsSetOnGraphics() throws Exception {
        Graphics2D g2d = mock(Graphics2D.class);
        AlphaComposite uniqueBaseAlpha = createUniqueAlpha();
        when(g2d.getComposite()).thenReturn(uniqueBaseAlpha);

        AwtGraphicsStream awtStream = new AwtGraphicsStream(g2d);
        awtStream.pushAlpha(anyAlpha());

        verify(g2d).setComposite(withAlpha(anyAlpha()));
    }

    @Test
    public void givenAlphaIsPopped_PreviousAlphaIsRestoredOnGraphics() throws Exception {
        Graphics2D g2d = mock(Graphics2D.class);
        AlphaComposite uniqueBaseAlpha = createUniqueAlpha();
        when(g2d.getComposite()).thenReturn(uniqueBaseAlpha);

        AwtGraphicsStream awtStream = new AwtGraphicsStream(g2d);
        awtStream.pushAlpha(anyAlpha());

        awtStream.popAlpha();

        verify(g2d, new LastCallOnMock()).setComposite(uniqueBaseAlpha);
    }

    private AlphaComposite withAlpha(final AlphaValue anyAlpha) {
        return Mockito.argThat(new TypeSafeDiagnosingMatcher<AlphaComposite>() {
            @Override
            protected boolean matchesSafely(AlphaComposite item, Description mismatchDescription) {
                return Compare.equals(item.getAlpha(), anyAlpha.alpha);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Similar alpha");
            }
        });
    }

    private AlphaValue anyAlpha() {
        return new AlphaValue(0.5f);
    }

    private AlphaComposite createUniqueAlpha() {
        return AlphaComposite.getInstance(AlphaComposite.DST_ATOP, 0.1f);
    }
}
