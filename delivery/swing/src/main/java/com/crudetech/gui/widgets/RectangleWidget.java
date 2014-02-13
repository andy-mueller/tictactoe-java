package com.crudetech.gui.widgets;


public class RectangleWidget extends RectangularWidget {
    public RectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }

    @Override
    public void paintEcs(GraphicsStream pipe) {
        try(GraphicsStream.Context ctx = pipe.newContext()) {
            ctx.pushColor(getColor());
            pipe.drawRectangle(getBoundary());
        }
    }

}
