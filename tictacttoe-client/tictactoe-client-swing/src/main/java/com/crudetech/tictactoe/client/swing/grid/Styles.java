package com.crudetech.tictactoe.client.swing.grid;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public enum Styles implements Style {
    Brush() {
        @Override
        String getCrossImageName() {
            return "tic-tac-toe-cross.jpg";
        }

        @Override
        String getNoughtImageName() {
            return "tic-tac-toe-nought.jpg";
        }

        @Override
        public Color getBackgroundColor() {
            return Color.WHITE;
        }

        @Override
        public Rectangle[][] getGridMarkLocations() {
            int width = 215;
            int height = 170;

            int x0 = 55;
            int y0 = 98;
            int x1 = 320;
            int y1 = 310;
            int x2 = 600;
            int y2 = 538;
            return new Rectangle[][]{
                    {new Rectangle(x0, y0, width, height), new Rectangle(x1, y0, width, height), new Rectangle(x2, y0, width, height)},
                    {new Rectangle(x0, y1, width, height), new Rectangle(x1, y1, width, height), new Rectangle(x2, y1, width, height)},
                    {new Rectangle(x0, y2, width, height), new Rectangle(x1, y2, width, height), new Rectangle(x2, y2, width, height)},
            };
        }

        @Override
        String getBackgroundImageName() {
            return "tic-tac-toe-grid.jpg";
        }

        @Override
        public Color getHighlightColor() {
            return Color.RED;
        }
    };

    private BufferedImage background;
    private BufferedImage cross;
    private BufferedImage nought;

    private Styles() {
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getBackgroundImage().getWidth(), getBackgroundImage().getHeight());
    }

    public BufferedImage getCrossImage() {
        if (cross == null) {
            try {
                cross = loadImage(getCrossImageName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return cross;
    }

    abstract String getCrossImageName();

    public BufferedImage getNoughtImage() {
        if (nought == null) {
            try {
                nought = loadImage(getNoughtImageName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return nought;
    }

    abstract String getNoughtImageName();

    @Override
    public BufferedImage getBackgroundImage() {
        if (background == null) {
            try {
                background = loadImage(getBackgroundImageName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return background;
    }

    abstract String getBackgroundImageName();


    private BufferedImage loadImage(String resourceId) throws IOException {
        String resourcePath = getResourcePath(resourceId);
        InputStream in = Objects.requireNonNull(getClass().getResourceAsStream(resourcePath));
        try {
            return ImageIO.read(in);
        } finally {
            in.close();
        }
    }

    private String getResourcePath(String resource) {
        return String.format("/%s/%sstyle/%s",
                getClass().getPackage().getName().replace('.', '/'),
                this.name().toLowerCase(),
                resource);
    }
}
