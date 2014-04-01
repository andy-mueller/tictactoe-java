package com.crudetech.gui.widgets;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(Features.class)
public class EmptyWidgetTest {
    @Test
    public void paintDoesNothing() {
        EmptyWidget empty = new EmptyWidget();
        GraphicsStream g2d = mock(GraphicsStream.class);

        empty.paint(g2d);

        verifyZeroInteractions(g2d);
    }

    @Test
    public void equals() {
        EmptyWidget empty = new EmptyWidget();
        assertThat(empty, is(new EmptyWidget()));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<EmptyWidget> equivalent() {
        return new Equivalent.Factory<EmptyWidget>() {
            @Override
            public EmptyWidget createItem() {
                return new EmptyWidget();
            }

            @Override
            public List<EmptyWidget> createOtherItems() {
                return emptyList();
            }
        };
    }
}
