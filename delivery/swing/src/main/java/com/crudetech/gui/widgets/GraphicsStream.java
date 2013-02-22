package com.crudetech.gui.widgets;


public interface GraphicsStream {
    void pushTranslation(int dx, int dy);
    void popTransformation();

    void pushColor(Color color);

    void drawRectangle(Rectangle rectangle);
    void fillRectangle(Rectangle boundary);

    void drawLine(Point start, Point end);

    void drawImage(Image image);

    void pushAlpha(AlphaValue alpha);
    void popAlpha();
}
