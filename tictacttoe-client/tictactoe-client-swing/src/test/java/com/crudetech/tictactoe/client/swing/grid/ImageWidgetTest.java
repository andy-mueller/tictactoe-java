package com.crudetech.tictactoe.client.swing.grid;

import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ImageWidgetTest {
    @Test
    public void widgetPaintsImageOnSpecifiedLocation() throws Exception {
        BufferedImage image = mock(BufferedImage.class);
        ImageWidget w = new ImageWidget(new Point(1, 1), image);
        
        Graphics2D g2d = mock(Graphics2D.class);
        w.paint(g2d);

        verify(g2d).drawImage(image, null, 1, 1);
    }
    @Test
    public void setLocationMovesImage() throws Exception {
        BufferedImage image = mock(BufferedImage.class);
        ImageWidget w = new ImageWidget(new Point(1, 1), image);
        Graphics2D g2d = mock(Graphics2D.class);

        w.setLocation(42, 42);
        w.paint(g2d);

        verify(g2d).drawImage(image, null, 42, 42);
    }
}
