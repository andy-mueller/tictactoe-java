package com.crudetech.tictactoe.usecases;


import com.crudetech.collections.Pair;
import com.crudetech.tictactoe.game.AlphaBetaPruningPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

import java.util.Objects;

class GameReference {
    final TicTacToeGame game;
    final Pair<Object, Player> startPlayer;
    final Pair<Object, Player> otherPlayer;

    GameReference(TicTacToeGame game, Pair<Object, Player> startPlayer, Pair<Object, Player> otherPlayer) {
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

    private Player getPlayerById(Object playerId) {
        if (Objects.equals(startPlayer.getFirst(), playerId)) {
            return startPlayer.getSecond();
        }
        if (Objects.equals(otherPlayer.getFirst(), playerId)) {
            return otherPlayer.getSecond();
        }
        throw new IllegalArgumentException("Unknown PlayerId!");
    }

    public void makeMove(Object movingPlayerId, Grid.Location move, Presenter presenter) {
        Player movingPlayer = getPlayerById(movingPlayerId);
        if (movingPlayer instanceof HumanPlayer) {
            ((HumanPlayer) movingPlayer).setPresenter(presenter);
        }
        if (game.isFinished()) {
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
        private PlayerReference otherPlayer;
        private Grid.Mark startPlayerMark;
        private PlayerReference startingPlayer;

        public Builder withStartPlayer(PlayerReference startingPlayer) {
            this.startingPlayer = startingPlayer;
            return this;
        }

        Player convertStartingPlayer(PlayerReference player) {
            if (player instanceof HumanPlayerReference) {
                return new GameReference.HumanPlayer();
            } else if (player instanceof AiPlayerReference) {
                return AlphaBetaPruningPlayer.builder().withMark(startPlayerMark).asStartingPlayer();
            } else
                throw new IllegalArgumentException();
        }

        Player convertOtherPlayer(PlayerReference player) {
            if (player instanceof HumanPlayerReference) {
                return new GameReference.HumanPlayer();
            } else if (player instanceof AiPlayerReference) {
                return AlphaBetaPruningPlayer.builder().withMark(startPlayerMark.getOpposite()).asSecondPlayer();
            } else
                throw new IllegalArgumentException();
        }

        static class PlayerFactory {

            Player convertOtherPlayer(PlayerReference player, Grid.Mark playersMark) {
                if (player instanceof HumanPlayerReference) {
                    return new GameReference.HumanPlayer();
                } else if (player instanceof AiPlayerReference) {
                    return AlphaBetaPruningPlayer.builder().withMark(playersMark.getOpposite()).asSecondPlayer();
                } else
                    throw new IllegalArgumentException();
            }
        }

        public Builder withStartPlayerMark(Grid.Mark startPlayerMark) {
            this.startPlayerMark = startPlayerMark;
            return this;
        }

        public Builder withOtherPlayer(PlayerReference otherPlayer) {
            this.otherPlayer = otherPlayer;
            return this;
        }

        public GameReference build() {
            Player startingPlayer = convertStartingPlayer(this.startingPlayer);
            Player otherPlayer = convertOtherPlayer(this.otherPlayer);

            TicTacToeGame game = gameBuilder(startingPlayer, startPlayerMark, otherPlayer).build();

            return new GameReference(game,
                    new Pair<>(this.startingPlayer.getId(), startingPlayer),
                    new Pair<>(this.otherPlayer.getId(), otherPlayer)
            );
        }

        TicTacToeGame.Builder gameBuilder(Player startingPlayer, Grid.Mark startPlayersMark, Player otherPlayer) {
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
