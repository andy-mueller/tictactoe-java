package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Rectangle;

import java.awt.Dimension;
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
