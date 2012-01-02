package com.crudetech.tictactoe.client.swing.grid;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TicTacToeGridUITest {

    private Graphics2D g2d;
    private JTicTacToeGrid grid;
    private TicTacToeGridUI ui;
    private Style style;

    @Before
    public void setUp() throws Exception {
        g2d = mock(Graphics2D.class);

        grid = new JTicTacToeGrid();
        grid.setSize(1000, 2000);

        style = new StyleStub();


        grid.getUI().setStyle(style);
        ui = grid.getUI();
    }

    @Test
    public void backGroundIsPaintedInMiddle() {
        grid.getUI().paint(g2d);

        verify(g2d).drawImage(style.getBackgroundImage(), null, 250, 500);
    }

    @Test
    public void backgroundIsPositionedAtOriginIfComponentIsSmaller() {
        grid.setSize(10, 10);
        ui.paint(g2d);

        verify(g2d).drawImage(style.getBackgroundImage(), null, 0, 0);
    }

    @Test
    public void backGroundIsInvalidated() {
        ui.paint(g2d);

        List<Widget> widgets = ui.buildPaintList();
        Widget background = widgets.get(0);

        Widget expectedBackground = new FilledRectangleWidget(
                new Rectangle(0, 0, grid.getWidth(), grid.getHeight()), style.getBackgroundColor());
        assertThat(background, is(expectedBackground));
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

    // model is painted
    // paint stack is painted in order
    //  paint order: background invalidate, background image, marks
    // model change
    // setStyle triggers repaint
}
