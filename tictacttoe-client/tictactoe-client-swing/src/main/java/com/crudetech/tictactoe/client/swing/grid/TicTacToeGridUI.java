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
        Point origin = computeOrigin();
        List<Widget> paintList = new ArrayList<>(11);

        paintList.add(backgroundWidget());

        paintList.add(getBackgroundImageWidget(origin));

        paintList.addAll(buildGridMarkWidgetList(origin));

        paintList.add(highlightWidget());

        if (component.getModel().hasHighlight()) {
            paintList.add(new RectangleWidget(getBoundaryForLocation(component.getModel().getHighlighted()), style.getHighlightColor()));
        }
        return paintList;
    }

    private Widget highlightWidget() {
        if (component.getModel().hasHighlight()) {
            return new RectangleWidget(getBoundaryForLocation(component.getModel().getHighlighted()), style.getHighlightColor());
        } else {
            return new EmptyWidget();
        }
    }

    private Widget backgroundWidget() {
        return new FilledRectangleWidget(component.getBounds(), style.getBackgroundColor());
    }

    private Point computeOrigin() {
        BufferedImage backgroundImage = style.getBackgroundImage();
        int x = max((component.getWidth() - backgroundImage.getWidth()) / 2, 0);
        int y = max((component.getHeight() - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }

    private Widget getBackgroundImageWidget(Point origin) {
        BufferedImage backgroundImage = style.getBackgroundImage();
        return new ImageWidget(origin, backgroundImage);
    }


    List<Widget> buildGridMarkWidgetList(Point gridOrigin) {
        List<Widget> gridMArks = new ArrayList<>(9);
        for (Grid.Cell cell : component.getModel().getModelObject().getCells()) {
            Rectangle bounds = getBoundaryForLocation(cell.getLocation());
            Widget widget = createMarkWidget(cell.getMark(), bounds);
            widget.moveBy(gridOrigin.x, gridOrigin.y);
            gridMArks.add(widget);
        }
        return gridMArks;
    }

    private Rectangle getBoundaryForLocation(Grid.Location location) {
        return style.getGridMarkLocations()[location.getRow().ordinal()][location.getColumn().ordinal()];
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
