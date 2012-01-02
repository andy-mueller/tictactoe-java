package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.tictactoe.game.Grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class TicTacToeGridUI extends ComponentUI {
    private Style style = Styles.Brush;
    private JTicTacToeGrid component;

    public static TicTacToeGridUI createUI(JComponent component) {
        return new TicTacToeGridUI();
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
        List<Widget> paintList = buildPaintList();

        for (Widget w : paintList) {
            w.paint(pipe);
        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return style.getPreferredSize();
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    List<Widget> buildPaintList() {
        List<Widget> paintList = new ArrayList<>(11);
        Widget cleanBackground = new FilledRectangleWidget(component.getBounds(), style.getBackgroundColor());
        paintList.add(cleanBackground);

        BufferedImage backgroundImage = style.getBackgroundImage();
        int backgroundX = max((component.getWidth() - backgroundImage.getWidth()) / 2, 0);
        int backgroundY = max((component.getHeight() - backgroundImage.getHeight()) / 2, 0);
        Widget backGround = new ImageWidget(new Point(backgroundX, backgroundY), backgroundImage);
        paintList.add(backGround);

        paintList.addAll(buildGridMarkWidgetList(backgroundX, backgroundY));
        return paintList;
    }

    List<Widget> buildGridMarkWidgetList(int paintOffsetX, int paintOffsetY) {
        List<Widget> gridMArks = new ArrayList<>(9);
        final Rectangle[][] markBounds = style.getGridMarkLocations();
        for (Grid.Cell cell : component.getModel().getModelObject().getCells()) {
            Grid.Location location = cell.getLocation();
            Rectangle bounds = markBounds[location.getRow().ordinal()][location.getColumn().ordinal()];
            Widget widget = createMarkWidget(cell.getMark(), bounds);
            widget.moveBy(paintOffsetX, paintOffsetY);
            gridMArks.add(widget);
        }
        return gridMArks;
    }

    private Widget createMarkWidget(Grid.Mark mark, Rectangle bounds) {
        switch (mark) {
            case Cross:
                return new ImageWidget(bounds.getLocation(), style.getCrossImage());
            case Nought:
                return new ImageWidget(bounds.getLocation(), style.getNoughtImage());
            case None:
                return new FilledRectangleWidget(bounds, style.getBackgroundColor());
            default:
                throw new RuntimeException("This is no sensible state!");
        }
    }
}
