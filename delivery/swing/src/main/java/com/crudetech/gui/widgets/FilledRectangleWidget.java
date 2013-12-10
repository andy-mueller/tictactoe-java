package com.crudetech.gui.widgets;


public class FilledRectangleWidget extends RectangularWidget {

    public FilledRectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }


    @Override
    public void paintEcs(GraphicsStream pipe) {
        pipe.pushColor(getColor());
        try {
            pipe.fillRectangle(getBoundary());
        } finally {
            pipe.popColor();
        }
    }
}
