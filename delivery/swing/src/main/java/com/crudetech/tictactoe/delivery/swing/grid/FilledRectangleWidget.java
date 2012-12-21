package com.crudetech.tictactoe.delivery.swing.grid;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class FilledRectangleWidget extends RectangularWidget {

    public FilledRectangleWidget(Rectangle boundary, Paint color) {
        super(boundary, color);
    }


    @Override
    public void paintEcs(Graphics2D g2d) {
        Point loc = getLocation();
        AffineTransform xform = g2d.getTransform();
        g2d.setTransform(AffineTransform.getTranslateInstance(loc.getX(), loc.getY()));

        g2d.setPaint(getColor());
        g2d.fill(getBoundary());

        g2d.setTransform(xform);
    }
}
