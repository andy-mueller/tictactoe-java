package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;


class AwtGraphicsStream implements GraphicsStream {
    private final List<AffineTransform> xforms = new ArrayList<>();
    private final List<Paint> colors = new ArrayList<>();
    private final List<Composite> composites = new ArrayList<>();
    private final Graphics2D pipe;

    public AwtGraphicsStream(Graphics2D g2d) {
        this.pipe = g2d;
    }

    @Override
    public void pushTranslation(int dx, int dy) {
        xforms.add(pipe.getTransform());
        pipe.translate(dx, dy);
    }

    @Override
    public void popTransformation() {
        pipe.setTransform(removeLast(xforms));
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
        pipe.drawImage(((AwtImage) image).image, null, 0, 0);
    }

    @Override
    public void pushAlpha(AlphaValue alpha) {
        composites.add(pipe.getComposite());
        pipe.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.alpha));
    }

    @Override
    public void popAlpha() {
        pipe.setComposite(removeLast(composites));
    }

    private <T> T removeLast(List<T> list) {
        return list.remove(list.size() - 1);
    }
}
