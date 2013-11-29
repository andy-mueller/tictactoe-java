package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.delivery.gui.widgets.Dimension;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.game.Grid;

import javax.imageio.ImageIO;
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
            return new AwtColor(java.awt.Color.WHITE);
        }

        @Override
        public Rectangle getGridMarkBoundary(Grid.Location location) {
            int width = 215;
            int height = 170;

            int x0 = 55;
            int y0 = 98;
            int x1 = 320;
            int y1 = 310;
            int x2 = 600;
            int y2 = 538;
            Rectangle[][] rectLocations = new Rectangle[][]{
                    {new Rectangle(x0, y0, width, height), new Rectangle(x1, y0, width, height), new Rectangle(x2, y0, width, height)},
                    {new Rectangle(x0, y1, width, height), new Rectangle(x1, y1, width, height), new Rectangle(x2, y1, width, height)},
                    {new Rectangle(x0, y2, width, height), new Rectangle(x1, y2, width, height), new Rectangle(x2, y2, width, height)},
            };
            return location.selectOf(rectLocations);
        }

        @Override
        String getBackgroundImageName() {
            return "tic-tac-toe-grid.jpg";
        }

        @Override
        public Color getHighlightColor() {
            return new AwtColor(java.awt.Color.RED);
        }
    };

    private Image background;
    private Image cross;
    private Image nought;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getBackgroundImage().getWidth(), getBackgroundImage().getHeight());
    }

    @Override
    public Image getCrossImage() {
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

    @Override
    public Image getNoughtImage() {
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
    public Image getBackgroundImage() {
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


    private Image loadImage(String resourceId) throws IOException {
        String resourcePath = getResourcePath(resourceId);
        try (InputStream in = Objects.requireNonNull(getClass().getResourceAsStream(resourcePath))) {
            return new AwtImage(ImageIO.read(in));
        }
    }

    private String getResourcePath(String resource) {
        return String.format("/%s/%sstyle/%s",
                getClass().getPackage().getName().replace('.', '/'),
                this.name().toLowerCase(),
                resource);
    }
}
