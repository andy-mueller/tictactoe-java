package com.crudetech.tictactoe.ui;

import com.crudetech.event.EventObject;
import com.crudetech.tictactoe.game.Grid;

import java.util.Objects;

public class CellEventObject<TEventSource> extends EventObject<TEventSource> {
    private final Grid.Location clickedCellLocation;

    public CellEventObject(TEventSource eventSource, Grid.Location clickedCellLocation) {
        super(eventSource);
        this.clickedCellLocation = clickedCellLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CellEventObject that = (CellEventObject) o;

        return Objects.equals(clickedCellLocation, that.clickedCellLocation);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(clickedCellLocation);
        return result;
    }

    @Override
    public String toString() {
        return "CellEventObject{" +
                "source=" + getSource() +
                "clickedCellLocation=" + clickedCellLocation +
                '}';
    }

    public Grid.Location getCellLocation() {
        return clickedCellLocation;
    }
}
