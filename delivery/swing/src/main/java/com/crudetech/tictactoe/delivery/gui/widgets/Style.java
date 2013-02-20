package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface Style {
    BufferedImage getBackgroundImage();

    Color getBackgroundColor();
    Color getHighlightColor();

    Dimension getPreferredSize();

    Rectangle[][] getGridMarkLocations();

    BufferedImage getCrossImage();

    BufferedImage getNoughtImage();

}
