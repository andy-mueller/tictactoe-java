package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.*;

import static java.util.Arrays.asList;


public class BackgroundImageWidget extends CompoundWidget{
    private final TicTacToeGridModel model;
    private final Image image;
    private final AlphaValue alphaValue;

    public BackgroundImageWidget(TicTacToeGridModel model,
                                 Image image,
                                 AlphaValue alphaValue) {

        this.model = model;
        this.image = image;
        this.alphaValue = alphaValue;
    }

    @Override
    protected Iterable<Widget> subWidgets() {
        return asList(createBackgroundImage());
    }

    private Widget createBackgroundImage() {
        Widget imageWidget =  new ImageWidget(Point.Origin, image);
        if(model.hasHighlightedThreeInARow()){
            return new CompositeDecoratorWidget(imageWidget, alphaValue);
        }
        return imageWidget;
    }
}
