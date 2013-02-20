package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.Color;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Features.class)
public class AwtColorTest {
    @Test
    public void ctorWrapsColor() throws Exception {
        assertThat(new AwtColor(Color.BLACK).color, is(Color.BLACK));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<AwtColor> equivalent() {
        return new Equivalent.Factory<AwtColor>() {
            @Override
            public AwtColor createItem() {
                return new AwtColor(Color.BLACK);
            }

            @Override
            public List<AwtColor> createOtherItems() {
                return asList(new AwtColor(Color.ORANGE));
            }
        };
    }
}
