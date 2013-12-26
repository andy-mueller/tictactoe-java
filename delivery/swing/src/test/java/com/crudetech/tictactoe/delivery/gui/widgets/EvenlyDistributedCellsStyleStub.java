package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.game.Grid;

public class EvenlyDistributedCellsStyleStub implements Style {
    private final Image nought;
    private final Image cross;
    private final Image back;
    private final Rectangle[][] gridMarkLocations = new Rectangle[3][3];
    private final Color backColor = newColor();
    private final Color highlightColor = newColor();
    private final int backgroundImageWidth;
    private final int backgroundImageHeight;
    private final int cellWidth;
    private final int cellHeight;

    public EvenlyDistributedCellsStyleStub(int backgroundImageWidth, int backgroundImageHeight, int cellWidth, int cellHeight) {
        this.backgroundImageWidth = backgroundImageWidth;
        this.backgroundImageHeight = backgroundImageHeight;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        evenlyDistributeCells();
        back = newImageStub(backgroundImageWidth, backgroundImageHeight);
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

    private void evenlyDistributeCells() {
        final int widthDistance = getHorizontalCellDistance();
        final int heightDistance = getVerticalCellDistance();
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                final int x = widthDistance + col * (widthDistance + cellWidth);
                final int y = heightDistance + row * (heightDistance + cellHeight);
                gridMarkLocations[row][col] = new Rectangle(x, y, cellWidth, cellHeight);
            }
        }
    }

    int getVerticalCellDistance() {
        return (backgroundImageHeight - 3 * cellHeight) / 4;
    }

    int getHorizontalCellDistance() {
        return (backgroundImageWidth - 3 * cellWidth) / 4;
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
    public Dimension getMinimumSize() {
        return new Dimension(42, 42);
    }

    @Override
    public Rectangle getGridMarkBoundary(Grid.Location location) {
        return location.selectOf(gridMarkLocations);
    }

    @Override
    public Image getCrossImage() {
        return cross;
    }

    @Override
    public Image getNoughtImage() {
        return nought;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int backgroundImageWidth = 500;
        private int backgroundImageHeight = 1000;
        private int cellWidth = 10;
        private int cellHeight = 10;

        public Builder withBackgroundImageSize(int width, int height) {
            this.backgroundImageWidth = width;
            this.backgroundImageHeight = height;
            return this;
        }


        public Builder withCellSize(int cellWidth, int cellHeight) {
            this.cellWidth = cellWidth;
            this.cellHeight = cellHeight;
            return this;
        }


        public EvenlyDistributedCellsStyleStub build() {
            return new EvenlyDistributedCellsStyleStub(backgroundImageWidth, backgroundImageHeight, cellWidth, cellHeight);
        }
    }
}
