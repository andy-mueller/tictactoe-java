package com.crudetech.gui.widgets;


class BoundedTextWidget extends EcsWidget {
    private final Rectangle boundary;
    private final String text;
    private final Font font;

    public BoundedTextWidget(Font font, Rectangle boundary, String text) {
        super(boundary.x, boundary.y);
        this.font = font;
        this.boundary = boundary.setLocation(0, 0);
        this.text = text;
    }

    @Override
    protected void paintEcs(GraphicsStream pipe) {
        final CoordinateSystem scale = computeScaleToFitInRectangle();
        try (GraphicsStream.Context ctx = pipe.newContext()) {
            ctx.pushCoordinateSystem(scale);
            ctx.pushFont(font);
            pipe.drawText(0, 0, text);
        }
    }


    private CoordinateSystem computeScaleToFitInRectangle() {
        final double sx = ((double) boundary.height) / font.getHeight();
        return new CoordinateSystem(Point.Origin, sx);
    }
}
