package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static com.crudetech.query.Query.from;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TicTacToeGridUITest {

    private Graphics2D g2d;
    private JTicTacToeGrid grid;
    private TicTacToeGridUI ui;
    private Style style;
    private final int paintOffsetX = 250;
    private final int paintOffsetY = 500;

    @Before
    public void setUp() throws Exception {
        g2d = mock(Graphics2D.class);
        when(g2d.getTransform()).thenReturn(new AffineTransform());
        TicTacToeGridModel model = new TicTacToeGridModel(
                LinearRandomAccessGrid.of(
                        Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                        Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                        Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross
                ));

        grid = new JTicTacToeGrid(model);
        grid.setSize(1000, 2000);

        ui = grid.getUI();

        style = new StyleStub();
        ui.setStyle(style);
    }

    @Test
    public void backGroundIsPaintedInMiddleOfComponent() {
        List<Widget> widgets = ui.buildPaintList();

        ImageWidget backGroundImage = (ImageWidget) widgets.get(1);
        assertThat(backGroundImage.getLocation(), is(new Point(250, 500)));
    }

    @Test
    public void backgroundIsPositionedAtOriginIfComponentIsSmaller() {
        grid.setSize(10, 10);
        ui.paint(g2d);

        verify(g2d).drawImage(style.getBackgroundImage(), null, 0, 0);
    }

    @Test
    public void backGroundIsInvalidated() {
        ui.paint(g2d);

        List<Widget> widgets = ui.buildPaintList();
        Widget background = widgets.get(0);

        Widget expectedBackground = getExpectedBackground();
        assertThat(background, is(expectedBackground));
    }

    @Test
    public void defaultStyleIsBrush() {
        grid = new JTicTacToeGrid();
        assertThat(Styles.Brush, is(grid.getUI().getStyle()));
    }

    @Test
    public void preferredSizeIsStyleSize() throws Exception {
        Dimension expected = style.getPreferredSize();

        assertThat(grid.getUI().getPreferredSize(grid), is(expected));
    }

    @Test
    public void gridMarksArePaintedFromModel() {
        List<Widget> widgets = ui.gridMarkWidgetList();

        List<Widget> expected = expectedGridMarkWidgets();

        assertThat(widgets, is(equivalentTo(expected)));
        assertThat(grid.getModel().hasHighlightedTriple(), is(false));
    }

    private List<Widget> expectedGridMarkWidgets() {
        final BufferedImage cross = style.getCrossImage();
        final BufferedImage nought = style.getNoughtImage();
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
    public void backgroundIsInMiddle() {
        List<Widget> widgets = ui.buildPaintList();
        Widget backgroundImage = widgets.get(1);

        assertThat(backgroundImage, is((Widget) getExpectedBackgroundImage()));
    }

    @Test
    public void paintListIsPaintedInOrder() {
        List<Widget> widgets = ui.buildPaintList();

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
        return new FilledRectangleWidget(new Rectangle(0, 0, grid.getWidth(), grid.getHeight() + 500), style.getBackgroundColor());
    }

    @Test
    public void highlightedRectangleIsAddedWhenModelIsHighlighted() {
        grid.getModel().highlightCell(Grid.Location.of(Grid.Row.First, Grid.Column.Third));
        List<Widget> widgets = ui.buildPaintList();


        Rectangle rect = (Rectangle) style.getGridMarkLocations()[0][2].clone();
        rect.translate(StyleStub.Width / 2, StyleStub.Height / 2);
        assertThat(widgets.get(widgets.size() - 2), is((Widget) new RectangleWidget(rect, style.getHighlightColor())));
    }

    @Test
    public void allNonWinningTripleAreTransparent() {
        Grid.Triple diagonal = Grid.Triple.of(Grid.Mark.Nought,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
        grid.getModel().highlightTriple(diagonal);

        List<Widget> widgets = ui.gridMarkWidgetList();

        List<Widget> expected = expectedGridMarkWidgetsWithHighlight();
        assertThat(widgets, is(equivalentTo(expected)));
    }
    @Test
    public void backgroundImageIsTransparentWhenWinningTripleIsSet() {
        Grid.Triple diagonal = Grid.Triple.of(Grid.Mark.Nought,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
        grid.getModel().highlightTriple(diagonal);

        List<Widget> widgets = ui.buildPaintList();

        assertThat(widgets.get(1), is(instanceOf(CompositeDecoratorWidget.class)));
    }
    @Test
    public void backgroundImageIsNotTransparentWhenNoWinningTripleIsSet() {
        grid.getModel().unHighlightTriple();

        List<Widget> widgets = ui.buildPaintList();

        assertThat(widgets.get(1), is(instanceOf(ImageWidget.class)));
    }

    private List<Widget> expectedGridMarkWidgetsWithHighlight() {
        final BufferedImage cross = style.getCrossImage();
        final BufferedImage nought = style.getNoughtImage();
        Rectangle[][] locations = style.getGridMarkLocations();

        return asList(
                new ImageWidget(loc(locations[0][0].getLocation()), cross),
                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[0][1].getLocation()), nought), TicTacToeGridUI.WinningTripleAlpha),
                new CompositeDecoratorWidget(
                        new EmptyWidget(), TicTacToeGridUI.WinningTripleAlpha),

                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[1][0].getLocation()), cross), TicTacToeGridUI.WinningTripleAlpha),
                new EmptyWidget(),
                new CompositeDecoratorWidget(
                        new EmptyWidget(), TicTacToeGridUI.WinningTripleAlpha),

                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[2][0].getLocation()), nought), TicTacToeGridUI.WinningTripleAlpha),
                new CompositeDecoratorWidget(
                        new ImageWidget(loc(locations[2][1].getLocation()), nought), TicTacToeGridUI.WinningTripleAlpha),
                new ImageWidget(loc(locations[2][2].getLocation()), cross)
        );
    }


    @Test
    public void debugIsNotPaintedIfDebugModeIsNotOn() {
        List<Widget> widgets = ui.buildPaintList();
        boolean hasDebugWidget = from(widgets).select(classOf()).where(isKindOf(TicTacToeGridUI.DebugWidget.class)).any();
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
        ui.turnOnDebug();
        List<Widget> widgets = ui.buildPaintList();
        boolean hasDebugWidget = from(widgets).select(classOf()).where(isKindOf(TicTacToeGridUI.DebugWidget.class)).any();
        assertThat(hasDebugWidget, is(true));
    }
}
