package com.crudetech.gui.widgets;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;

@RunWith(Features.class)
public class ImageWidgetTest {
    @Rule
    public GraphicStreamMockery streamMockery = GraphicStreamMockery.withOnlyOneSubContext();

    @Test
    public void widgetPaintsImageInEcs() throws Exception {
        Image image = mock(Image.class);
        Widget w = new ImageWidget(1, 1, image);

        w.paint(streamMockery.stream());

        streamMockery.verifyOnLastSubContext().drawImage(image);
    }

    @Feature(AdheresToGraphicsStreamProtocol.class)
    public AdheresToGraphicsStreamProtocol.Factory adheresToStreamProtocol() {
        return new AdheresToGraphicsStreamProtocol.Factory() {
            @Override
            public Widget createWidget() {
                return new ImageWidget(3, 2, mock(Image.class));
            }
        };
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<ImageWidget> imageWidgetEqualityFeature() {
        final Image image = mock(Image.class);
        return new Equivalent.Factory<ImageWidget>() {
            @Override
            public ImageWidget createItem() {
                return new ImageWidget(42, 42, image);
            }

            @Override
            public List<ImageWidget> createOtherItems() {
                return asList(
                        new ImageWidget(0, 0, image),
                        new ImageWidget(42, 42, mock(Image.class))
                );
            }
        };
    }
}
