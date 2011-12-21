package com.crudetech.tictactoe.client.swing.grid;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.Math.max;

public class TicTacToeUI extends ComponentUI {
    private BufferedImage cross;
    private BufferedImage nought;
    private Style style = Styles.Brush;
    private JTicTacToeGrid component;

    public TicTacToeUI() {
        cross = loadImage("/com/crudetech/tictactoe/client/swing/grid/brushstyle/tic-tac-toe-cross.jpg");
        nought = loadImage("/com/crudetech/tictactoe/client/swing/grid/brushstyle/tic-tac-toe-nought.jpg");
    }

    public static TicTacToeUI createUI(JComponent component) {
        return new TicTacToeUI();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;

        paint(g2d);
    }

    @Override
    public void installUI(JComponent c) {
        component = (JTicTacToeGrid) c;
        super.installUI(c);
    }

    @Override
    public void uninstallUI(JComponent c) {
        component = null;
        super.uninstallUI(c);
    }

    void paint(Graphics2D pipe) {
        pipe.setPaint(style.getBackgroundColor());
        pipe.fillRect(0, 0, component.getWidth(), component.getHeight());

        BufferedImage backgroundImage = style.getBackgroundImage();
        int backgroundX = max((component.getWidth() - backgroundImage.getWidth())/2, 0);
        int backgroundY = max((component.getHeight() - backgroundImage.getHeight())/2, 0);
        ImageWidget backGround = new ImageWidget(new Point(backgroundX, backgroundY), backgroundImage);
        backGround.paint(pipe);
    }

    private BufferedImage loadImage(String path) {
        try {
            try (InputStream imageStream = getClass().getResourceAsStream(path)) {
                return ImageIO.read(imageStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return style.getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return getMinimumSize(c);
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }
}
