package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;

public class StyleStub implements Style {
    private final Image nought;
    private final Image cross;
    private final Image back;
    private final Rectangle[][] locations = new Rectangle[3][3];
    private final Color backColor = newColor();
    private final Color highlightColor = newColor();

    public StyleStub(int width, int height, int cellWidth, int cellHeight) {
        fillLocations(cellWidth, cellHeight, width, height);
        back = newImageStub(width, height);
        cross = newImageStub(cellWidth, cellHeight);
        nought = newImageStub(cellWidth, cellHeight);
    }

    private Image newImageStub(final int width, final int height) {
        return new Image() {
            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }
        };
    }

    private Color newColor() {
        return new Color() {
        };
    }

    private void fillLocations(int cellWidth, int cellHeight, int width, int height) {
        int widthDistance = (width - 3 * cellWidth) / 4;
        int heightDistance = (height - 3 * cellHeight) / 4;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                locations[row][col] = new Rectangle(widthDistance + col * (widthDistance + cellWidth), heightDistance + row * (heightDistance + cellHeight), cellWidth, cellHeight);
            }
        }
    }

    @Override
    public Image getBackgroundImage() {
        return back;
    }

    @Override
    public Color getBackgroundColor() {
        return backColor;
    }

    @Override
    public Color getHighlightColor() {
        return highlightColor;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(42, 42);
    }

    @Override
    public Rectangle[][] getGridMarkLocations() {
        return locations;
    }

    @Override
    public Image getCrossImage() {
        return cross;
    }

    @Override
    public Image getNoughtImage() {
        return nought;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder {
        private int width = 500;
        private int height = 1000;
        private int cellWidth = 10;
        private int cellHeight = 10;

        public Builder withBackgroundImageSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }


        public Builder withCellSize(int cellWidth, int cellHeight) {
            this.cellWidth = cellWidth;
            this.cellHeight = cellHeight;
            return this;
        }


        public StyleStub build() {
            return new StyleStub(width, height, cellWidth, cellHeight);
        }
    }
}
