package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.Coordinates;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.tictactoe.delivery.gui.widgets.GridCellHit;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridModel;
import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridWidget;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.Date;

import static com.crudetech.query.Query.from;


public class TicTacToeGridUI extends ComponentUI {
    private Style style = Styles.Brush;
    private JTicTacToeGrid component;
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
        getGridWidget().paint(streamInto(g2d));
    }

    private GraphicsStream streamInto(final Graphics2D g2d) {
        return new AwtGraphicsStream(g2d);
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return WidgetAwtConverter.dimension(style.getMinimumSize());
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

    void buildGraphic() {
        gridWidget = TicTacToeGridWidget.builder()
                .withBounds(WidgetAwtConverter.rectangle(component.getBounds()))
                .withStyle(style)
                .withModel(getModel())
                .noDebug()
                .createTicTacToeGridWidget();
    }

    public GridCellHit gridCellHit(int x, int y) {
        com.crudetech.gui.widgets.Point hitPoint = com.crudetech.gui.widgets.Point.of(x, y);
        return getGridWidget().gridCellHit(hitPoint, Coordinates.World);
    }

    private TicTacToeGridModel getModel() {
        return component.getModel();
    }

    void invalidate() {
        invalidateGraphic();
    }

    private void invalidateGraphic() {
        System.out.println("---->" + new Date() + ": invalidating graphic");
        gridWidget = null;
    }

    public void repaint() {
        invalidateGraphic();
        component.repaint();
    }

    void repaintCells(Iterable<Grid.Location> changedCells) {
        invalidateGraphic();
        for (Rectangle rect : getRectanglesForCells(changedCells)) {
            component.repaint(rect);
        }
    }

    Iterable<Rectangle> getRectanglesForCells(Iterable<Grid.Location> cells) {
        return from(getGridWidget().getCellBoundaries(cells))
                .select(toAwtRectangle())
                .select(toInflatedRectangle());
    }

    private UnaryFunction<Rectangle, Rectangle> toInflatedRectangle() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle rectangle) {
                rectangle.width += 3;
                rectangle.height += 3;
                return rectangle;
            }
        };
    }

    private UnaryFunction<com.crudetech.gui.widgets.Rectangle, Rectangle> toAwtRectangle() {
        return new UnaryFunction<com.crudetech.gui.widgets.Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(com.crudetech.gui.widgets.Rectangle rectangle) {
                return WidgetAwtConverter.rectangle(rectangle);
            }
        };
    }


    private TicTacToeGridWidget getGridWidget() {
        if (gridWidget == null) {
            buildGraphic();
        }
        return gridWidget;
    }
}
