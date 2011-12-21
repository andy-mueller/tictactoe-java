package com.crudetech.tictactoe.client.swing.grid;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class TicTacToeUITest {

    private Graphics2D g2d;
    private JTicTacToeGrid grid;
    private BufferedImage background;
    private Style style;

    @Before
    public void setUp() throws Exception {
        g2d = mock(Graphics2D.class);
        grid = new JTicTacToeGrid();
        grid.setSize(1000, 2000);

        background = mock(BufferedImage.class);
        when(background.getWidth()).thenReturn(500);
        when(background.getHeight()).thenReturn(1000);

        style = mock(Style.class);
        when(style.getBackgroundImage()).thenReturn(background);
        when(style.getBackgroundColor()).thenReturn(Color.ORANGE);
        when(style.getPreferredSize()).thenReturn(new Dimension(100, 200));

        grid.getUI().setStyle(style);
    }

    @Test
    public void backGroundIsPaintedInMiddle() {
        grid.getUI().paint(g2d);

        verify(g2d).drawImage(background, null, 250, 500);
    }

    @Test
    public void backgroundIsPositionedAtOriginIfComponentIsSmaller() {
        grid.setSize(10, 10);
        grid.getUI().paint(g2d);

        verify(g2d).drawImage(background, null, 0, 0);
    }

    @Test
    public void backGroundIsInvalidated() {
        grid.getUI().paint(g2d);

        verify(g2d).setPaint(Color.ORANGE);
        verify(g2d).fillRect(0, 0, grid.getWidth(), grid.getHeight());
    }

    @Test
    public void defaultStyleIsBrush() {
        grid = new JTicTacToeGrid();
        assertThat(Styles.Brush, is(grid.getUI().getStyle()));
    }

    @Test
    public void preferredSizeIsStyleSize() throws Exception {
        Dimension expected = style.getPreferredSize();
        
        assertThat(grid.getUI().getPreferredSize(grid), is(expected));
    }
    //  paint order: background invalidate, background image, marks
}
