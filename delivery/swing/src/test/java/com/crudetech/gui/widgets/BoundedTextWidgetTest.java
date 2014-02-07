package com.crudetech.gui.widgets;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BoundedTextWidgetTest {

    private static final String SHORT_TEXT = "short text";
    public static final Rectangle VERY_LARGE_BOUNDARY = new Rectangle(2, 3, 10000, 10000);

    @Test
    public void givenTextIsSmallerThanBoundary_textIsPaintedAsItIs() throws Exception {
        BoundedTextWidget boundedTextWidget = new BoundedTextWidget(VERY_LARGE_BOUNDARY, SHORT_TEXT);
        GraphicsStream pipe = mock(GraphicsStream.class);

        boundedTextWidget.paint(pipe);

        verify(pipe).drawText(0, 0, SHORT_TEXT);
    }

    static class BoundedTextWidget extends EcsWidget {
        private final Rectangle boundary;
        private final String text;

        public BoundedTextWidget(Rectangle boundary, String text) {
            super(boundary.x, boundary.y);
            this.boundary = boundary.setLocation(0, 0);
            this.text = text;
        }

        @Override
        protected void paintEcs(GraphicsStream pipe) {
            pipe.drawText(0, 0, text);
        }
    }

    @Test
    public void givenABoundaryWithAnOffset_WidgetLocationsIsAdjusted() throws Exception {
        BoundedTextWidget boundedTextWidget = new BoundedTextWidget(VERY_LARGE_BOUNDARY, SHORT_TEXT);

        assertThat(boundedTextWidget.coordinateSystem(), hasLocation(VERY_LARGE_BOUNDARY.getLocation()));
    }

    private static Matcher<? super CoordinateSystem> hasLocation(Point location) {
        return new FeatureMatcher<CoordinateSystem, Point>(is(location), "CoordinateSystem with location", "location") {
            @Override
            protected Point featureValueOf(CoordinateSystem actual) {
                return actual.getLocation();
            }
        };
    }
    // pushes font
    // adjustFontHeight when necessary
    // adjust font size when to wide
    // throws when lower/upper font size limit is reached
    //      ->later maybe scale? Encapsulate Sizing in sub class!
}
