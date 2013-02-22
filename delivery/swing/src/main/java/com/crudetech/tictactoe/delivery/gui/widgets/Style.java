package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;


public interface Style {
    Image getBackgroundImage();

    Color getBackgroundColor();
    Color getHighlightColor();

    Dimension getPreferredSize();

    Rectangle[][] getGridMarkLocations();

    Image getCrossImage();

    Image getNoughtImage();

}
