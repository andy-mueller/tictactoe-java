package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.crudetech.query.Query.from;
import static java.lang.Math.max;

public class TicTacToeGridUI extends ComponentUI {
    private Style style = Styles.Brush;
    private JTicTacToeGrid component;
    static final Composite WinningTripleAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
    private TicTacToeGridWidget gridWidget;

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

    void paint(Graphics2D g2d) {
        buildGridWidget();
        gridWidget.paint(streamInto(g2d));
    }

    private GraphicsStream streamInto(final Graphics2D g2d) {
        return new GraphicsStream() {
            private final List<AffineTransform> xforms = new ArrayList<>();
            private final List<Paint> colors = new ArrayList<>();
            private final List<Composite> composites = new ArrayList<>();
            private final Graphics2D pipe = g2d;

            @Override
            public void pushTranslation(int dx, int dy) {
                xforms.add(pipe.getTransform());
                pipe.translate(dx, dy);
            }

            @Override
            public void popTransformation() {
                pipe.setTransform(xforms.remove(xforms.size() - 1));
            }

            @Override
            public void pushColor(Paint color) {
                colors.add(pipe.getPaint());
                pipe.setPaint(color);
            }


            @Override
            public void drawRectangle(Rectangle rectangle) {
                pipe.draw(rectangle);
            }


            @Override
            public void fillRectangle(Rectangle rectangle) {
                pipe.fill(rectangle);
            }


            @Override
            public void drawLine(int x, int y, int x1, int y1) {
                pipe.drawLine(x, y, x1, y1);
            }


            @Override
            public void drawImage(BufferedImage image) {
                pipe.drawImage(image, null, 0, 0);
            }


            @Override
            public void pushComposite(Composite composite) {
                composites.add(pipe.getComposite());
                pipe.setComposite(composite);
            }


            @Override
            public void popComposite() {
                pipe.setComposite(composites.remove(composites.size() - 1));
            }
        };
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
        buildGridWidget();
        return gridWidget.buildPaintList();
    }

    private void buildGridWidget() {
        gridWidget = new TicTacToeGridWidget(component.getBounds(), style, getModel().getHighlightedTriple(), getModel().getGrid().getCells(), getModel().getHighlightedCell(), isDebugMode);
    }


    private boolean isDebugMode = false;

    void turnOnDebug() {
        isDebugMode = true;
    }


    Point getUiOrigin() {
        BufferedImage backgroundImage = style.getBackgroundImage();
        int x = max((component.getWidth() - backgroundImage.getWidth()) / 2, 0);
        int y = max((component.getHeight() - backgroundImage.getHeight()) / 2, 0);
        return new Point(x, y);
    }


    List<Widget> gridMarkWidgetList() {
        return gridWidget.gridMarkWidgetList();
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
