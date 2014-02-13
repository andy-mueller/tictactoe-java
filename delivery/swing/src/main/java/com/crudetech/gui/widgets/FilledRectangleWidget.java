package com.crudetech.gui.widgets;


public class FilledRectangleWidget extends RectangularWidget {

    public FilledRectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }


    @Override
    public void paintEcs(GraphicsStream pipe) {
        try (GraphicsStream.Context ctx = pipe.newContext()) {
            ctx.pushColor(getColor());
            pipe.fillRectangle(getBoundary());
        }
    }
}
