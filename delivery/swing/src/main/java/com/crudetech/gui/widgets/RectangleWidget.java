package com.crudetech.gui.widgets;


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
