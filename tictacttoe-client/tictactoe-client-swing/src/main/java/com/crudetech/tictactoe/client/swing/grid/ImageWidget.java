package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

class ImageWidget implements Widget {

    private int x, y;
    private final BufferedImage image;

    ImageWidget(Point location, BufferedImage image) {

        this.x = location.x;
        this.y = location.y;
        this.image = image;
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.drawImage(image, null, x, y);
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageWidget that = (ImageWidget) o;

        return x == that.x
            && y == that.y
            && Objects.equals(image, that.image);

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, image);
    }

    @Override
    public String toString() {
        return "ImageWidget{" +
                "x=" + x +
                ", y=" + y +
                ", image=" + image +
                '}';
    }
}
