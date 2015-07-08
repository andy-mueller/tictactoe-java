package com.crudetech.tictactoe.game;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.crudetech.matcher.Verify.verifyThat;

public class TicTacToeGameFsm extends TransitionTableFsm<TicTacToeGameFsm.Event, TicTacToeGameFsm.State> {
    public TicTacToeGameFsm(Game game) {
        super(State.NotStarted);
        this.game = game;
    }

    enum State {
        NotStarted {
            @Override
            public void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column) {
                throw new TicTacToeGame.GameWasNotStartedException();
            }

            @Override
            public void verifyNotStarted() {
            }
        },
        FirstPlayersTurn {
            @Override
            public void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column) {
                verifyThat(context.getGrid(), isNotMarkedAt(row, column));
                verifyPlayersTurn(context.get1stPlayer(), movingPlayer);
                LinearRandomAccessGrid grid = context.getGrid();
                context.setGrid(grid.setAt(row, column, context.get1stPlayersMark()));
            }

            @Override
            public Player getCurrentPlayer(Player firstPlayer, Player unused) {
                return firstPlayer;
            }
        },
        SecondPlayersTurn {
            @Override
            public void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column) {
                verifyThat(context.getGrid(), isNotMarkedAt(row, column));
                verifyPlayersTurn(context.get2ndPlayer(), movingPlayer);
                LinearRandomAccessGrid grid = context.getGrid();
                context.setGrid(grid.setAt(row, column, context.get2ndPlayersMark()));
            }

            @Override
            public Player getCurrentPlayer(Player unused, Player secondPlayer) {
                return secondPlayer;
            }
     },
        Evaluate {
            @Override
            public void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column) {
                throw new IllegalStateException();
            }
        },
        Tie {
            @Override
            public void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column) {
                throw new TicTacToeGame.GameIsFinishedException();
            }
        },
        SecondPlayerWins {
            @Override
            public void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column) {
                throw new TicTacToeGame.GameIsFinishedException();
            }
        },
        FirstPlayerWins {
            @Override
            public void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column) {
                throw new TicTacToeGame.GameIsFinishedException();
            }
        };

        private static void verifyPlayersTurn(Player currentPlayer, Player movingPlayer) {
            if (currentPlayer != movingPlayer) {
                throw new TicTacToeGame.NotThisPlayersTurnException();
            }
        }


        public abstract void makeMove(Game context, Player movingPlayer, Grid.Row row, Grid.Column column);

        public void verifyNotStarted() {
            throw new TicTacToeGame.GameIsAlreadyStartedException();
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

        public Player getCurrentPlayer(Player firstPlayer, Player secondPlayer) {
            throw new IllegalStateException(String.format("The property currentPlayer is not valid in the state [%s]", this));
        }
    }

    enum Event {Move, SwitchTo1stPlayer, SwitchTo2ndPlayer, Tie, FirstPlayerWins, SecondPlayerWins, Start}

    interface Game {
        void starting();

        void eval();

        void switchTurnTo1stPlayer();

        void switchTurnTo2ndPlayer();

        void tie();

        void firstPlayerWins();

        void secondPlayerWins();

        LinearRandomAccessGrid getGrid();

        void setGrid(LinearRandomAccessGrid linearRandomAccessGrid);

        Player get1stPlayer();

        Player get2ndPlayer();

        Grid.Mark get1stPlayersMark();
        Grid.Mark get2ndPlayersMark();
    }

    private final Game game;

    {
        addTransition(State.NotStarted, Event.Start, State.FirstPlayersTurn, new Runnable() {
            @Override
            public void run() {
                game.starting();
            }
        });
        addTransition(State.FirstPlayersTurn, Event.Move, State.Evaluate, new Runnable() {
            @Override
            public void run() {
                game.eval();
            }
        });
        addTransition(State.SecondPlayersTurn, Event.Move, State.Evaluate, new Runnable() {
            @Override
            public void run() {
                game.eval();
            }
        });
        addTransition(State.Evaluate, Event.SwitchTo2ndPlayer, State.SecondPlayersTurn, new Runnable() {
            @Override
            public void run() {
                game.switchTurnTo2ndPlayer();
            }
        });
        addTransition(State.Evaluate, Event.SwitchTo1stPlayer, State.FirstPlayersTurn, new Runnable() {
            @Override
            public void run() {
                game.switchTurnTo1stPlayer();
            }
        });
        addTransition(State.Evaluate, Event.Tie, State.Tie, new Runnable() {
            @Override
            public void run() {
                game.tie();
            }
        });
        addTransition(State.Evaluate, Event.FirstPlayerWins, State.FirstPlayerWins, new Runnable() {
            @Override
            public void run() {
                game.firstPlayerWins();
            }
        });
        addTransition(State.Evaluate, Event.SecondPlayerWins, State.SecondPlayerWins, new Runnable() {
            @Override
            public void run() {
                game.secondPlayerWins();
            }
        });
    }

}
