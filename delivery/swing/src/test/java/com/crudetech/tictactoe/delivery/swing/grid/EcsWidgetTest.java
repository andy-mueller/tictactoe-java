package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.EcsWidget;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
    public void setLocationChangesLocation(){
        ecsWidget.setLocation(500, 2);

        assertThat(ecsWidget, hasLocation(500, 2));
    }

    private Matcher<EcsWidget> hasLocation(final int x, final int y) {
        return new CustomTypeSafeMatcher<EcsWidget>("") {
            @Override
            protected boolean matchesSafely(EcsWidget ecsWidget) {
                return ecsWidget.getLocationX() == x && ecsWidget.getLocationY() == y;
            }

        };
    }

    @Test
    public void ctorSetsLocation(){
        assertThat(ecsWidget, hasLocation(42, 42));
    }
    @Feature(Equivalent.class)
    public static Equivalent.Factory<EcsWidget> isEquivalentByLocation(){
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
