package com.crudetech.gui.widgets;

import java.awt.Composite;

public interface GraphicsStream {
    void pushTranslation(int dx, int dy);
    void popTransformation();

    void pushColor(Color color);

    void drawRectangle(Rectangle rectangle);
    void fillRectangle(Rectangle boundary);

    void drawLine(int x, int y, int x1, int y1);

    void drawImage(Image image);

    void pushComposite(Composite composite);
    void popComposite();
}
