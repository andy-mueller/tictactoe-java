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

    interface Presenter {
        void display(Grid grid);

        void highlight(Grid.ThreeInARow threeInARow);

        void finished();
    }
    public Player getPlayerById(Object playerId) {
        return startPlayer;
    }

    public void makeMove(Object movingPlayerId, Grid.Location move, Presenter presenter) {
        HumanPlayer movingPlayer = (HumanPlayer) getPlayerById(movingPlayerId);
        movingPlayer.setPresenter(presenter);
        game.makeMove(movingPlayer, move);
//        movingPlayer.resetPresenter();
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private Player otherPlayer;
        private Grid.Mark startPlayerMark;
        private Player startingPlayer;

        public Builder withStartPlayer(Player startingPlayer) {
            this.startingPlayer = startingPlayer;
            return this;
        }

        public Builder withStartPlayerMark(Grid.Mark startPlayerMark) {
            this.startPlayerMark = startPlayerMark;
            return this;
        }

        public Builder withOtherPlayer(Player otherPlayer) {
            this.otherPlayer = otherPlayer;
            return this;
        }

        public GameReference build() {
            TicTacToeGame g = newGame(startingPlayer, otherPlayer);
            g.startWithPlayer(startingPlayer, startPlayerMark);
            return new GameReference(g, startingPlayer, otherPlayer);
        }

        TicTacToeGame newGame(Player startingPlayer, Player otherPlayer) {
            return new TicTacToeGame(startingPlayer, otherPlayer);
        }
    }
}
