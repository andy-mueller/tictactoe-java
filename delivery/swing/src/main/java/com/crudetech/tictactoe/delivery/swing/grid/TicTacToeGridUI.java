package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.tictactoe.delivery.gui.widgets.GridCellHit;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridModel;
import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridWidget;
import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridWidgetBuilder;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

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
        buildGraphic();
        gridWidget.paint(streamInto(g2d));
    }

    private GraphicsStream streamInto(final Graphics2D g2d) {
        return new AwtGraphicsStream(g2d);
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

    void buildGraphic() {
        gridWidget = new TicTacToeGridWidgetBuilder()
                .withBounds(WidgetAwtConverter.rectangle(component.getBounds()))
                .withStyle(style)
                .withModel(getModel())
                .noDebug()
                .createTicTacToeGridWidget();
    }

    public GridCellHit checkGridCellHit(int x, int y) {
        return gridWidget.checkGridCellHit(com.crudetech.gui.widgets.Point.of(x, y));
    }

    private TicTacToeGridModel getModel() {
        return component.getModel();
    }

    void repaintAll() {
        component.repaint();
    }

    void repaintCells(Iterable<Grid.Location> changedCells) {
        Iterable<Rectangle> repaintedInComponentCoordinates =
                from(gridWidget.getCellBoundaries(changedCells))
                        .select(toAwtRectangle())
                        .select(toInflatedRectangle());

        for (Rectangle rect : repaintedInComponentCoordinates) {
            component.repaint(rect);
        }
    }

    private UnaryFunction<Rectangle, Rectangle> toInflatedRectangle() {
        return new UnaryFunction<Rectangle, Rectangle>() {
            @Override
            public Rectangle execute(Rectangle rectangle) {
                rectangle.width += 1;
                rectangle.height += 1;
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


}
