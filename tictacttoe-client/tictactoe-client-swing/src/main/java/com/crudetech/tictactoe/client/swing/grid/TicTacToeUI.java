package com.crudetech.tictactoe.client.swing.grid;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TicTacToeUI extends ComponentUI {
    private BufferedImage grid;
    private BufferedImage cross;
    private BufferedImage nought;
    private Style style = Styles.Brush;
    private JTicTacToeGrid component;

    public TicTacToeUI() {
        grid = loadImage("/com/crudetech/tictactoe/client/swing/grid/brush/tic-tac-toe-grid.jpg");
        cross = loadImage("/com/crudetech/tictactoe/client/swing/grid/brush/tic-tac-toe-cross.jpg");
        nought = loadImage("/com/crudetech/tictactoe/client/swing/grid/brush/tic-tac-toe-nought.jpg");
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
        component = (JTicTacToeGrid)c;
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

        ImageWidget backGround = new ImageWidget(new Point(0, 0), style.getBackgroundImage());
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
        return new Dimension(this.grid.getWidth(), this.grid.getHeight());
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
