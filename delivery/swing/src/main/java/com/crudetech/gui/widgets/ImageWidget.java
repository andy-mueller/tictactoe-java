package com.crudetech.gui.widgets;

import java.util.Objects;

public class ImageWidget extends EcsWidget {
    private final Image image;

    public ImageWidget(Point location, Image image) {
        this(location.x, location.y, image);
    }

    public ImageWidget(int x, int y, Image image) {
        super(x, y);
        this.image = image;
    }

    public ImageWidget(Image image) {
        this(Point.Origin, image);
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
                "location=" + coordinateSystem() +
                ", image=" + image +
                '}';
    }

}
