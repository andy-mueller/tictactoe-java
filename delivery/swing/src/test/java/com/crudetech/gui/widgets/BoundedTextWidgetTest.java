package com.crudetech.gui.widgets;

import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.lang.Compare;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.atLeastOnce;

@RunWith(Features.class)
public class BoundedTextWidgetTest {

    private static final String SHORT_TEXT = "short text";
    private static final Rectangle VERY_LARGE_BOUNDARY = new Rectangle(2, 3, 10000, 10000);

    @Rule
    public GraphicStreamMockery streamMockery = GraphicStreamMockery.withOnlyOneSubContext();

    @Test
    public void givenTextIsSmallerThanBoundary_textIsPaintedAsItIs() throws Exception {
        BoundedTextWidget boundedTextWidget = createBoundedTextWidgetWithTextThatFits();

        boundedTextWidget.paint(streamMockery.stream());

        streamMockery.verifyOnLastSubContext().drawText(0, 0, SHORT_TEXT);
    }

    private static BoundedTextWidget createBoundedTextWidgetWithTextThatFits() {
        Font smallFont = new FontStub(2);
        return new BoundedTextWidget(smallFont, VERY_LARGE_BOUNDARY, SHORT_TEXT);
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

    @Test
    public void givenAFontThatIsHigherThanTheBoundingBox_fontHeightIsAdjusted() throws Exception {
        Rectangle flatBound = new Rectangle(2, 3, 10000, 5);
        Font highFont = new FontStub(flatBound.height * 2);
        BoundedTextWidget widget = new BoundedTextWidget(highFont, flatBound, SHORT_TEXT);

        widget.paint(streamMockery.stream());


        streamMockery.verifyOnLastSubContext(atLeastOnce()).pushCoordinateSystem(widget.coordinateSystem());
        streamMockery.verifyOnLastSubContext().pushCoordinateSystem(withScale(0.5));
    }

    private CoordinateSystem withScale(double scale) {
        return argThat(
                new FeatureMatcher<CoordinateSystem, Double>(doubleEqualTo(scale), "CoordinateSystem with scale", "scale") {
                    @Override
                    protected Double featureValueOf(CoordinateSystem actual) {
                        return actual.getScale();
                    }
                }
        );
    }

    private Matcher<? super Double> doubleEqualTo(final double d) {
        return new TypeSafeDiagnosingMatcher<Double>() {

            private final double tolerance = 1e-3;

            @Override
            protected boolean matchesSafely(Double item, Description mismatchDescription) {
                if (Compare.equals(d, item, tolerance)) {
                    return true;
                }
                mismatchDescription.appendText("was not within the tolerance of " + tolerance);
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("equal to <%s> within the tolerance of <%s>", d, tolerance));
            }
        };
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

    @Feature(AdheresToGraphicsStreamProtocol.class)
    public static AdheresToGraphicsStreamProtocol.Factory adheresToStreamProtocol() {
        return new AdheresToGraphicsStreamProtocol.Factory() {
            @Override
            public Widget createWidget() {
                return createBoundedTextWidgetWithTextThatFits();
            }
        };
    }

    // pop font??->Alternative: restoration
    // adjustFontHeight when necessary
    // adjust font size when to wide
    // throws when lower/upper font size limit is reached
    //      ->later maybe scale? Encapsulate Sizing in sub class!
}
