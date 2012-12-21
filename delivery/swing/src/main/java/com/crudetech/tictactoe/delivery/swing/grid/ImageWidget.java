package com.crudetech.tictactoe.delivery.swing.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Objects;

class ImageWidget extends EcsWidget {
    private final BufferedImage image;

    ImageWidget(Point location, BufferedImage image) {
        super(location);
        this.image = image;
    }

    @Override
    public void paintEcs(Graphics2D g2d) {
        g2d.drawImage(image, null, 0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ImageWidget that = (ImageWidget) o;

        return Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "ImageWidget{" +
                "location=" + getLocation() +
                ", image=" + image +
                '}';
    }
}
