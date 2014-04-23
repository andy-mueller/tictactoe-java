package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.*;
import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.Font;
import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;

import java.awt.*;
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
    public Context newContext() {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void pushCoordinateSystem(CoordinateSystem coos) {
        pushCurrentTransformationOnStack();
        pushTranslation(coos.getLocation().x, coos.getLocation().y);
        pushScale(coos.getScale());
    }

    private void pushTranslation(int dx, int dy) {
        applyNewTranslation(dx, dy);
    }

    private void pushScale(double scale) {
        pipe.scale(scale, scale);
    }

    private void pushCurrentTransformationOnStack() {
        xforms.add(pipe.getTransform());
    }

    private void applyNewTranslation(int dx, int dy) {
        pipe.translate(dx, dy);
    }

    @Override
    public void popCoordinateSystem() {
        pipe.setTransform(removeLastOf(xforms));
    }

    @Override
    public void pushColor(Color color) {
        colors.add(pipe.getPaint());
        pipe.setPaint(((AwtColor) color).color);
    }

    @Override
    public void popColor() {
        pipe.setPaint(removeLastOf(colors));
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
        pipe.setComposite(convertAlphaToComposite(alpha));
    }

    static AlphaComposite convertAlphaToComposite(AlphaValue alpha) {
        return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.alpha);
    }

    @Override
    public void popAlpha() {
        pipe.setComposite(removeLastOf(composites));
    }

    @Override
    public void drawText(int x, int y, String text) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void pushFont(Font font) {
        throw new RuntimeException("Not implemented yet!");
    }

    private <T> T removeLastOf(List<T> list) {
        return list.remove(list.size() - 1);
    }
}
