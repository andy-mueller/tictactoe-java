package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.GraphicsStream;

import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class FilledRectangleWidget extends RectangularWidget {

    public FilledRectangleWidget(Rectangle boundary, Paint color) {
        super(boundary, color);
    }


    @Override
    public void paintEcs(GraphicsStream pipe) {
        pipe.pushColor(getColor());
        pipe.fillRectangle(getBoundary());
    }
}
