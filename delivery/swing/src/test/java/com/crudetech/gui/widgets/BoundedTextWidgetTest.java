package com.crudetech.gui.widgets;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BoundedTextWidgetTest {
    @Test
    public void givenTextIsSmallerThanBoundary_textIsPaintedAsItIs() throws Exception {
        Rectangle boundary = new Rectangle(2, 3, 10000, 10000);
        BoundedTextWidget boundedTextWidget = new BoundedTextWidget(boundary, "short text");
        GraphicsStream pipe = mock(GraphicsStream.class);

        boundedTextWidget.paint(pipe);

        verify(pipe).drawText(0, 0, "short text");
    }

    static class BoundedTextWidget extends EcsWidget {
        private final Rectangle boundary;
        private final String text;

        public BoundedTextWidget(Rectangle boundary, String text) {
            this.boundary = boundary;
            this.text = text;
        }

        @Override
        protected void paintEcs(GraphicsStream pipe) {
            pipe.drawText(0, 0, text);
        }
    }
    // boundary location is set to origin
    // pushes font
    // adjustFontHeight when necessary
    // adjust fint size when to wide
    // throws when lower/upper font size limit is reached
    //      ->later maybe scale? Encapsulate Sizing in sub class!
}
