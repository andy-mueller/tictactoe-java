package com.crudetech.tictactoe.delivery.gui.widgets;

import java.awt.*;
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
