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
public class PointTest {
    @Test
    public void ctorSetsLocation() throws Exception {
        Point p = new Point(3, 4);

        assertThat(p.x, is(3));
        assertThat(p.y, is(4));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<Point> equivalent() {
        return new Equivalent.Factory<Point>() {
            @Override
            public Point createItem() {
                return new Point(2, 3);
            }

            @Override
            public List<Point> createOtherItems() {
                return asList(new Point(42, 3), new Point(42, 3), new Point(42, 42));
            }
        };
    }
}
