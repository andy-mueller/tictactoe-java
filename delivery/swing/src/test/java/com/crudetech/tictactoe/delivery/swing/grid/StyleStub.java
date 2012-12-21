package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.tictactoe.delivery.swing.grid.Style;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StyleStub implements Style {
    static final int Width = 500;
    static final int Height = 1000;

    static final int GridCellWidth = 10;
    static final int GridCellDistance = 10;
    public static int GridCellHeight = 10   ;

    private final BufferedImage nought = mock(BufferedImage.class);
    private final BufferedImage cross = mock(BufferedImage.class);
    private final BufferedImage back;
    private final Rectangle[][] locations = new Rectangle[3][3];

    StyleStub() {
        fillLocations();
        back = mock(BufferedImage.class);
        when(back.getWidth()).thenReturn(Width);
        when(back.getHeight()).thenReturn(Height);
    }

    private void fillLocations() {
        for(int row = 0; row < 3; ++row){
            for(int col = 0; col < 3; ++col){
                locations[row][col] = new Rectangle(row*(GridCellDistance+GridCellHeight), col*(GridCellDistance+GridCellWidth), GridCellWidth, GridCellHeight);
            }
        }
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
    public Color getHighlightColor() {
        return Color.PINK;
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
