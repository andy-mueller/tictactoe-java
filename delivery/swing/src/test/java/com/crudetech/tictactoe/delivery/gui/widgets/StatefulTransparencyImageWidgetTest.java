package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.*;
import org.junit.Before;
import org.junit.Test;

import static com.crudetech.collections.Iterables.firstOf;
import static com.crudetech.matcher.RangeHasSize.hasSizeOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class StatefulTransparencyImageWidgetTest {

    private TransparencyStub transparency;
    private StatefulTransparencyImageWidget widget;
    private Image image;
    private AlphaValue alphaValue;

    class TransparencyStub implements StatefulTransparencyImageWidget.TransparencyState {
        private boolean transparency = false;

        void makeTransparent(){
            transparency = true;
        }
        @Override
        public boolean isTransparent() {
            return transparency;
        }

        @Override
        public AlphaValue transparency() {
            return alphaValue;
        }
    }

    @Before
    public void setUp() throws Exception {
        image = mock(Image.class);
        alphaValue = new AlphaValue(0.42f);
        transparency = new TransparencyStub();

        widget = new StatefulTransparencyImageWidget(transparency, image);
    }

    @Test
    public void givenNoTransparency_ImageIsNotTransparent() throws Exception {

        Iterable<Widget> actualWidget = widget.subWidgets();

        assertThat(actualWidget, hasSizeOf(1));
        assertThat(firstOf(actualWidget), is(instanceOf(ImageWidget.class)));
    }

    @Test
    public void givenTransparency_ImageIsTransparent() throws Exception {
        transparency.makeTransparent();

        Iterable<Widget> actualWidget = widget.subWidgets();

        assertThat(actualWidget, hasSizeOf(1));
        assertThat(firstOf(actualWidget), is(instanceOf(CompositeDecoratorWidget.class)));
    }
    @Test
    public void givenNoTransparency_imageLocationWillBeAtOrigin() {
        Widget actualWidget = firstOf(widget.subWidgets());

        assertThat(actualWidget.widgetCoordinates(), is(CoordinateSystem.identity()));
    }

    @Test
    public void givenNoTransparency_locationImageWillBeUsedInImageWidget() {
        Widget actualWidget = firstOf(widget.subWidgets());

        assertThat(actualWidget, is(expectedImageWidget()));
    }

    private Widget expectedImageWidget() {
        return new ImageWidget(image);
    }

    @Test
    public void givenTransparency_alphaValueIsApplied() {
        transparency.makeTransparent();

        CompositeDecoratorWidget actualWidget = (CompositeDecoratorWidget) firstOf(widget.subWidgets());

        Widget expectedWidget = new CompositeDecoratorWidget<>(expectedImageWidget(), alphaValue);
        assertThat(actualWidget, is(expectedWidget));
    }
}
