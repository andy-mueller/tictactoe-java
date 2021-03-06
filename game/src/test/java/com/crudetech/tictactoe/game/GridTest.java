package com.crudetech.tictactoe.game;

import com.crudetech.junit.collections.Iterable;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Features.class)
public class GridTest {

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
    public static Equivalent.Factory<Grid.ThreeInARow> winingTripleIsEquivalent() {
        return new Equivalent.Factory<Grid.ThreeInARow>() {
            @Override
            public Grid.ThreeInARow createItem() {
                return Grid.ThreeInARow.of(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First));
            }

            @Override
            public List<Grid.ThreeInARow> createOtherItems() {
                return asList(
                        Grid.ThreeInARow.of(Grid.Mark.Cross, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        Grid.ThreeInARow.of(Grid.Mark.Nought, Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        Grid.ThreeInARow.of(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        Grid.ThreeInARow.of(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.Second, Grid.Column.First)),
                        Grid.ThreeInARow.Empty
                );
            }
        };
    }

    @Feature(Iterable.class)
    public static Iterable.Factory<Grid.Cell> implementsIterable() {
        return new Iterable.Factory<Grid.Cell>() {
            @Override
            public java.lang.Iterable<Grid.Cell> createIterable() {
                return new LinearRandomAccessGrid().getCells();
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

    @Test
    public void markCanReturnItsOppositeMark() {
        assertThat(Grid.Mark.Cross.getOpposite(), is(Grid.Mark.Nought));
        assertThat(Grid.Mark.Nought.getOpposite(), is(Grid.Mark.Cross));
    }

    @Test
    public void markNoneThrowsOnOppositeMark() {
        Runnable askMarkNoneForOpposite = new Runnable() {
            @Override
            public void run() {
                Grid.Mark.None.getOpposite();
            }
        };

        assertThat(askMarkNoneForOpposite, doesThrow(IllegalStateException.class));
    }

    @Test
    public void rowPositionHasCorrectOrder() {
        assertThat(Grid.Row.First.position(), is(0));
        assertThat(Grid.Row.Second.position(), is(1));
        assertThat(Grid.Row.Third.position(), is(2));
    }

    @Test
    public void columnPositionHasCorrectOrder() {
        assertThat(Grid.Column.First.position(), is(0));
        assertThat(Grid.Column.Second.position(), is(1));
        assertThat(Grid.Column.Third.position(), is(2));
    }
}
