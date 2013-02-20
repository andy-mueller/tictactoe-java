package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Rectangle;


public class FilledRectangleWidget extends RectangularWidget {

    public FilledRectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }


    @Override
    public void paintEcs(GraphicsStream pipe) {
        pipe.pushColor(getColor());
        pipe.fillRectangle(getBoundary());
    }
}
