package com.crudetech.tictactoe.client.swing.grid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public enum Styles implements Style{
    Brush;
    private BufferedImage background;

    private Styles() {

    }
    @Override
    public BufferedImage getBackgroundImage() {
        return loadBackGroundImage();
    }

    @Override
    public Color getBackgroundColor() {
        return Color.WHITE;
    }
    BufferedImage loadBackGroundImage() {
        if (background == null) {
            try {
                background = loadImage("tic-tac-toe-grid.jpg");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return background;
    }

    private BufferedImage loadImage(String resourceId) throws IOException {
        String resourcePath = getResourcePath(resourceId);
        try(InputStream in = Objects.requireNonNull(getClass().getResourceAsStream(resourcePath))){
            return ImageIO.read(in);
        }
    }

    private String getResourcePath(String resource) {
        return String.format("/%s/%s/%s",
                getClass().getPackage().getName().replace('.', '/'),
                this.name().toLowerCase(), resource);
    }
}
