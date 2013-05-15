package com.crudetech.gui.widgets;

public interface Transformable<TTransformable extends Transformable> {
    TTransformable transformBy(Transformation xform);
}
