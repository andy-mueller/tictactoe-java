package com.crudetech.gui.widgets;

import java.util.Objects;

public class Rectangle {

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle rhs) {
        this(rhs.x, rhs.y, rhs.width, rhs.height);
    }

    @Override
    public boolean equals(Object o) {
        EqualsBuilder<Rectangle> b = new EqualsBuilder<>(this, o);
        if (b.notEquals()) {
            return false;
        }
        Rectangle that = b.other();
        return b
                .append(x, that.x)
                .append(y, that.y)
                .append(width, that.width)
                .append(height, that.height)
                .equals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(x)
                .append(y)
                .append(width)
                .append(height)
                .hashCode();
    }

    public boolean contains(int x, int y) {
        return this.x <= x && x <= (this.x + width)
                && this.y <= y && y <= (this.y + height);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public Rectangle setLocation(int x, int y) {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle translate(int dx, int dy) {
        return setLocation(x + dx, y + dy);
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    private static class HashCodeBuilder {
        private int result = 1;

        HashCodeBuilder append(Object o) {
            result = 31 * result + (o == null ? 0 : o.hashCode());
            return this;
        }

        @Override
        public int hashCode() {
            return result;
        }
    }

    private static class EqualsBuilder<T> {
        private final T other;
        private boolean equals;

        public EqualsBuilder(T me, Object other) {
            equals = sameObject(me, other) || sameType(me, other);
            this.other = cast(other);
        }

        @SuppressWarnings("unchecked")
        private T cast(Object other) {
            return (T) other;
        }

        private boolean sameType(Object lhs, Object rhs) {
            return rhs != null && lhs.getClass() == rhs.getClass();
        }

        private boolean sameObject(Object lhs, Object rhs) {
            return lhs == rhs;
        }

        public T other() {
            return other;
        }

        public boolean notEquals() {
            return !equals();
        }

        public EqualsBuilder<T> append(Object lhs, Object rhs) {
            equals = equals && Objects.equals(lhs, rhs);
            return this;
        }

        public boolean equals() {
            return equals;
        }
    }
}
