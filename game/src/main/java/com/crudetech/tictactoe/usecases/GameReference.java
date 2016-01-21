package com.crudetech.tictactoe.usecases;


import com.crudetech.collections.Pair;
import com.crudetech.tictactoe.game.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.*;

class GameReference {

    private final PlayerReference startPlayer;
    private final Grid.Mark startingPlayersMark;
    private final PlayerReference otherPlayer;

    private LinearRandomAccessGrid grid;
    private TicTacToeGameFsm fsm;
    private final GameFsmContext contextFsmContext;

    GameReference(PlayerReference startPlayer, Grid.Mark startingPlayersMark, PlayerReference otherPlayer, Grid grid, TicTacToeGameFsm.State state) {
        this.startPlayer = startPlayer;
        this.startingPlayersMark = startingPlayersMark;
        this.otherPlayer = otherPlayer;
        this.grid = LinearRandomAccessGrid.of(grid);
        this.contextFsmContext = new GameFsmContext();
        fsm = new TicTacToeGameFsm(contextFsmContext, state);
    }

    GameReference start() {
        fsm.handleEvent(TicTacToeGameFsm.Event.Start);
        return this;
    }



    @Override
    public String toString() {
        return "GameReference{" +
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


    private class GameFsmContext implements TicTacToeGameFsm.Context {
        private Presenter presenter = nullPresenter();

        private void setPresenter(Presenter presenter) {
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

        @Override
        public void starting() {
            verifyThat(getStartingPlayer().getFirst(), is(anyOf(equalTo((Object) startPlayer), equalTo((Object) otherPlayer))));
            verifyThat(getStartingPlayer().getSecond(), is(not(Grid.Mark.None)));

            presenter.display(grid);
        }

        @Override
        public void eval() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            if (didWin(triple)) {
                TicTacToeGameFsm.Event currentPlayerWins = fsm.previousState().currentPlayerWinsEvent();
                fsm.handleEvent(currentPlayerWins);
            } else if (grid.isTieForFirstPlayersMark(getStartingPlayer().getSecond())) {
                fsm.handleEvent(TicTacToeGameFsm.Event.Tie);
            } else {
                TicTacToeGameFsm.Event nextPlayersTurn = fsm.previousState().nextPlayersTurn();
                fsm.handleEvent(nextPlayersTurn);
            }
        }

        private boolean didWin(Grid.ThreeInARow triple) {
            return !triple.equals(Grid.ThreeInARow.Empty);
        }


        @Override
        public void switchTurnToStartingPlayer() {
            startPlayer.yourTurn(GameReference.this);
        }

        @Override
        public void switchTurnToOtherPlayer() {
            otherPlayer.yourTurn(GameReference.this);
        }

        @Override
        public void tie() {
            presenter.display(grid);
            presenter.finished();
        }

        @Override
        public void startingPlayerWins() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            presenter.display(grid);
            presenter.highlight(triple);
        }

        @Override
        public void otherPlayerWins() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            presenter.display(grid);
            presenter.highlight(triple);
        }

        @Override
        public void startingPlayerMoved() {
            presenter.display(grid);
        }

        @Override
        public void otherPlayerMoved() {
            presenter.display(grid);
        }

        @Override
        public Pair<Object, Grid.Mark> getStartingPlayer() {
            return new Pair<Object, Grid.Mark>(startPlayer, startingPlayersMark);
        }

        @Override
        public Pair<Object, Grid.Mark> getOtherPlayer() {
            return new Pair<Object, Grid.Mark>(otherPlayer, startingPlayersMark.getOpposite());
        }
    }

    public void makeMove(Object movingPlayerId, Grid.Location move, Presenter presenter) {
        if (fsm.currentState().isFinished()) {
            presenter.gameAlreadyFinished();
            return;
        }

        contextFsmContext.setPresenter(presenter);

        Grid.Row row = move.getRow();
        Grid.Column column = move.getColumn();
        verifyThat(grid, isNotMarkedAt(row, column));
        TicTacToeGameFsm.State state = fsm.currentState();
        state.verifyPlayersTurn(contextFsmContext, playerForId(movingPlayerId));

        Grid.Mark activePlayersMark = state.activePlayersMark(contextFsmContext);
        placeMark(row, column, activePlayersMark);

        fsm.handleEvent(TicTacToeGameFsm.Event.Move);


//        contextFsmContext.resetPresenter();
    }

    private PlayerReference playerForId(Object playerId) {
        if (startPlayer.hasId(playerId)) {
            return startPlayer;
        } else if (otherPlayer.hasId(playerId)) {
            return otherPlayer;
        } else {
            throw new RuntimeException("No player matches ID: " + playerId);
        }
    }

    private void placeMark(Grid.Row row, Grid.Column column, Grid.Mark mark) {
        grid = grid.setAt(row, column, mark);
    }

    private static Matcher<LinearRandomAccessGrid> isNotMarkedAt(final Grid.Row row, final Grid.Column column) {
        return new TypeSafeMatcher<LinearRandomAccessGrid>() {
            @Override
            protected boolean matchesSafely(LinearRandomAccessGrid grid) {
                return !grid.hasMarkAt(row, column);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("The grid was already marked at the specified location[%s, %s]", row, column));
            }
        };
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private PlayerReference otherPlayer;
        private Grid.Mark startPlayerMark;
        private PlayerReference startingPlayer;
        private Grid grid = LinearRandomAccessGrid.empty();
        private TicTacToeGameFsm.State state = TicTacToeGameFsm.State.NotStarted;

        public Builder withStartPlayer(PlayerReference startingPlayer) {
            this.startingPlayer = startingPlayer;
            return this;
        }

        public Builder withStartPlayerMark(Grid.Mark startPlayerMark) {
            this.startPlayerMark = startPlayerMark;
            return this;
        }

        public Builder withOtherPlayer(PlayerReference otherPlayer) {
            this.otherPlayer = otherPlayer;
            return this;
        }

        public Builder withGrid(Grid grid) {
            this.grid = grid;
            return this;
        }

        public Builder withState(TicTacToeGameFsm.State state) {
            this.state = state;
            return this;
        }

        public GameReference build() {
            return new GameReference(startingPlayer, startPlayerMark, otherPlayer, grid, state);
        }
    }
}
