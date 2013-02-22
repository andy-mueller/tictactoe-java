package com.crudetech.tictactoe.delivery.gui.widgets;

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
public class DimensionTest {
    @Test
    public void ctorSetsDimension() throws Exception {
        Dimension d = new Dimension(4, 5);

        assertThat(d.width, is(4));
        assertThat(d.height, is(5));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<Dimension> equivalent(){
        return new Equivalent.Factory<Dimension>() {
            @Override
            public Dimension createItem() {
                return new Dimension(4, 5);
            }

            @Override
            public List<Dimension> createOtherItems() {
                return asList(
                        new Dimension(4, 42),
                        new Dimension(42, 5),
                        new Dimension(42, 54)
                );
            }
        };
    }
}
