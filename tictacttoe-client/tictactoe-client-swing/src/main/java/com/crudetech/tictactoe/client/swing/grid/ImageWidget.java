package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;
import java.awt.image.BufferedImage;

class ImageWidget {

    private int x, y;
    private final BufferedImage image;

    ImageWidget(Point location, BufferedImage image) {

        this.x = location.x;
        this.y = location.y;
        this.image = image;
    }

    public void paint(Graphics2D g2d) {
        g2d.drawImage(image, null, x, y);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
