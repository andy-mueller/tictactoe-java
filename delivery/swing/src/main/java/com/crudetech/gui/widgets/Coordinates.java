package com.crudetech.gui.widgets;

public abstract class Coordinates {
    public abstract Point toWidgetCoordinates(Widget widget, Point point);

    public abstract Point toWorldCoordinates(Widget widget, Point point);

    public static Coordinates World = new Coordinates() {

        @Override
        public Point toWidgetCoordinates(Widget widget, Point point) {
            return widget.widgetCoordinates().toWidgetCoordinates(point);
        }

        @Override
        public Point toWorldCoordinates(Widget widget, Point point) {
            return point;
        }
    };
    public static Coordinates Widget = new Coordinates() {
        @Override
        public Point toWidgetCoordinates(Widget widget, Point point) {
            return point;
        }

        @Override
        public Point toWorldCoordinates(Widget widget, Point point) {
            return widget.widgetCoordinates().toWorldCoordinates(point);
        }
    };
}
