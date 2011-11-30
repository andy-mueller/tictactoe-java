package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(Features.class)
public class LinearRandomAccessGridTest {
    @Test
    public void ctorDoesSetupEmptyGrid() {
        final LinearRandomAccessGrid grid = new LinearRandomAccessGrid();

        assertThat(grid, isEmpty());
    }

    @Test
    public void setAtStoresItemAtThisPosition() {
        LinearRandomAccessGrid grid = new LinearRandomAccessGrid();

        final Grid.Location location = Grid.Location.of(Grid.Row.First, Grid.Column.Second);
        grid.setAt(location, Grid.Mark.Nought);

        UnaryFunction<Grid.Cell, Boolean> notChanged = new UnaryFunction<Grid.Cell, Boolean>() {
            @Override
            public Boolean execute(Grid.Cell cell) {
                return !cell.getLocation().equals(location);
            }
        };
        UnaryFunction<Grid.Cell, Grid.Mark> gridMark = new UnaryFunction<Grid.Cell, Grid.Mark>() {
            @Override
            public Grid.Mark execute(Grid.Cell cell) {
                return cell.getMark();
            }
        };

        boolean othersAreNone = from(grid.getCells()).where(notChanged).select(gridMark).where(notEquals(Grid.Mark.None)).any();
        assertThat(othersAreNone, is(false));

    }

    private static <T> UnaryFunction<T, Boolean> notEquals(final T rhs) {
        return new UnaryFunction<T, Boolean>() {
            @Override
            public Boolean execute(T lhs) {
                return !Objects.equals(lhs, rhs);
            }
        };
    }


    @Feature(Equivalent.class)
    public static Equivalent.Factory<Grid> equivalentFeature() {
        return new Equivalent.Factory<Grid>() {
            @Override
            public Grid createItem() {
                return LinearRandomAccessGrid.of(new Grid.Mark[]{
                        Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                        Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                        Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                });
            }

            @Override
            public List<Grid> createOtherItems() {

                LinearRandomAccessGrid template = new LinearRandomAccessGrid();

                UnaryFunction<Grid.Cell, Grid> createModifiedGrid = new UnaryFunction<Grid.Cell, Grid>() {
                    @Override
                    public Grid execute(Grid.Cell cell) {
                        LinearRandomAccessGrid grid = new LinearRandomAccessGrid();
                        grid.setAt(cell.getLocation(), Grid.Mark.Cross);
                        return grid;
                    }
                };

                return from(template.getCells()).select(createModifiedGrid).toList();
            }
        };
    }

    @Test
    public void copyCtorCreatesCopy() {
        Grid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross,
        });

        LinearRandomAccessGrid copy = new LinearRandomAccessGrid(grid);

        assertThat(copy, is(grid));
    }

    @Test
    public void copyCtorCreatesIndependentCopy() {
        Grid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross,
        });

        LinearRandomAccessGrid copy = new LinearRandomAccessGrid(grid);
        copy.setAt(Grid.Location.of(Grid.Row.Second, Grid.Column.Third), Grid.Mark.Cross);

        assertThat(copy, is(not(grid)));
    }
}
