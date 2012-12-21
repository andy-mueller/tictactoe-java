package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.delivery.swing.grid.ImageWidget;
import com.crudetech.tictactoe.delivery.swing.grid.Widget;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.Graphics2D;
import java.awt.Point;
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
        Widget w = new ImageWidget(new Point(1, 1), image);

        Graphics2D g2d = mock(Graphics2D.class);
        w.paintEcs(g2d);

        verify(g2d).drawImage(image, null, 0, 0);
    }


    @Feature(Equivalent.class)
    public static Equivalent.Factory<ImageWidget> imageWidgetEqualityFeature() {
        final BufferedImage image = mock(BufferedImage.class);
        final Point loc = new Point(42, 42);
        return new Equivalent.Factory<ImageWidget>() {
            @Override
            public ImageWidget createItem() {
                return new ImageWidget(loc, image);
            }

            @Override
            public List<ImageWidget> createOtherItems() {
                return asList(
                        new ImageWidget(new Point(), image),
                        new ImageWidget(loc, mock(BufferedImage.class))
                );
            }
        };
    }
}
