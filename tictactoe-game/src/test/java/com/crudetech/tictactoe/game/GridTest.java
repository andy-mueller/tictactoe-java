package com.crudetech.tictactoe.game;

import com.crudetech.junit.collections.Iterable;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Features.class)
public class GridTest {
    @Test
    public void rowValuesHaveCorrectOrdinals() {
        assertThat(Grid.Row.First.ordinal(), is(0));
        assertThat(Grid.Row.Second.ordinal(), is(1));
        assertThat(Grid.Row.Third.ordinal(), is(2));
    }

    @Test
    public void columnValuesHaveCorrectOrdinals() {
        assertThat(Grid.Column.First.ordinal(), is(0));
        assertThat(Grid.Column.Second.ordinal(), is(1));
        assertThat(Grid.Column.Third.ordinal(), is(2));
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
    public static Equivalent.Factory<Grid.Triple> winingTripleIsEquivalent() {
        return new Equivalent.Factory<Grid.Triple>() {
            @Override
            public Grid.Triple createItem() {
                return new Grid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First));
            }

            @Override
            public List<Grid.Triple> createOtherItems() {
                return asList(
                        new Grid.Triple(Grid.Mark.Cross, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new Grid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new Grid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new Grid.Triple(Grid.Mark.Nought, Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.Second, Grid.Column.First)),
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
}
