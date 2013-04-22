package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.*;

import static java.util.Arrays.asList;


class StatefulTransparencyImageWidget extends CompoundWidget {
    private final TransparencyState transparencyState;
    private final Image image;
    private final AlphaValue alphaValue;

    interface TransparencyState {
        boolean isTransparent();
    }
    public StatefulTransparencyImageWidget(TransparencyState transparencyState,
                                           Image image,
                                           AlphaValue alphaValue) {
        this.transparencyState = transparencyState;
        this.image = image;
        this.alphaValue = alphaValue;
    }

    @Override
    protected Iterable<Widget> subWidgets() {
        return asList(createBackgroundImage());
    }

    private Widget createBackgroundImage() {
        Widget imageWidget = new ImageWidget(Point.Origin, image);
        if (transparencyState.isTransparent()) {
            return addTransparency(imageWidget);
        }
        return imageWidget;
    }

    private CompositeDecoratorWidget addTransparency(Widget imageWidget) {
        return new CompositeDecoratorWidget(imageWidget, alphaValue);
    }
}
