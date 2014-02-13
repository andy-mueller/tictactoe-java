package com.crudetech.gui.widgets;


public interface GraphicsStream {

    Context newContext();

    interface Context extends AutoCloseable{
        void pushCoordinateSystem(CoordinateSystem coos);
        void popCoordinateSystem();

        void pushColor(Color color);
        void popColor();
        void pushAlpha(AlphaValue alpha);
        void popAlpha();
        void pushFont(Font font);

        @Override
        void close();
    }


    void drawRectangle(Rectangle rectangle);
    void fillRectangle(Rectangle boundary);

    void drawLine(Point start, Point end);

    void drawImage(Image image);


    void drawText(int x, int y, String text);
}
