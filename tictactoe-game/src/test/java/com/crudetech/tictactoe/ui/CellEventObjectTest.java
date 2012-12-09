package com.crudetech.tictactoe.ui;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.game.Grid;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;

@RunWith(Features.class)
public class CellEventObjectTest {

    @Feature(Equivalent.class)
    public static Equivalent.Factory<CellEventObject<Object>> cellClickedEventObjectEquivalent() {
        return new Equivalent.Factory<CellEventObject<Object>>() {
            Grid.Location cellLocation = Grid.Location.of(Grid.Row.First, Grid.Column.First);

            @Override
            public CellEventObject<Object> createItem() {
                return new CellEventObject<Object>(object, cellLocation);
            }

            Object object = new Object();

            @Override
            public List<CellEventObject<Object>> createOtherItems() {
                return asList(
                        new CellEventObject<Object>(object, Grid.Location.of(Grid.Row.First, Grid.Column.Second)),
                        new CellEventObject<Object>(new Object(), cellLocation)
                );
            }
        };
    }

}
