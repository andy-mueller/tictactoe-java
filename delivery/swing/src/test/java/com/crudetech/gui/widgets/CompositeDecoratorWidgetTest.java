package com.crudetech.gui.widgets;


import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Features.class)
public class CompositeDecoratorWidgetTest {

    private AlphaValue alpha;
    private CompositeDecoratorWidget<Widget> decorator;
    private Widget decorated;

    @Rule
    public GraphicStreamMockery streamMockery = GraphicStreamMockery.withOnlyOneSubContext();

    @Before
    public void setUp() throws Exception {
        alpha = new AlphaValue(0.4f);
        decorated = mock(Widget.class);
        decorator = new CompositeDecoratorWidget<>(decorated, alpha);
    }

    @Test
    public void decoratorSetsTransparencyLevel() {
        decorator.paint(stream());

        streamMockery.verifyOnlyOneSubContextCreated();
        streamMockery.verifyOnOnlySubContext().pushAlpha(alpha);
    }

    @Test
    public void contextStreamIsPassedToDecoratedWidget() throws Exception {
        decorator.paint(stream());

        streamMockery.verifyOnlyOneSubContextCreated();

        verify(decorated).paint(streamMockery.lastContextMock());
    }

    private GraphicsStream stream() {
        return streamMockery.stream();
    }


    @Feature(Equivalent.class)
    public static Equivalent.Factory<CompositeDecoratorWidget<Widget>> decoratorIsEquivalent() {
        return new Equivalent.Factory<CompositeDecoratorWidget<Widget>>() {
            AlphaValue alpha = new AlphaValue(0.4f);
            Widget decorated = mock(Widget.class);

            @Override
            public CompositeDecoratorWidget<Widget> createItem() {
                return new CompositeDecoratorWidget<>(decorated, alpha);
            }

            @Override
            public List<CompositeDecoratorWidget<Widget>> createOtherItems() {
                return asList(
                        new CompositeDecoratorWidget<>(mock(Widget.class), alpha),
                        new CompositeDecoratorWidget<>(decorated, new AlphaValue(0.1f))
                );
            }
        };
    }

    @Feature(AdheresToGraphicsStreamProtocol.class)
    public static AdheresToGraphicsStreamProtocol.Factory adheresToStreamProtocol() {
        return new AdheresToGraphicsStreamProtocol.Factory() {
            @Override
            public Widget createWidget() {
                AlphaValue alpha = new AlphaValue(0.4f);
                Widget decorated = mock(Widget.class);
                return new CompositeDecoratorWidget<>(decorated, alpha);
            }
        };
    }
}
