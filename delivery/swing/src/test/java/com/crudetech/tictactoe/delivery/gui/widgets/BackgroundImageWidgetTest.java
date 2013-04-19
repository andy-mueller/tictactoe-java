package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import static com.crudetech.collections.Iterables.firstOf;
import static com.crudetech.matcher.RangeHasSize.hasSizeOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class BackgroundImageWidgetTest {

    private TicTacToeGridModel model;
    private BackgroundImageWidget widget;
    private Image image;
    private AlphaValue alphaValue;

    @Before
    public void setUp() throws Exception {
        image = mock(Image.class);
        alphaValue = new AlphaValue(0.42f);
        model = new TicTacToeGridModel();
        widget = new BackgroundImageWidget(model, image, alphaValue);
    }

    @Test
    public void givenNothingIsHighlighted_ImageIsNotTransparent() throws Exception {
        model.unHighlightThreeInARow();

        Iterable<Widget> actualWidget = widget.subWidgets();

        assertThat(actualWidget, hasSizeOf(1));
        assertThat(firstOf(actualWidget), is(instanceOf(ImageWidget.class)));
    }

    @Test
    public void given3InARowIsHighlighted_ImageIsTransparent() throws Exception {
        model.highlightThreeInARow(diagonal());

        Iterable<Widget> actualWidget = widget.subWidgets();

        assertThat(actualWidget, hasSizeOf(1));
        assertThat(firstOf(actualWidget), is(instanceOf(CompositeDecoratorWidget.class)));
        assertThat(((CompositeDecoratorWidget) firstOf(actualWidget)).getDecorated(), is(instanceOf(ImageWidget.class)));
    }

    private Grid.ThreeInARow diagonal() {
        return Grid.ThreeInARow.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
        );
    }

    @Test
    public void givenNothingIsHighlighted_imageLocationWillBeAtOrigin() {
        Widget actualWidget = firstOf(widget.subWidgets());

        assertThat(actualWidget.widgetCoordinates(), is(CoordinateSystem.idenitity()));
    }

    @Test
    public void givenNothingIsHighlighted_locationImageWillBeUsedInImageWidget() {
        Widget actualWidget = firstOf(widget.subWidgets());

        Widget expectedImageWidget = expectedImageWidget();

        assertThat(actualWidget, is(expectedImageWidget));
    }

    private ImageWidget expectedImageWidget() {
        return new ImageWidget(image);
    }

    @Test
    public void givenHighlight_alphaValueIsApplied() {
        model.highlightThreeInARow(diagonal());

        CompositeDecoratorWidget actualWidget = (CompositeDecoratorWidget) firstOf(widget.subWidgets());

        Widget expectedWidget = new CompositeDecoratorWidget(expectedImageWidget(), alphaValue);
        assertThat(actualWidget, is(expectedWidget));
    }
    //custom interface for highlight query
}
