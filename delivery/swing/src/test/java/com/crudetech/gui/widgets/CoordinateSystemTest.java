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
public class CoordinateSystemTest {
    @Test
    public void defaultCtorSetsLocationAndScale() throws Exception {
        CoordinateSystem coos = new CoordinateSystem();

        assertThat(coos.getLocation(), is(Point.of(0, 0)));
        assertThat(coos.getScale(), is(1.0));
    }

    @Test
    public void ctorSetsLocationAndScale() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.of(1, 2), 3.0);

        assertThat(coos.getLocation(), is(Point.of(1, 2)));
        assertThat(coos.getScale(), is(3.0));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<CoordinateSystem> equivalent() {
        return new Equivalent.Factory<CoordinateSystem>() {
            @Override
            public CoordinateSystem createItem() {
                return new CoordinateSystem();
            }

            @Override
            public List<CoordinateSystem> createOtherItems() {
                return asList(
                        new CoordinateSystem(Point.of(0, 1), 1.0),
                        new CoordinateSystem(Point.of(0, 0), 2.0)
                );
            }
        };
    }

    @Test
    public void translationMovesLocation() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.of(1, 2));

        coos.translate(2, 1);

        assertThat(coos, is(new CoordinateSystem(Point.of(3, 3))));
    }

    @Test
    public void scaleMultipliesOnScale() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.Origin, 3.0);

        coos.scale(2.0);

        assertThat(coos, is(new CoordinateSystem(Point.Origin, 6.0)));
    }

    @Test
    public void givenNoScale_toWorldAddsLocationToPoint() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.of(2, 3));

        Point actual = coos.toWorldCoordinates(Point.of(3, 2));

        assertThat(actual, is(Point.of(5, 5)));
    }


    @Test
    public void givenScaleAndRectangleIsLocatedAtCoordinateOrigin_toWorldMovesAndScalesLocationToPoint() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.of(2, 1), 0.5);

        Rectangle r = new Rectangle(0, 0, 4, 2);
        Rectangle inWorldCoordinates = coos.toWorldCoordinates(r);

        assertThat(inWorldCoordinates, is(new Rectangle(2, 1, 2, 1)));
    }

    @Test
    public void givenScaleAndRectangleNotOnEcsOrigin_toWorldMovesAndScalesLocationToPoint() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.of(2, 1), 0.5);

        Rectangle r = new Rectangle(2, 4, 4, 2);
        Rectangle inWorldCoordinates = coos.toWorldCoordinates(r);

        assertThat(inWorldCoordinates, is(new Rectangle(3, 3, 2, 1)));
    }

    @Test
    public void givenRectangleNotOnEcsOrigin_worldToEcsAppliesTranslationAndScale() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.of(2, 1), 0.5);

        Rectangle r = new Rectangle(3, 3, 2, 1);
        Rectangle inWidgetCoordinates = coos.toWidgetCoordinates(r);

        assertThat(inWidgetCoordinates, is(new Rectangle(2, 4, 4, 2)));
    }

    @Test
    public void toWidgetRemovesLocationToPoint() throws Exception {
        CoordinateSystem coos = new CoordinateSystem(Point.of(2, 3));

        Point actual = coos.toWidgetCoordinates(Point.of(2, 3));

        assertThat(actual, is(Point.of(0, 0)));
    }
}
