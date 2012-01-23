package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.collections.Iterables;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.AlphaComposite;
import java.awt.Composite;
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
    static final Composite WinningTripleAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);

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
        List<Widget> paintList = new ArrayList<>();

        paintList.add(backgroundWidget());

        paintList.add(getBackgroundImageWidget());

        paintList.addAll(buildGridMarkWidgetList());

        paintList.add(highlightWidget());

        return paintList;
    }

    private Widget highlightWidget() {
        Point origin = getUiOrigin();
        if (component.getModel().hasHighlightedCell()) {
            Rectangle boundaryForLocation = (Rectangle) getBoundaryForLocation(component.getModel().getHighlightedCell()).clone();
            boundaryForLocation.translate(origin.x, origin.y);
            return new RectangleWidget(boundaryForLocation, style.getHighlightColor());
        } else {
            return new EmptyWidget();
        }
    }

    private Widget backgroundWidget() {
        return new FilledRectangleWidget(component.getBounds(), style.getBackgroundColor());
    }

    private Point getUiOrigin() {
        BufferedImage backgroundImage = style.getBackgroundImage();
        int x = max((component.getWidth() - backgroundImage.getWidth()) / 2, 0);
        int y = max((component.getHeight() - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }

    private Widget getBackgroundImageWidget() {
        BufferedImage backgroundImage = style.getBackgroundImage();
        return new ImageWidget(getUiOrigin(), backgroundImage);
    }


    List<Widget> buildGridMarkWidgetList() {
        Point gridOrigin = getUiOrigin();
        List<Widget> gridMArks = new ArrayList<>(9);
        for (Grid.Cell cell : component.getModel().getGrid().getCells()) {
            Rectangle bounds = getBoundaryForLocation(cell.getLocation());
            Widget widget = wrapTransparentIfIsNotInHighlightedWinningTriple(createMarkWidget(cell.getMark(), bounds), cell.getLocation());
            widget.moveBy(gridOrigin.x, gridOrigin.y);
            gridMArks.add(widget);
        }
        return gridMArks;
    }

    private Widget wrapTransparentIfIsNotInHighlightedWinningTriple(Widget widget, Grid.Location location) {
        if (noWinningTripleHighlighted() || isInWinningTriple(location)) {
            return widget;
        }
        return new CompositeDecoratorWidget(widget, WinningTripleAlpha);
    }

    private boolean isInWinningTriple(Grid.Location location) {
        return Iterables.contains(getModel().getHighlightedTriple().getLocations(), location);
    }

    private boolean noWinningTripleHighlighted() {
        return !getModel().hasHighlightedTriple();
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
        Iterable<Grid.Cell> allCells = component.getModel().getGrid().getCells();
        Rectangle[][] hitBoundaries = getStyle().getGridMarkLocations();
        return new GridCellHit(allCells, ptInUiCoordinates.x, ptInUiCoordinates.y, hitBoundaries);
    }

    private Point inUiCoordinates(int x, int y) {
        Point o = getUiOrigin();
        return new Point(x - o.x, y - o.y);
    }

    private TicTacToeGridModel getModel() {
        return component.getModel();
    }
}
