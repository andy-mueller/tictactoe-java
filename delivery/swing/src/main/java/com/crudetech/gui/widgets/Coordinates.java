package com.crudetech.gui.widgets;

public abstract class Coordinates {
    public abstract <TTransformable extends Transformable<TTransformable>>
    TTransformable toWidgetCoordinates(Widget widget, TTransformable transformable);

    public abstract <TTransformable extends Transformable<TTransformable>>
    TTransformable toWorldCoordinates(Widget widget, TTransformable transformable);

    public static Coordinates World = new Coordinates() {

        @Override
        public <TTransformable extends Transformable<TTransformable>>
        TTransformable toWidgetCoordinates(Widget widget, TTransformable transformable) {
            return widget.widgetCoordinates().toWidgetCoordinates(transformable);
        }

        @Override
        public <TTransformable extends Transformable<TTransformable>>
        TTransformable toWorldCoordinates(Widget widget, TTransformable transformable) {
            return transformable;
        }
    };
    public static Coordinates Widget = new Coordinates() {
        @Override
        public <TTransformable extends Transformable<TTransformable>>
        TTransformable toWidgetCoordinates(Widget widget, TTransformable transformable) {
            return transformable;
        }

        @Override
        public <TTransformable extends Transformable<TTransformable>>
        TTransformable toWorldCoordinates(Widget widget, TTransformable transformable) {
            return widget.widgetCoordinates().toWorldCoordinates(transformable);
        }
    };
}
