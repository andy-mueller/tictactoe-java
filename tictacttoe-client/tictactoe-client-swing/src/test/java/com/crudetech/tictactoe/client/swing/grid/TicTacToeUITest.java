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
    private Style styleStub;

    @Before
    public void setUp() throws Exception {
        g2d = mock(Graphics2D.class);
        grid = new JTicTacToeGrid();
        background = mock(BufferedImage.class);
        styleStub = mock(Style.class);
        when(styleStub.getBackgroundImage()).thenReturn(background);
        when(styleStub.getBackgroundColor()).thenReturn(Color.ORANGE);

        grid.getUI().setStyle(styleStub);
    }

    @Test
    public void backGroundIsPainted() {
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

}
