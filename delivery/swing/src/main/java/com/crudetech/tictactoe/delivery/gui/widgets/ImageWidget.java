package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.EcsWidget;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Point;

import java.util.Objects;

class ImageWidget extends EcsWidget {
    private final Image image;

    ImageWidget(Point location, Image image) {
        this(location.x, location.y, image);
    }

    ImageWidget(int x, int y, Image image) {
        super(x, y);
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
                "location=" + widgetCoordinates() +
                ", image=" + image +
                '}';
    }

}
