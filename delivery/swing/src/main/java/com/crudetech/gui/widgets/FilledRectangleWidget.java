package com.crudetech.gui.widgets;


public class FilledRectangleWidget extends RectangularWidget {

    public FilledRectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }


    @Override
    public void paintEcs(GraphicsStream stream) {
        try (GraphicsStream.Context pipe = stream.newContext()) {
            pipe.pushColor(getColor());
            pipe.fillRectangle(getBoundary());
        }
    }
}
