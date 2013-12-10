package com.crudetech.gui.widgets;

public class DecoratorWidget<TDecorated extends Widget> extends DecoratorTemplateWidget<TDecorated> {
    private final TDecorated decorated;

    protected DecoratorWidget(TDecorated decorated) {
        this.decorated = decorated;
    }

    @Override
    public TDecorated getDecorated() {
        return decorated;
    }
}
