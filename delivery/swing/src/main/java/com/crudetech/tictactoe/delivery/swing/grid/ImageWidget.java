package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.EcsWidget;
import com.crudetech.gui.widgets.GraphicsStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

class ImageWidget extends EcsWidget {
    private final BufferedImage image;

    ImageWidget(Point location, BufferedImage image) {
        super(location.x, location.y);
        this.image = image;
    }

    @Override
    public void paintEcs(GraphicsStream pipe) {
        pipe.drawImage(image);
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
                "location=" + getLocationAsString() +
                ", image=" + image +
                '}';
    }

}
