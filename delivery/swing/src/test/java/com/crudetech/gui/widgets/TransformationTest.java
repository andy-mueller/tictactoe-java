package com.crudetech.gui.widgets;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TransformationTest {

    private final Point inTransform = Point.of(1, 1);
    private final Point inWorld = Point.of(4, 3);

    @Test
    public void givenTranslationAndScale_PointIsTransformedToWorld() throws Exception {
        Transformation xform = new Transformation(2, 1, 2);

        Point actual = xform.transform(inTransform);

        assertThat(actual, is(inWorld));
    }
    @Test
    public void givenTranslationAndScale_PointIsTransformedToCoordinateSystemUsingTheInverse() throws Exception {
        Transformation inverse = new Transformation(2, 1, 2).inverse();

        Point actual = inverse.transform(inWorld);

        assertThat(actual, is(inTransform));
    }
}
