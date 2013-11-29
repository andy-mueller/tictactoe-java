package com.crudetech.tictactoe.delivery.gui.widgets;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.*;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.crudetech.query.Query.from;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridWidgetTest {

    private static final int CellDimension = 100;
    private Style style;
    private TicTacToeGridModel model;

    private static final Color Orange = new Color() {
    };
    private static final Rectangle widgetBoundary = new Rectangle(1, 1, 750, 500);

    @Before
    public void setUp() throws Exception {
        style = StyleStub.builder()
                .withBackgroundImageSize(500, 400)
                .withCellSize(CellDimension, CellDimension)
                .build();

        model = new TicTacToeGridModel(LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross
        ));
    }


    @Test
    public void givenWidgetIsLargerThanBackgroundImage_ImageIsCentered() {
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
                .withBounds(widgetBoundary)
                .withStyle(style)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();
        List<Widget> widgets = widget.buildPaintList();

        Widget backGroundImage = widgets.get(1);
        assertThat(backGroundImage.widgetCoordinates(), is(new CoordinateSystem(Point.of(125, 50))));
    }

    @Test
    public void givenWidgetIsSmallerThanBackgroundImage_ImageIsAtOrigin() {
        Style styleWithBiggerBackground =
                StyleStub.builder()
                        .withBackgroundImageSize(500, 1000)
                        .withCellSize(100, 200)
                        .build();
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
                .withBounds(widgetBoundary)
                .withStyle(styleWithBiggerBackground)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();

        Widget backgroundImage = widget.buildPaintList().get(1);
        assertThat(backgroundImage.widgetCoordinates(), is(new CoordinateSystem(Point.of(125, 0), CoordinateSystem.NoScale)));
    }

    @Test
    public void backGroundIsInvalidated() {
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
                .withBounds(widgetBoundary)
                .withStyle(style)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();

        List<Widget> widgets = widget.buildPaintList();
        Widget background = widgets.get(0);

        Widget expectedBackground = getExpectedBackground(750, 500);
        assertThat(background, is(expectedBackground));
    }

    private Widget getExpectedBackground(int width, int height) {
        return new FilledRectangleWidget(new Rectangle(0, 0, width, height + 500), style.getBackgroundColor());
    }

    @Test
    public void paintListIsPaintedInOrder() {
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
                .withBounds(widgetBoundary)
                .withStyle(style)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();
        List<Widget> widgets = widget.buildPaintList();

        List<Class<? extends Widget>> widgetClasses = from(widgets).select(toClass()).toList();

        List<Class<? extends Widget>> expectedList = new ArrayList<>();
        expectedList.add(FilledRectangleWidget.class);
        expectedList.add(StatefulTransparencyImageWidget.class);
        expectedList.add(TicTacToeGridCellsWidget.class);
        expectedList.add(TicTacToeGridHighlightedCellWidget.class);
        expectedList.add(EmptyWidget.class);

        assertThat(widgetClasses, is(equalTo(expectedList)));
        assertThat(widgetClasses, is(expectedList));
    }


    private UnaryFunction<Widget, Class<? extends Widget>> toClass() {
        return new UnaryFunction<Widget, Class<? extends Widget>>() {
            @Override
            public Class<? extends Widget> execute(Widget widget) {
                return widget.getClass();
            }
        };
    }

    @Test
    public void debugIsNotPaintedIfDebugModeIsOf() {
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
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
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
                .withBounds(new Rectangle(0, 0, 500, 600))
                .withStyle(style)
                .withModel(model)
                .setDebugModeOn(Orange)
                .createTicTacToeGridWidget();

        List<Widget> widgets = widget.buildPaintList();
        boolean hasDebugWidget = from(widgets).select(classOf()).where(isKindOf(TicTacToeGridWidget.DebugWidget.class)).any();
        assertThat(hasDebugWidget, is(true));
    }

    @Test
    public void givenImageThatFitsInWidget_CellWidgetAndImageOverlap() throws Exception {
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
                .withBounds(widgetBoundary)
                .withStyle(style)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();

        Widget backgroundImage = widget.backgroundImageWidget();
        Widget cellWidget = widget.gridCellsWidget();

        assertThat(cellWidget.widgetCoordinates(), is(backgroundImage.widgetCoordinates()));
    }

    @Test
    public void givenScaledWidget_CellRectanglesAreMappedFromLocations() {
        TicTacToeGridWidget widget = TicTacToeGridWidget.builder()
                .withBounds(widgetBoundary)
                .withStyle(style)
                .withModel(model)
                .noDebug()
                .createTicTacToeGridWidget();
//        widget.widgetCoordinates()
//                .setLocation(Point.of(10, 20))
//                .setScale(0.5);

        Iterable<Rectangle> cellBoundaries = widget.getCellBoundaries(asList(Grid.Location.of(Grid.Row.Third, Grid.Column.Third)));

        final int horizontalCellPadding = (widgetBoundary.width - 3 * CellDimension) / 4;
        Rectangle bl = new Rectangle((int) ((widgetBoundary.width - horizontalCellPadding) * 0.5) + 10, 0, 0, 0);

        assertThat(cellBoundaries, is(equalTo((Iterable) asList(bl))));
    }
}
