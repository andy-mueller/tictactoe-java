package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StyleStub implements Style {
    private final BufferedImage nought = mock(BufferedImage.class);
    private final BufferedImage cross = mock(BufferedImage.class);
    private final BufferedImage back;
    private final Rectangle[][] locations = {
            {new Rectangle(0, 0, 10, 10), new Rectangle(0, 1, 10, 11), new Rectangle(0, 2, 10, 12)},
            {new Rectangle(1, 0, 11, 10), new Rectangle(1, 1, 11, 11), new Rectangle(1, 2, 11, 12)},
            {new Rectangle(2, 0, 12, 10), new Rectangle(2, 1, 12, 11), new Rectangle(2, 2, 12, 12)},
    };

    StyleStub() {
        back = mock(BufferedImage.class);
        when(back.getWidth()).thenReturn(500);
        when(back.getHeight()).thenReturn(1000);
    }

    @Override
    public BufferedImage getBackgroundImage() {
        return back;
    }

    @Override
    public Color getBackgroundColor() {
        return Color.MAGENTA;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(42, 42);
    }

    @Override
    public Rectangle[][] getGridMarkLocations() {
        return locations;
    }

    @Override
    public BufferedImage getCrossImage() {
        return cross;
    }

    @Override
    public BufferedImage getNoughtImage() {
        return nought;
    }
}
