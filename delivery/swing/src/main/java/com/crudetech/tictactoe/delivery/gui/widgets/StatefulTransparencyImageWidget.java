package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.*;
import com.crudetech.lang.Objects;

import static java.util.Arrays.asList;


class StatefulTransparencyImageWidget extends CompoundWidget {
    private final TransparencyState transparencyState;
    private final Image image;

    interface TransparencyState {
        boolean isTransparent();
        AlphaValue transparency();
    }
    public StatefulTransparencyImageWidget(TransparencyState transparencyState,
                                           Image image) {
        this.transparencyState = transparencyState;
        this.image = image;
    }

    boolean hasImage(Image image) {
        return Objects.equals(this.image, image);
    }

    @Override
    protected Iterable<Widget> subWidgets() {
        return asList(createBackgroundImage());
    }

    private Widget createBackgroundImage() {
        Widget imageWidget = new ImageWidget(Point.Origin, image);
        if (isTransparent()) {
            return addTransparency(imageWidget);
        }
        return imageWidget;
    }
    boolean isTransparent() {
        return transparencyState.isTransparent();
    }

    private CompositeDecoratorWidget addTransparency(Widget imageWidget) {
        return new CompositeDecoratorWidget(imageWidget, transparencyState.transparency());
    }
}
