package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Point;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class ScoreboardUI extends ComponentUI {
    @SuppressWarnings("unused")
    public static ScoreboardUI createUI(JComponent component) {
        return new ScoreboardUI();
    }

    static class ScoreboardUnitWidget {
        static final double widthOverHeight = 0.85;
        static final int baseHeight = 150;
        Point location = Point.Origin;

        int getWidth() {
            return partOf(getHeight(), widthOverHeight);
        }

        private int partOf(int whole, double part) {
            return (int) (whole * part);
        }

        int getHeight() {
            return baseHeight;
        }

        Rectangle getNameRectangle() {
            final double nameBoxHeightRatio = 0.3;
            final double nameBoxWidthRatio = 0.85;
            return null;
        }

        void paint(Graphics2D pipe) {
            paintBackground(pipe);
        }

        private void paintBackground(Graphics2D pipe) {
            pipe.setColor(Color.WHITE);
            pipe.fill(new Rectangle(location.x, location.y, getWidth(), getHeight()));
        }

        private void paintDebugSeparators(Graphics pipe, int width, int height) {
            final int horizontalSeparatorHeight = (int) (height * (1.0 / 3));

            pipe.setColor(Color.RED);
            pipe.drawLine(0, horizontalSeparatorHeight, width, horizontalSeparatorHeight);

            int vertSeparator = (int) (width * (0.66));
            pipe.drawLine(vertSeparator, horizontalSeparatorHeight, vertSeparator, 0);

        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        ScoreboardUnitWidget scoreboardUnit = new ScoreboardUnitWidget();

        scoreboardUnit.paint((Graphics2D) g);

//        paintDebugSeparators(g, width, height);
//
//        final int horizontalSeparatorHeight = (int) (height * (1.0 / 3));
//
//        pipe.setFont(new Font("Arial", Font.PLAIN, 20));
//        pipe.setColor(Color.RED);
//        final int textYPos = (horizontalSeparatorHeight - 20) / 2 + 20;
//        String playerName = "Marianne";
//        Graphics2D namePipe = (Graphics2D) pipe.create();
//        namePipe.translate(10, textYPos);
//        Rectangle2D boundary = measureStringBoundary(playerName, namePipe);
//        namePipe.drawString(playerName, 0, 0);
//        namePipe.setColor(Color.YELLOW);
//        namePipe.draw(boundary);
//        namePipe.dispose();
//
//        pipe.setFont(new Font("Arial", Font.PLAIN, 50));
//        pipe.setColor(Color.RED);
//        final int scoreYPos = 130;
//        pipe.drawString("12", 20, scoreYPos);
    }

    private Rectangle2D measureStringBoundary(String str, Graphics2D pipe) {
        FontRenderContext fontRenderContext = pipe.getFontRenderContext();
        Font currentFont = pipe.getFont();
        return measureStringBoundary(str, currentFont, fontRenderContext);
    }

    private Rectangle2D measureStringBoundary(String str, Font currentFont, FontRenderContext fontRenderContext) {
        return currentFont.getStringBounds(str, fontRenderContext);
    }

}
