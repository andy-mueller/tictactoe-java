package com.crudetech.gui.widgets;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.delivery.swing.grid.AwtColor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;

@RunWith(Features.class)
public class FilledRectangleWidgetTest {

    @Rule
    public GraphicStreamMockery streamMockery = GraphicStreamMockery.withOnlyOneSubContext();

    @Test
    public void paintingIsInEcs() {
        Widget w = aFilledRectangle();
        moveAtAnother(w, Point.of(42, 42));

        w.paint(streamMockery.stream());

        streamMockery.verifyOnLastSubContext().fillRectangle(someBoundary());
    }

    private void moveAtAnother(Widget w, Point location) {
        WidgetJig jig = new WidgetJig(w);
        jig.setLocation(location);
    }

    @Test
    public void colorIsSetForPainting() {
        FilledRectangleWidget w = aFilledRectangle();

        w.paintEcs(streamMockery.stream());

        streamMockery.verifyOnlyOneSubContextCreated();
        streamMockery.verifyOnOnlySubContext().pushColor(AwtColor.ORANGE);
    }

    @Feature(AdheresToGraphicsStreamProtocol.class)
    public static AdheresToGraphicsStreamProtocol.Factory adheresToStreamProtocol() {
        return new AdheresToGraphicsStreamProtocol.Factory() {
            @Override
            public Widget createWidget() {
                return aFilledRectangle();
            }
        };
    }

    private static FilledRectangleWidget aFilledRectangle() {
        return new FilledRectangleWidget(someBoundary(), AwtColor.ORANGE);
    }

    private static Rectangle someBoundary() {
        return new Rectangle(0, 0, 84, 966);
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<FilledRectangleWidget> equivalentFixture() {
        return new Equivalent.Factory<FilledRectangleWidget>() {
            @Override
            public FilledRectangleWidget createItem() {
                return aFilledRectangle();
            }

            @Override
            public List<FilledRectangleWidget> createOtherItems() {
                return asList(
                        new FilledRectangleWidget(new Rectangle(0, 0, 42, 966), AwtColor.ORANGE),
                        new FilledRectangleWidget(new Rectangle(0, 0, 84, 42), AwtColor.ORANGE),
                        new FilledRectangleWidget(someBoundary(), AwtColor.CYAN)
                );
            }
        };
    }
}
