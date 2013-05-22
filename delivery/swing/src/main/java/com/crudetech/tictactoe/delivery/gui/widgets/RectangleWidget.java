package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Rectangle;


public class RectangleWidget extends RectangularWidget {
    public RectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }

    @Override
    public void paintEcs(GraphicsStream pipe) {
        pipe.pushColor(getColor());
        try {
            pipe.drawRectangle(getBoundary());
        } finally {
            pipe.popColor();
        }
    }

}
