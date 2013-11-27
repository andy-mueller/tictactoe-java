package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;

import java.util.Objects;

public class GridCells {
    private GridCells() {
    }

    public static UnaryFunction<Grid.Cell, Grid.Location> location() {
        return new UnaryFunction<Grid.Cell, Grid.Location>() {
            @Override
            public Grid.Location execute(Grid.Cell cell) {
                return cell.getLocation();
            }
        };
    }

    public static UnaryFunction<Grid.Cell, Boolean> markIsEqualTo(final Grid.Mark mark) {
        return new UnaryFunction<Grid.Cell, Boolean>() {
            @Override
            public Boolean execute(Grid.Cell cell) {
                return Objects.equals(cell.getMark(), mark);
            }
        };
    }
}
