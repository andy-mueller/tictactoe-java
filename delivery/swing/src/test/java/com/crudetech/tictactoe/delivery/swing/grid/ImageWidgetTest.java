package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Features.class)
public class ImageWidgetTest {
    @Test
    public void widgetPaintsImageInEcs() throws Exception {
        BufferedImage image = mock(BufferedImage.class);
        Widget w = new ImageWidget(1, 1, image);

        GraphicsStream g2d = mock(GraphicsStream.class);
        w.paint(g2d);

        verify(g2d).drawImage(image);
    }


    @Feature(Equivalent.class)
    public static Equivalent.Factory<ImageWidget> imageWidgetEqualityFeature() {
        final BufferedImage image = mock(BufferedImage.class);
        return new Equivalent.Factory<ImageWidget>() {
            @Override
            public ImageWidget createItem() {
                return new ImageWidget(42, 42, image);
            }

            @Override
            public List<ImageWidget> createOtherItems() {
                return asList(
                        new ImageWidget(0,0, image),
                        new ImageWidget(42, 42, mock(BufferedImage.class))
                );
            }
        };
    }
}
