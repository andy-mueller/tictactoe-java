package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Rectangle;

import java.awt.Dimension;
import java.awt.Point;


class WidgetAwtConverter {
     static Point point(com.crudetech.gui.widgets.Point p) {
        return new Point(p.x, p.y);
    }

     static Rectangle rectangle(java.awt.Rectangle r) {
        return new Rectangle(r.x, r.y, r.width, r.height);
    }

     static java.awt.Rectangle rectangle(Rectangle r) {
        return new java.awt.Rectangle(r.x, r.y, r.width, r.height);
    }
    static Dimension dimension(com.crudetech.tictactoe.delivery.gui.widgets.Dimension d) {
        return new Dimension(d.width, d.height);
    }
}
