package com.crudetech.tictactoe.game;

class PlayerStub implements Player {
    private Grid lastGrid = null;
    private Grid winGrid = null;
    private Grid loosingGrid;
    private int turnCount = 0;
    private LinearRandomAccessGrid.Triple winTriple;
    private Grid tieGrid;

    @Override
    public void yourTurn(Grid actualGrid) {
        lastGrid = actualGrid;
        turnCount++;
    }

    @Override
    public void youWin(Grid actualGrid, LinearRandomAccessGrid.Triple triple) {
        this.winGrid = actualGrid;
        this.winTriple = triple;
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.Triple triple) {
        loosingGrid = actualGrid;
    }

    @Override
    public void tie(Grid actualGrid) {
        this.tieGrid = actualGrid;
    }

    int getTurnCount() {
        return turnCount;
    }

    Grid getLastGrid() {
        return lastGrid;
    }

    Grid getWinGrid() {
        return winGrid;
    }

    LinearRandomAccessGrid.Triple getWinTriple() {
        return winTriple;
    }

    boolean wasWinning() {
        return getWinGrid() != null;
    }

    Grid getLoosingGrid() {
        return loosingGrid;
    }

    boolean wasLoosing() {
        return getLoosingGrid() != null;
    }

    Grid getTieGrid() {
        return tieGrid;
    }

    boolean wasTie() {
        return getTieGrid() != null;
    }
}
