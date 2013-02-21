package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;

import java.awt.Dimension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StyleStub implements Style {
    static final int Width = 500;
    static final int Height = 1000;

    static final int GridCellWidth = 10;
    static final int GridCellDistance = 10;
    public static int GridCellHeight = 10   ;

    private final Image nought = mock(Image.class);
    private final Image cross = mock(Image.class);
    private final Image back;
    private final Rectangle[][] locations = new Rectangle[3][3];

    StyleStub() {
        fillLocations();
        back = mock(Image.class);
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
    public Image getBackgroundImage() {
        return back;
    }

    @Override
    public Color getBackgroundColor() {
        return new AwtColor(java.awt.Color.MAGENTA);
    }

    @Override
    public Color getHighlightColor() {
        return new AwtColor(java.awt.Color.PINK);
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
    public Image getCrossImage() {
        return cross;
    }

    @Override
    public Image getNoughtImage() {
        return nought;
    }
}
