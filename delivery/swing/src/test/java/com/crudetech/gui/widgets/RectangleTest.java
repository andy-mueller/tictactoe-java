package com.crudetech.gui.widgets;

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
public class RectangleTest {
    @Test
    public void ctorSetsProperties() throws Exception {
        Rectangle r = new Rectangle(1, 2, 3, 4);

        assertThat(r.x, is(1));
        assertThat(r.y, is(2));
        assertThat(r.width, is(3));
        assertThat(r.height, is(4));
    }
    @Test
    public void givenComparisonWithBadType_equalsReturnsFalse() throws Exception {
        Rectangle r = new Rectangle(1, 2, 3, 4);

        assertThat(r.equals(this), is(false));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<Rectangle> equivalent() {
        return new Equivalent.Factory<Rectangle>() {
            @Override
            public Rectangle createItem() {
                return new Rectangle(1, 2, 3, 4);
            }

            @Override
            public List<Rectangle> createOtherItems() {
                return asList(
                        new Rectangle(1, 42, 3, 4),
                        new Rectangle(1, 2, 42, 4),
                        new Rectangle(1, 2, 3, 42),
                        new Rectangle(7, 8, 9, 4)
                );
            }
        };
    }
}
