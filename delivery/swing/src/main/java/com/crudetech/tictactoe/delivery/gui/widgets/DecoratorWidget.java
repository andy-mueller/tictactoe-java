package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Widget;

class DecoratorWidget<TDecorated extends Widget> extends DecoratorTemplateWidget<TDecorated> {
    private final TDecorated decorated;
    DecoratorWidget(TDecorated decorated) {
        this.decorated = decorated;
    }

    @Override
    TDecorated getDecorated() {
        return decorated;
    }
}
