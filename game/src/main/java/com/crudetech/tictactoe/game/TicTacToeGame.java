package com.crudetech.tictactoe.game;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.*;

public class TicTacToeGame {
    private final TicTacToeGameFsm.Game gameFsmContext;
    private LinearRandomAccessGrid grid = new LinearRandomAccessGrid();
    private final Player player1;
    private final Player player2;


    private Player firstPlayer;
    private Grid.Mark startingPlayersMark;

    private final TicTacToeGameFsm fsm;


    public TicTacToeGame(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        setBackReferenceOnPlayer();

        gameFsmContext = gameFsmContext();
        fsm = new TicTacToeGameFsm(gameFsmContext());
    }

    private void setBackReferenceOnPlayer() {
        this.player1.setGame(this);
        this.player2.setGame(this);
    }

    private Player getCurrentPlayer() {
        return fsm.currentState().getCurrentPlayer(firstPlayer, getSecondPlayer());
    }

    private Player getSecondPlayer() {
        return firstPlayer == player1 ? player2 : player1;
    }


    static class GameIsFinishedException extends IllegalStateException {
        GameIsFinishedException() {
            super("The game is finished!");
        }
    }

    static class GameIsAlreadyStartedException extends IllegalStateException {
        GameIsAlreadyStartedException() {
            super("The game was already started!");
        }
    }

    static class NotThisPlayersTurnException extends IllegalStateException {
        NotThisPlayersTurnException() {
            super("It is not the passed in players turn!");
        }
    }

    static class GameWasNotStartedException extends IllegalStateException {
        GameWasNotStartedException() {
            super("The game was not started yet!");
        }
    }

    public Player getStartingPlayer() {
        return firstPlayer;
    }

    public Grid.Mark getStartingPlayersMark() {
        return startingPlayersMark;
    }
    private TicTacToeGameFsm.Game gameFsmContext() {
        return new TicTacToeGameFsm.Game() {
            @Override
            public void starting() {
                verifyThat(firstPlayer, is(anyOf(equalTo(player1), equalTo(player2))));
                verifyThat(startingPlayersMark, is(not(Grid.Mark.None)));

                TicTacToeGame.this.firstPlayer= firstPlayer;
                getCurrentPlayer().yourTurn(grid);
            }

            @Override
            public void eval() {
                Grid.ThreeInARow triple = grid.getThreeInARow();
                if (didWin(triple)) {
                    if (wasFirstPlayersTurn())
                        fsm.handleEvent(TicTacToeGameFsm.Event.FirstPlayerWins);
                    else
                        fsm.handleEvent(TicTacToeGameFsm.Event.SecondPlayerWins);
                } else if (grid.isTieForFirstPlayersMark(startingPlayersMark)) {
                    fsm.handleEvent(TicTacToeGameFsm.Event.Tie);
                } else {
                    if (wasFirstPlayersTurn())
                        fsm.handleEvent(TicTacToeGameFsm.Event.SwitchTo2ndPlayer);
                    else
                        fsm.handleEvent(TicTacToeGameFsm.Event.SwitchTo1stPlayer);
                }
            }

            @Override
            public void switchTurnTo1stPlayer() {
                getSecondPlayer().moveWasMade(grid);
                firstPlayer.yourTurn(grid);
            }

            @Override
            public void switchTurnTo2ndPlayer() {
                firstPlayer.moveWasMade(grid);
                getSecondPlayer().yourTurn(grid);
            }

            @Override
            public void tie() {
                player1.tie(grid);
                player2.tie(grid);
            }

            @Override
            public void firstPlayerWins() {
                Grid.ThreeInARow triple = grid.getThreeInARow();
                firstPlayer.youWin(grid, triple);
                getSecondPlayer().youLoose(grid, triple);
            }

            @Override
            public void secondPlayerWins() {
                Grid.ThreeInARow triple = grid.getThreeInARow();
                firstPlayer.youLoose(grid, triple);
                getSecondPlayer().youWin(grid, triple);
            }

            @Override
            public LinearRandomAccessGrid getGrid() {
                return TicTacToeGame.this.grid;
            }

            @Override
            public void setGrid(LinearRandomAccessGrid grid) {
                TicTacToeGame.this.grid = grid;
            }

            @Override
            public Player get1stPlayer() {
                return firstPlayer;
            }

            @Override
            public Player get2ndPlayer() {
                return getSecondPlayer();
            }

            @Override
            public Grid.Mark get1stPlayersMark() {
                return startingPlayersMark;
            }

            @Override
            public Grid.Mark get2ndPlayersMark() {
                return startingPlayersMark.getOpposite();
            }
        };
    }

    private boolean wasFirstPlayersTurn() {
        return fsm.previousState() == TicTacToeGameFsm.State.FirstPlayersTurn;
    }

    public void startWithPlayer(Player startingPlayer, Grid.Mark startPlayersMark) {
        fsm.currentState().verifyNotStarted();

        this.firstPlayer = startingPlayer;
        this.startingPlayersMark = startPlayersMark;


        fsm.handleEvent(TicTacToeGameFsm.Event.Start);
    }

    public void makeMove(Player player, Grid.Location location) {
        makeMove(player, location.getRow(), location.getColumn());
    }

    public void makeMove(Player player, Grid.Row row, Grid.Column column) {
        fsm.currentState().makeMove(gameFsmContext, player, row, column);
        fsm.handleEvent(TicTacToeGameFsm.Event.Move);
    }


    private boolean didWin(Grid.ThreeInARow triple) {
        return !triple.equals(Grid.ThreeInARow.Empty);
    }
}
