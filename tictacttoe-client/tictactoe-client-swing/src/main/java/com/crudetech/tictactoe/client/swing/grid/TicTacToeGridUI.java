package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.tictactoe.game.Grid;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class TicTacToeGridUI extends ComponentUI {
    private Style style = Styles.Brush;
    private JTicTacToeGrid component;

    @SuppressWarnings("unused")
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
        for (Widget w : buildPaintList()) {
            paintWidget(w, pipe);
        }
    }

    private void paintWidget(Widget widget, Graphics2D pipe) {
        AffineTransform oldTransform = pipe.getTransform();
        Point loc = widget.getLocation();
        pipe.setTransform(AffineTransform.getTranslateInstance(loc.getX(), loc.getY()));
        try {
            widget.paintEcs(pipe);
        } finally {
            pipe.setTransform(oldTransform);
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
        List<Widget> paintList = new ArrayList<>();

        paintList.add(backgroundWidget());

        paintList.add(getBackgroundImageWidget(origin));

        paintList.addAll(buildGridMarkWidgetList(origin));

        paintList.add(highlightWidget(origin));

        return paintList;
    }

    private Widget highlightWidget(Point origin) {
        if (component.getModel().hasHighlight()) {
            Rectangle boundaryForLocation = (Rectangle) getBoundaryForLocation(component.getModel().getHighlighted()).clone();
            boundaryForLocation.translate(origin.x, origin.y);
            return new RectangleWidget(boundaryForLocation, style.getHighlightColor());
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
                return new EmptyWidget();
            default:
                throw new RuntimeException("This is no sensible state!");
        }
    }

    public GridCellHit checkGridCellHit(int x, int y) {
        Point ptInUiCoordinates = inUiCoordinates(x, y);
        Iterable<Grid.Cell> allCells = component.getModel().getModelObject().getCells();
        Rectangle[][] hitBoundaries = getStyle().getGridMarkLocations();
        return new GridCellHit(allCells, ptInUiCoordinates.x, ptInUiCoordinates.y, hitBoundaries);
    }

    private Point inUiCoordinates(int x, int y) {
        Point o = computeOrigin();
        return new Point(x - o.x, y - o.y);
    }
}
