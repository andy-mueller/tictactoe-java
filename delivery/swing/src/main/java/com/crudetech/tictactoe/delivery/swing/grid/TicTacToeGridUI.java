package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import static com.crudetech.query.Query.from;



public class TicTacToeGridUI extends ComponentUI {
    private Style style = Styles.Brush;
    private JTicTacToeGrid component;
    static final AlphaValue WinningTripleAlpha = new AlphaValue(0.4f);
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
        buildGraphic();
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
            public void pushColor(Color color) {
                colors.add(pipe.getPaint());
                pipe.setPaint(((AwtColor) color).color);
            }


            @Override
            public void drawRectangle(Rectangle rectangle) {
                pipe.draw(WidgetAwtConverter.rectangle(rectangle));
            }

            @Override
            public void fillRectangle(Rectangle rectangle) {
                pipe.fill(WidgetAwtConverter.rectangle(rectangle));
            }


            @Override
            public void drawLine(com.crudetech.gui.widgets.Point start, com.crudetech.gui.widgets.Point end) {
                pipe.drawLine(start.x, start.y, end.x, end.y);
            }


            @Override
            public void drawImage(Image image) {
                pipe.drawImage(((AwtImage)image).image, null, 0, 0);
            }


            @Override
            public void pushAlpha(AlphaValue alpha) {
                composites.add(pipe.getComposite());
                pipe.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.alpha));
            }


            @Override
            public void popAlpha() {
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
        com.crudetech.tictactoe.delivery.gui.widgets.Dimension preferredSize = style.getPreferredSize();
        return new Dimension(preferredSize.width, preferredSize.height);
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    List<Widget> buildPaintList() {
        buildGraphic();
        return gridWidget.buildPaintList();
    }

    void buildGraphic() {
        gridWidget = new TicTacToeGridWidget(WidgetAwtConverter.rectangle(component.getBounds()),
                style, getModel().getHighlightedTriple(),
                getModel().getGrid().getCells(),
                getModel().getHighlightedCell(),
                isDebugMode,
                new AwtColor(java.awt.Color.ORANGE));
    }


    private boolean isDebugMode = false;

    void turnOnDebug() {
        isDebugMode = true;
    }


    Point getUiOrigin() {
         return WidgetAwtConverter.point(gridWidget.getUiOrigin());
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
            component.repaint(WidgetAwtConverter.rectangle(rect));
        }
    }

    private UnaryFunction<Rectangle, Rectangle> toComponentCoos() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle r) {
                Point origin = getUiOrigin();
                return new Rectangle(r.x + origin.x, r.y + origin.y, r.width + 1, r.height + 1);
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
