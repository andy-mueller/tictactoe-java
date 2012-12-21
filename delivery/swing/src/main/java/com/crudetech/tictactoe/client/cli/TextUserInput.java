package com.crudetech.tictactoe.client.cli;

import com.crudetech.lang.Enums;
import com.crudetech.tictactoe.game.Grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextUserInput {
    private final BufferedReader in;

    public TextUserInput(InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    public Grid.Location nextLocation() {
        Integer[] location = locationOf(nextInput());

        Grid.Row row = Enums.ofOrdinal(Grid.Row.class, firstOf(location));
        Grid.Column col = Enums.ofOrdinal(Grid.Column.class, secondOf(location));

        return Grid.Location.of(row, col);
    }

    private <T> T secondOf(T[] location) {
        return location[1];
    }

    private <T> T firstOf(T[] location) {
        return location[0];
    }

    private Integer[] locationOf(String[] coordinates) {
        return new Integer[]{
                valueOf(firstOf(coordinates)),
                valueOf(secondOf(coordinates))
        };
    }

    private int valueOf(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            throw new BadFormatException(e);
        }
    }

    private String[] nextInput() {
        String nextLine = nextLine();
        String[] coordinates = nextLine.split(",");
        return requireLength(coordinates, 2);
    }

    private <T> T[] requireLength(T[] array, int length) {
        if (array.length != length) {
            throw new BadFormatException();
        }
        return array;
    }

    private String nextLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class BadFormatException extends RuntimeException {
        public BadFormatException(Throwable e) {
            super(e);
        }

        public BadFormatException() {
            super();
        }
    }
}
