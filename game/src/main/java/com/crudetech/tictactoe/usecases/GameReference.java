package com.crudetech.tictactoe.usecases;


import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

class GameReference {
    final TicTacToeGame game;
    final Player startPlayer;
    final Player otherPlayer;

    GameReference(TicTacToeGame game, Player startPlayer, Player otherPlayer) {
        this.game = game;
        this.startPlayer = startPlayer;
        this.otherPlayer = otherPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameReference that = (GameReference) o;

        if (!game.equals(that.game)) return false;
        if (!otherPlayer.equals(that.otherPlayer)) return false;
        if (!startPlayer.equals(that.startPlayer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = game.hashCode();
        result = 31 * result + startPlayer.hashCode();
        result = 31 * result + otherPlayer.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GameReference{" +
                "game=" + game +
                ", startPlayer=" + startPlayer +
                ", otherPlayer=" + otherPlayer +
                '}';
    }

    public Player getPlayerById(Object playerId) {
        return startPlayer;
    }

    public void makeMove(Player movingPlayer, Grid.Location move) {
        game.makeMove(movingPlayer, move);
    }
}
