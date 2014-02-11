package com.crudetech.gui.widgets;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BoundedTextWidgetTest {

    private static final String SHORT_TEXT = "short text";
    public static final Rectangle VERY_LARGE_BOUNDARY = new Rectangle(2, 3, 10000, 10000);

    @Test
    public void givenTextIsSmallerThanBoundary_textIsPaintedAsItIs() throws Exception {
        BoundedTextWidget boundedTextWidget = createBoundedTextWidgetWithTextThatFits();
        GraphicsStream pipe = mock(GraphicsStream.class);

        boundedTextWidget.paint(pipe);

        verify(pipe).drawText(0, 0, SHORT_TEXT);
    }

    private BoundedTextWidget createBoundedTextWidgetWithTextThatFits() {
        Font smallFont = new FontStub(2);
        return new BoundedTextWidget(smallFont, VERY_LARGE_BOUNDARY, SHORT_TEXT);
    }

    static class BoundedTextWidget extends EcsWidget {
        private final Rectangle boundary;
        private final String text;
        private final Font font;

        public BoundedTextWidget(Font font, Rectangle boundary, String text) {
            super(boundary.x, boundary.y);
            this.font = font;
            this.boundary = boundary.setLocation(0, 0);
            this.text = text;
        }

        @Override
        protected void paintEcs(GraphicsStream pipe) {
            pipe.pushFont(font);
            pipe.drawText(0, 0, text);
        }
    }

    @Test
    public void givenABoundaryWithAnOffset_widgetLocationsIsAdjusted() throws Exception {
        BoundedTextWidget boundedTextWidget = createBoundedTextWidgetWithTextThatFits();

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

    //    @Test
    public void givenAFontThatIsHigherThanTheBoundingBox_fontHeightIsAdjusted() throws Exception {
        Rectangle flatBound = new Rectangle(2, 3, 10000, 5);
        Font highFont = new FontStub(flatBound.height * 2);
        BoundedTextWidget widget = new BoundedTextWidget(highFont, flatBound, "some text");
        GraphicsStream pipe = mock(GraphicsStream.class);

        widget.paint(pipe);

        verify(pipe).pushFont(fontWithHeightLessOrEqualTo(flatBound.height));
    }

    private static Font fontWithHeightLessOrEqualTo(int expectedMaxHeight) {
        return argThat(
                new FeatureMatcher<Font, Integer>(lessThanOrEqualTo(expectedMaxHeight), "Font with height", "height") {
                    @Override
                    protected Integer featureValueOf(Font actual) {
                        return actual.getHeight();
                    }
                }
        );
    }

    static class FontStub implements Font {
        private final int height;

        public FontStub(int height) {
            this.height = height;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public String toString() {
            return "FontStub{" +
                    "height=" + height +
                    '}';
        }
    }
    // pushes font
    // pop font??->Alternative: restoration
    // adjustFontHeight when necessary
    // adjust font size when to wide
    // throws when lower/upper font size limit is reached
    //      ->later maybe scale? Encapsulate Sizing in sub class!
}
