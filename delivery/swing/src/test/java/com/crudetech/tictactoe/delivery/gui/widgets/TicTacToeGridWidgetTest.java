package com.crudetech.tictactoe.delivery.gui.widgets;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static com.crudetech.query.Query.from;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridWidgetTest {
    private final int paintOffsetX = 250;
    private final int paintOffsetY = 500;

    private Style style;
    private TicTacToeGridModel model;

    private static final Color Orange = new Color() {
    };

    @Before
    public void setUp() throws Exception {
        style = new StyleStub();

        model = new TicTacToeGridModel(LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross
        ));
    }

    @Test
    public void backgroundIsPositionedAtOriginIfComponentIsSmaller() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(1, 1, 10, 10))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();

        Widget backgroundImage = widget.buildPaintList().get(1);
        assertThat(backgroundImage.widgetCoordinates(), is(CoordinateSystem.world()));
    }

    @Test
    public void backGroundIsInvalidated() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 500, 600))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();

        List<Widget> widgets = widget.buildPaintList();
        Widget background = widgets.get(0);

        Widget expectedBackground = getExpectedBackground(500, 600);
        assertThat(background, is(expectedBackground));
    }

    private Widget getExpectedBackground(int width, int height) {
        return new FilledRectangleWidget(new Rectangle(0, 0, width, height + 500), style.getBackgroundColor());
    }

    @Test
    public void backGroundIsPaintedInMiddleOfComponent() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 1000, 2000))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();
        List<Widget> widgets = widget.buildPaintList();

        ImageWidget backGroundImage = (ImageWidget) widgets.get(1);
        assertThat(backGroundImage.widgetCoordinates(), is(new CoordinateSystem(Point.of(250, 500))));
    }

    @Test
    public void gridMarksArePaintedFromModel() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 1000, 2000))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();
        List<Widget> widgets = widget.gridMarkWidgetList();

        List<Widget> expected = expectedGridMarkWidgets();

        assertThat(widgets, is(equivalentTo(expected)));
    }

    private List<Widget> expectedGridMarkWidgets() {
        final Image cross = style.getCrossImage();
        final Image nought = style.getNoughtImage();
        Rectangle[][] locations = style.getGridMarkLocations();

        return Arrays.<Widget>asList(
                new ImageWidget(loc(locations[0][0].getLocation()), cross),
                new ImageWidget(loc(locations[0][1].getLocation()), nought),
                new EmptyWidget(),

                new ImageWidget(loc(locations[1][0].getLocation()), cross),
                new EmptyWidget(),
                new EmptyWidget(),

                new ImageWidget(loc(locations[2][0].getLocation()), nought),
                new ImageWidget(loc(locations[2][1].getLocation()), nought),
                new ImageWidget(loc(locations[2][2].getLocation()), cross)
        );
    }

    private Point loc(Point location) {
        return new Point(location.x + paintOffsetX, location.y + paintOffsetY);
    }

    @Test
    public void backgroundImageIsInMiddle() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 1000, 2000))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();
        List<Widget> widgets = widget.buildPaintList();
        Widget backgroundImage = widgets.get(1);

        assertThat(backgroundImage, is((Widget) getExpectedBackgroundImage()));
    }

    @Test
    public void paintListIsPaintedInOrder() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 1000, 2000))
                .withStyle(style)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();
        List<Widget> widgets = widget.buildPaintList();

        List<Widget> expectedList = new ArrayList<>();
        expectedList.add(getExpectedBackground());
        expectedList.add(getExpectedBackgroundImage());
        expectedList.addAll(expectedGridMarkWidgets());
        expectedList.add(new EmptyWidget());
        expectedList.add(new EmptyWidget());

        assertThat(widgets, is(expectedList));
    }

    private ImageWidget getExpectedBackgroundImage() {
        return new ImageWidget(new Point(paintOffsetX, paintOffsetY), style.getBackgroundImage());
    }

    private Widget getExpectedBackground() {
        return new FilledRectangleWidget(new Rectangle(0, 0, 1000, 2000 + 500), style.getBackgroundColor());
    }

    @Test
    public void highlightedRectangleIsAddedWhenModelIsHighlighted() {
        Grid.Location highlightedLocation = Grid.Location.of(Grid.Row.First, Grid.Column.Third);
        model.highlightCell(highlightedLocation);
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 1000, 2000))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();


        List<Widget> widgets = widget.buildPaintList();

        Rectangle rect = style.getGridMarkLocations()[highlightedLocation.getRow().position()][highlightedLocation.getColumn().position()];
        rect = rect.translate(StyleStub.Width / 2, StyleStub.Height / 2);
        Widget expectHighlightRectangle = new RectangleWidget(rect, style.getHighlightColor());

        assertThat(widgets.get(widgets.size() - 2), is(expectHighlightRectangle));
    }

    @Test
    public void allNonWinningTripleAreTransparent() {
        Grid.ThreeInARow diagonal = Grid.ThreeInARow.of(Grid.Mark.Nought,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
        model.highlightThreeInARow(diagonal);

        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 1000, 2000))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();

        List<Widget> widgets = widget.gridMarkWidgetList();

        List<Widget> expected = expectedGridMarkWidgetsWithHighlight();
        assertThat(widgets, is(equivalentTo(expected)));
    }

    private List<Widget> expectedGridMarkWidgetsWithHighlight() {
        final Image cross = style.getCrossImage();
        final Image nought = style.getNoughtImage();
        Rectangle[][] locations = style.getGridMarkLocations();

        return asList(
                new ImageWidget(loc(locations[0][0].getLocation()), cross),
                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[0][1].getLocation()), nought), TicTacToeGridWidget.WinningTripleAlpha),
                new CompositeDecoratorWidget(
                        new EmptyWidget(), TicTacToeGridWidget.WinningTripleAlpha),

                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[1][0].getLocation()), cross), TicTacToeGridWidget.WinningTripleAlpha),
                new EmptyWidget(),
                new CompositeDecoratorWidget(
                        new EmptyWidget(), TicTacToeGridWidget.WinningTripleAlpha),

                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[2][0].getLocation()), nought), TicTacToeGridWidget.WinningTripleAlpha),
                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[2][1].getLocation()), nought), TicTacToeGridWidget.WinningTripleAlpha),
                new ImageWidget(loc(locations[2][2].getLocation()), cross)
        );
    }


    @Test
    public void backgroundImageIsTransparentWhenWinningTripleIsSet() {
        Grid.ThreeInARow diagonal = Grid.ThreeInARow.of(Grid.Mark.Nought,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
        model.highlightThreeInARow(diagonal);

        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 500, 600))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();

        List<Widget> widgets = widget.gridMarkWidgetList();

        assertThat(widgets.get(1), is(instanceOf(CompositeDecoratorWidget.class)));
    }


    @Test
    public void backgroundImageIsNotTransparentWhenNoWinningTripleIsSet() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 500, 600))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();


        List<Widget> widgets = widget.buildPaintList();

        assertThat(widgets.get(1), is(instanceOf(ImageWidget.class)));
    }


    @Test
    public void debugIsNotPaintedIfDebugModeIsOf() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 500, 600))
                .withStyle(style)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();

        List<Widget> widgets = widget.buildPaintList();
        boolean hasDebugWidget = from(widgets).select(classOf()).where(isKindOf(TicTacToeGridWidget.DebugWidget.class)).any();
        assertThat(hasDebugWidget, is(false));
    }

    private <T> UnaryFunction<T, Class<T>> classOf() {
        return new UnaryFunction<T, Class<T>>() {
            @SuppressWarnings("unchecked")
            @Override
            public Class<T> execute(T t) {
                return (Class<T>) t.getClass();
            }
        };
    }

    private UnaryFunction<Class<?>, Boolean> isKindOf(final Class<?> rhs) {
        return new UnaryFunction<Class<?>, Boolean>() {
            @Override
            public Boolean execute(Class<?> clazz) {
                return rhs.isAssignableFrom(clazz);
            }
        };
    }

    @Test
    public void debugIsOnlyPaintedIfDebugModeIsOn() {
        TicTacToeGridWidget widget = new TicTacToeGridWidgetBuilder()
                .withBounds(new Rectangle(0, 0, 500, 600))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();

        List<Widget> widgets = widget.buildPaintList();
        boolean hasDebugWidget = from(widgets).select(classOf()).where(isKindOf(TicTacToeGridWidget.DebugWidget.class)).any();
        assertThat(hasDebugWidget, is(true));
    }
}
