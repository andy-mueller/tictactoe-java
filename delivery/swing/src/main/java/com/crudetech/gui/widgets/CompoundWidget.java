package com.crudetech.gui.widgets;

public abstract class CompoundWidget extends EcsWidget{
    protected abstract Iterable<Widget> subWidgets();
    @Override
    protected void paintEcs(GraphicsStream pipe) {
        for (Widget widget : subWidgets()) {
            widget.paint(pipe);
        }
    }
}
