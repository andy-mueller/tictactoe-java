package com.crudetech.gui.widgets;


public class EmptyWidget extends EcsWidget {
    @Override
    public void paintEcs(GraphicsStream pipe) {
    }

    @Override
    public void paint(GraphicsStream pipe) {
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        return -42;
    }
}
