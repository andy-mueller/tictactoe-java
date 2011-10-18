package com.crudetech.tictactoe.game;


public class Enums {
    public static <T extends Enum<T>> T ofOrdinal(Class<T> enumClass, int ordinal) {
        return enumClass.getEnumConstants()[ordinal];
    }
}
