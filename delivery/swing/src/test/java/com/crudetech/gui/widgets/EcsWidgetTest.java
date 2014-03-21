package com.crudetech.gui.widgets;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(Features.class)
public class EcsWidgetTest {

    private EcsWidget ecsWidget;

    private static class NoPaintEcsWidget extends EcsWidget {
        public NoPaintEcsWidget(int x, int y) {
            super(x, y);
        }

        @Override
        public void paintEcs(GraphicsStream pipe) {

        }
    }

    @Before
    public void setUp() throws Exception {
        ecsWidget = new NoPaintEcsWidget(42, 42);
    }


    @Test
    public void ctorSetsCoordinateSystem() {
        assertThat(ecsWidget.coordinateSystem(), is(new CoordinateSystem(Point.of(42, 42))));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<EcsWidget> isEquivalentByLocation() {
        return new Equivalent.Factory<EcsWidget>() {
            @Override
            public EcsWidget createItem() {
                return new NoPaintEcsWidget(42, 42);
            }

            @Override
            public List<EcsWidget> createOtherItems() {
                return Arrays.<EcsWidget>asList(new NoPaintEcsWidget(1, 1));
            }
        };
    }
}
