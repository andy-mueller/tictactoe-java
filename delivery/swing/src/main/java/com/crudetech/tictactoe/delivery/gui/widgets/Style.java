package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.game.Grid;


public interface Style {
    Image getBackgroundImage();

    Color getBackgroundColor();

    Color getHighlightColor();

    Dimension getPreferredSize();

    Rectangle getGridMarkLocation(Grid.Location location);

    Image getCrossImage();

    Image getNoughtImage();

}
