package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.GraphicsStream;

import java.awt.Rectangle;

public class RectangleWidget extends RectangularWidget{
    public RectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }

    @Override
    public void paintEcs(GraphicsStream pipe) {
        pipe.pushColor(getColor());
        pipe.drawRectangle(getBoundary());
    }
}
