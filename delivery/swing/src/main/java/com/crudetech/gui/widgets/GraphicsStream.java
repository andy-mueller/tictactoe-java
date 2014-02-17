package com.crudetech.gui.widgets;


public interface GraphicsStream {
    void pushCoordinateSystem(CoordinateSystem coos);
    void popCoordinateSystem();

    void pushColor(Color color);
    void popColor();

    void drawRectangle(Rectangle rectangle);
    void fillRectangle(Rectangle boundary);

    void drawLine(Point start, Point end);

    void drawImage(Image image);

    void pushAlpha(AlphaValue alpha);
    void popAlpha();

    void drawText(int x, int y, String text);

    void pushFont(Font font);
}
