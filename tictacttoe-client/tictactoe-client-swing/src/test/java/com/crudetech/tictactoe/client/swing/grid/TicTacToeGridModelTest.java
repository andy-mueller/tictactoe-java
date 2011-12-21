package com.crudetech.tictactoe.client.swing.grid;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class TicTacToeGridModelTest {
    <T> Iterable<? extends Iterable<? extends T>> create(){
        ArrayList<LinkedList<T>> arrayLists = new ArrayList<>();
        return arrayLists;
    }
    @Test
    public void ctorSetsModelData() {
        Iterable<? extends Iterable<? extends Object>> iterables = create();
    }
}
