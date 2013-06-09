package com.crudetech.tictactoe.delivery.gui.widgets;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.Point;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.crudetech.matcher.RangeIsEqual.equalTo;
import static com.crudetech.query.Query.from;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridCellsWidgetTest {

    private Style style;
    private TicTacToeGridModel gridModel;
    private static AlphaValue alphaValue = new AlphaValue(0.42f);

    @Before
    public void setUp() throws Exception {
        style = StyleStub.builder()
                .withBackgroundImageSize(500, 400)
                .withCellSize(100, 100)
                .build();
        gridModel = new TicTacToeGridModel(
                LinearRandomAccessGrid.of(
                        Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                        Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
                        Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Cross
                )
        );
    }

    @Test
    public void cellsAreSetupAccordingToModel() throws Exception {

        TicTacToeGridCellsWidget widget = new TicTacToeGridCellsWidget(gridModel, style);

        Iterable<Class<?>> cellWidgetsClasses = from(widget.getCells()).select(toClass());

        List<Class<?>> expectedCellTypes = new ArrayList<>();
        expectedCellTypes.addAll(asList(TicTacToeGridCellsWidget.Cross.class, TicTacToeGridCellsWidget.None.class, TicTacToeGridCellsWidget.Nought.class));
        expectedCellTypes.addAll(asList(TicTacToeGridCellsWidget.None.class, TicTacToeGridCellsWidget.Nought.class, TicTacToeGridCellsWidget.None.class));
        expectedCellTypes.addAll(asList(TicTacToeGridCellsWidget.None.class, TicTacToeGridCellsWidget.Cross.class, TicTacToeGridCellsWidget.Cross.class));

        assertThat(cellWidgetsClasses, is(equalTo(expectedCellTypes)));
    }

    @Test
    public void cellsAreLocatedAtCorrectPosition() throws Exception {

        TicTacToeGridCellsWidget widget = new TicTacToeGridCellsWidget(gridModel, style);

        Iterable<Point> realCellLocations = from(widget.getCells()).select(toCoordinates()).select(toLocation());


        Iterable<Point> expectedLocations = asList(
                Point.of(50, 25), Point.of(200, 25), Point.of(350, 25),
                Point.of(50, 150), Point.of(200, 150), Point.of(350, 150),
                Point.of(50, 275), Point.of(200, 275), Point.of(350, 275)
        );

        assertThat(expectedLocations, is(equalTo(realCellLocations)));
    }

    private UnaryFunction<CoordinateSystem, Point> toLocation() {
        return new UnaryFunction<CoordinateSystem, Point>() {
            @Override
            public Point execute(CoordinateSystem coordinateSystem) {
                return coordinateSystem.getLocation();
            }
        };
    }

    private UnaryFunction<TicTacToeGridCellsWidget.CellWidget<?>, CoordinateSystem> toCoordinates() {
        return new UnaryFunction<TicTacToeGridCellsWidget.CellWidget<?>, CoordinateSystem>() {
            @Override
            public CoordinateSystem execute(TicTacToeGridCellsWidget.CellWidget<?> cellWidget) {
                return cellWidget.widgetCoordinates();
            }
        };
    }

    private UnaryFunction<Object, Class<?>> toClass() {
        return new UnaryFunction<Object, Class<?>>() {
            @Override
            public Class<?> execute(Object o) {
                return o.getClass();
            }
        };
    }

    private static final Grid.Location middleCell = Grid.Location.of(Grid.Row.Second, Grid.Column.Second);
    TicTacToeGridModel model = new TicTacToeGridModel();

    @Test
    public void givenCrossWidget_ImageIsUsedFromStyle() throws Exception {
        TicTacToeGridCellsWidget.Cross crossWidget = new TicTacToeGridCellsWidget.Cross(middleCell, model, style);

        assertThat(crossWidget.hasImage(style.getCrossImage()), is(true));
    }

    @Test
    public void givenNoughtWidget_ImageIsUsedFromStyle() throws Exception {
        TicTacToeGridCellsWidget.Nought noughtWidget = new TicTacToeGridCellsWidget.Nought(middleCell, model, style);

        assertThat(noughtWidget.hasImage(style.getNoughtImage()), is(true));
    }

    @Test
    public void givenNoneWidget_NoImageIsUsed() throws Exception {
        TicTacToeGridCellsWidget.None noneWidget = new TicTacToeGridCellsWidget.None(middleCell, model, style);

        assertThat(noneWidget.getDecorated(), is(instanceOf(EmptyWidget.class)));
    }

    @Test
    public void givenCellThatIsNotWithHighlighted3InARow_StateIsTransparent() throws Exception {
        StatefulTransparencyImageWidget.TransparencyState threeInARowHighlightState =
                threeInARowHighlightState(gridModel, Grid.Location.of(Grid.Row.First, Grid.Column.Third));
        gridModel.highlightThreeInARow(diagonal());

        assertThat(threeInARowHighlightState.isTransparent(), is(true));
    }

    private Grid.ThreeInARow diagonal() {
        return Grid.ThreeInARow.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
        );
    }

    private StatefulTransparencyImageWidget.TransparencyState threeInARowHighlightState(TicTacToeGridModel model, Grid.Location location) {
        return new TicTacToeGridCellsWidget.ThreeInARowHighlightTransparencyState(model, location, alphaValue);
    }

    @Test
    public void givenCellThatIsWithinAHighlighted3InARow_StateIsNotTransparent() throws Exception {
        StatefulTransparencyImageWidget.TransparencyState threeInARowHighlightState =
                threeInARowHighlightState(gridModel, Grid.Location.of(Grid.Row.First, Grid.Column.First));
        gridModel.highlightThreeInARow(diagonal());

        assertThat(threeInARowHighlightState.isTransparent(), is(false));
    }

    @Test
    public void givenAlphaValue_TransparencyStateStoresIt() throws Exception {
        StatefulTransparencyImageWidget.TransparencyState threeInARowHighlightState =
                threeInARowHighlightState(gridModel, Grid.Location.of(Grid.Row.First, Grid.Column.First));

        assertThat(threeInARowHighlightState.transparency(), is(alphaValue));
    }

    @Test
    public void givenACompleteGridCellWidgetAndAModelWithHighlighted3InARow_WidgetHasCorrectTransparency() throws Exception {
        gridModel.setGrid(LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross
        ));
        gridModel.highlightThreeInARow(diagonal());

        TicTacToeGridCellsWidget widget = new TicTacToeGridCellsWidget(gridModel, style);

        Iterable<Boolean> cellTransparencies = from(widget.getCells()).select(transparency());

        Iterable<Boolean> expectedTransparency = asList(
                false, true, true,
                true, false, true,
                true, true, false
        );
        assertThat(cellTransparencies, is(equalTo(expectedTransparency)));
    }

    private UnaryFunction<? super TicTacToeGridCellsWidget.CellWidget<?>, Boolean> transparency() {
        return new UnaryFunction<TicTacToeGridCellsWidget.CellWidget<?>, Boolean>() {
            @Override
            public Boolean execute(TicTacToeGridCellsWidget.CellWidget<?> widget) {
                return widget.isTransparent();
            }
        };
    }

    @Test
    public void givenCompleteGridCellWidget_subWidgetsIsSameAsGetCells() throws Exception {
        TicTacToeGridCellsWidget widget = new TicTacToeGridCellsWidget(gridModel, style);

        assertThat(widget.subWidgets(), is(equalTo(widget.getCells())));
    }
}
