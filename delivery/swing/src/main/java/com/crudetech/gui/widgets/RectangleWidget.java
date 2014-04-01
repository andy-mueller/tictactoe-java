package com.crudetech.gui.widgets;


public class RectangleWidget extends RectangularWidget {
    public RectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }

    @Override
    public void paintEcs(GraphicsStream stream) {
        try (GraphicsStream.Context pipe = stream.newContext()) {
            pipe.pushColor(getColor());
            pipe.drawRectangle(getBoundary());
        }
    }

}
