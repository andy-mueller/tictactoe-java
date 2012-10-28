package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.collections.Iterables;
import com.crudetech.functional.UnaryFunction;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.crudetech.query.Query.from;
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

    private static class Graphics2dTransform implements AutoCloseable {
        private final AffineTransform originalXForm;
        private final Graphics2D pipe;

        Graphics2dTransform(Graphics2D pipe) {
            this.pipe = pipe;
            originalXForm = pipe.getTransform();
        }

        @Override
        public void close() {
            pipe.setTransform(originalXForm);
        }

        void pushTranslation(double dx, double dy) {
            AffineTransform translate = AffineTransform.getTranslateInstance(dx, dy);
            pipe.transform(translate);
        }
    }

    private void paintWidget(Widget widget, Graphics2D pipe) {
        try (Graphics2dTransform xform = new Graphics2dTransform(pipe)) {
            Point loc = widget.getLocation();
            xform.pushTranslation(loc.getX(), loc.getY());
            widget.paintEcs(pipe);
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
        paintList.add(backgroundImageWidget());
        paintList.addAll(gridMarkWidgetList());
        paintList.add(highlightWidget());
        paintList.add(debugInfoWidget());


        return paintList;
    }

    private Widget debugInfoWidget() {
        return isDebugMode ? new DebugWidget() : new EmptyWidget();
    }

    private boolean isDebugMode = false;

    void turnOnDebug() {
        isDebugMode = true;
    }

    class DebugWidget extends EcsWidget {
        @Override
        public void paintEcs(Graphics2D pipe) {
            System.out.println("---->Painting @" + new Date());
            pipe.setPaint(Color.ORANGE);
            for (int row = 0; row < component.getHeight(); row += 50) {
                pipe.drawLine(0, row, component.getWidth(), row);
            }
            for (int col = 0; col < component.getWidth(); col += 50) {
                pipe.drawLine(col, 0, col, component.getHeight());
            }
        }
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
        Rectangle bounds = component.getBounds();
        bounds.height += 500;
        return new FilledRectangleWidget(bounds, style.getBackgroundColor());
    }

    Point getUiOrigin() {
        BufferedImage backgroundImage = style.getBackgroundImage();
        int x = max((component.getWidth() - backgroundImage.getWidth()) / 2, 0);
        int y = max((component.getHeight() - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }

    private Widget backgroundImageWidget() {
        BufferedImage backgroundImage = style.getBackgroundImage();

        ImageWidget imageWidget = new ImageWidget(getUiOrigin(), backgroundImage);

        return hasHighlightedTriple() ? new CompositeDecoratorWidget(imageWidget, WinningTripleAlpha) : imageWidget;
    }

    private boolean hasHighlightedTriple() {
        return getModel().hasHighlightedTriple();
    }


    List<Widget> gridMarkWidgetList() {
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

    public void repaintAll() {
        component.repaint();
    }

    public void repaintCells(Iterable<Grid.Location> changedCells) {
        Iterable<Rectangle> repaintedInComponentCoordinates =
                from(changedCells).select(toBoundary()).select(toComponentCoos());

        for (Rectangle rect : repaintedInComponentCoordinates) {
            component.repaint(rect);
        }
    }

    private UnaryFunction<Rectangle, Rectangle> toComponentCoos() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle rectangle) {
                rectangle = new Rectangle(rectangle);
                Point origin = getUiOrigin();
                rectangle.translate(origin.x, origin.y);
                rectangle.width += 1;
                rectangle.height += 1;
                return rectangle;
            }
        };
    }

    private UnaryFunction<Grid.Location, Rectangle> toBoundary() {
        return new UnaryFunction<Grid.Location, Rectangle>() {
            @Override
            public Rectangle execute(Grid.Location location) {
                return getStyle().getGridMarkLocations()[location.getRow().ordinal()][location.getColumn().ordinal()];
            }
        };
    }
}
