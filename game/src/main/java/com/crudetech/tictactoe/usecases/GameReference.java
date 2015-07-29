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

        void gameAlreadyFinished();
    }

    public Player getPlayerById(Object playerId) {
        return startPlayer;
    }

    public void makeMove(Object movingPlayerId, Grid.Location move, Presenter presenter) {
        Player movingPlayer = getPlayerById(movingPlayerId);
        if (movingPlayer instanceof HumanPlayer) {
            ((HumanPlayer) movingPlayer).setPresenter(presenter);
        }
        if(game.isFinished()){
            presenter.gameAlreadyFinished();
            return;
        }
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

        public Builder withStartPlayer(PlayerReference startingPlayer) {
            this.startingPlayer = convert(startingPlayer);
            return this;
        }

        Player convert(PlayerReference player) {
            return null;
        }

        public Builder withStartPlayerMark(Grid.Mark startPlayerMark) {
            this.startPlayerMark = startPlayerMark;
            return this;
        }

        public Builder withOtherPlayer(PlayerReference otherPlayer) {
            this.otherPlayer = convert(otherPlayer);
            return this;
        }

        public GameReference build() {
            TicTacToeGame g = gameBuilder().build();

            return new GameReference(g, startingPlayer, otherPlayer);
        }

        TicTacToeGame.Builder gameBuilder() {
            return TicTacToeGame.builder()
                    .withStartingPlayer(startingPlayer)
                    .withStartingPlayersMark(startPlayerMark)
                    .withOtherPlayer(otherPlayer);
        }
    }

    /**
     *
     */
    static class HumanPlayer implements Player {
        private Presenter presenter = nullPresenter();

        @Override
        public void yourTurn(Grid actualGrid) {
            presenter.display(actualGrid);
        }

        @Override
        public void youWin(Grid actualGrid, Grid.ThreeInARow triple) {
            presenter.display(actualGrid);
            presenter.highlight(triple);
            presenter.finished();
        }

        @Override
        public void youLoose(Grid actualGrid, Grid.ThreeInARow triple) {
            presenter.display(actualGrid);
            presenter.highlight(triple);
            presenter.finished();
        }

        @Override
        public void tie(Grid actualGrid) {
            presenter.display(actualGrid);
            presenter.finished();
        }

        @Override
        public void moveWasMade(Grid actualGrid) {
            presenter.display(actualGrid);
        }

        @Override
        public void setGame(TicTacToeGame game) {
        }

        public void setPresenter(Presenter presenter) {
            this.presenter = presenter;
        }

        void resetPresenter() {
            presenter = nullPresenter();
        }

        private Presenter nullPresenter() {
            return new Presenter() {
                @Override
                public void display(Grid grid) {
                }

                @Override
                public void highlight(Grid.ThreeInARow threeInARow) {
                }

                @Override
                public void finished() {

                }

                @Override
                public void gameAlreadyFinished() {
                }
            };
        }
    }
}
