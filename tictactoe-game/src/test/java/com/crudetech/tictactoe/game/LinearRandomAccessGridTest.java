package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;
import com.crudetech.junit.collections.Iterable;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridMatcher.isEmpty;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

        UnaryFunction<Grid.Cell, Boolean> notChanged2 = new UnaryFunction<Grid.Cell, Boolean>() {
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

        boolean othersAreNone = from(grid).where(notChanged2).select(gridMark).where(notEquals(Grid.Mark.None)).any();
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
    public static Equivalent.Factory<Grid.Location> locationsAreEquivalent() {
        return new Equivalent.Factory<Grid.Location>() {
            @Override
            public Grid.Location createItem() {
                return new Grid.Location(Grid.Row.First, Grid.Column.Second);
            }

            @Override
            public List<Grid.Location> createOtherItems() {
                return asList(
                        new Grid.Location(Grid.Row.First, Grid.Column.First),
                        new Grid.Location(Grid.Row.Second, Grid.Column.Second),
                        new Grid.Location(Grid.Row.Third, Grid.Column.Third)
                );
            }
        };
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<Grid> isEquivalent() {
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

                return from(template).select(createModifiedGrid).toList();
            }
        };
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<LinearRandomAccessGrid.Triple> winingTripleIsEquivalent() {
        return new Equivalent.Factory<LinearRandomAccessGrid.Triple>() {
            @Override
            public LinearRandomAccessGrid.Triple createItem() {
                return new LinearRandomAccessGrid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First));
            }

            @Override
            public List<LinearRandomAccessGrid.Triple> createOtherItems() {
                return asList(
                        new LinearRandomAccessGrid.Triple(Grid.Mark.Cross, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new LinearRandomAccessGrid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new LinearRandomAccessGrid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new LinearRandomAccessGrid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.Second, Grid.Column.First)),
                        Grid.Triple.Empty
                );
            }
        };
    }

    @Feature(Iterable.class)
    public static Iterable.Factory<Grid.Cell> implementsIterable() {
        return new Iterable.Factory<Grid.Cell>() {
            @Override
            public java.lang.Iterable<Grid.Cell> createIterable() {
                return new LinearRandomAccessGrid();
            }
        };
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<Grid.Cell> cellsAreEquivalent() {
        return new Equivalent.Factory<Grid.Cell>() {
            @Override
            public Grid.Cell createItem() {
                return new Grid.Cell(Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Mark.Cross);
            }

            @Override
            public List<Grid.Cell> createOtherItems() {
                return asList(
                        new Grid.Cell(Grid.Location.of(Grid.Row.Second, Grid.Column.Second), Grid.Mark.Cross),
                        new Grid.Cell(Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Mark.Nought)
                );
            }
        };
    }
}
