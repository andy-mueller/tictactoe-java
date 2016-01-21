package com.crudetech.tictactoe.game;


import com.crudetech.collections.Pair;

public class TicTacToeGameFsm extends TransitionTableFsm<TicTacToeGameFsm.Event, TicTacToeGameFsm.State> {
    public TicTacToeGameFsm(Context context) {
        this(context, State.NotStarted);
    }

    public TicTacToeGameFsm(Context context, State initial) {
        super(initial);
        this.context = context;
    }

    public TicTacToeGameFsm transferInto(State state) {
        return new TicTacToeGameFsm(context, state);
    }

    public enum State {
        NotStarted {
            @Override
            public void verifyNotStarted() {
            }

            @Override
            public Pair<Object, Grid.Mark> activePlayer(Context context) {
                throw new GameWasNotStartedException();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

        },
        StartingPlayersTurn {
            @Override
            public Pair<Object, Grid.Mark> activePlayer(Context context) {
                return context.getStartingPlayer();
            }

            @Override
            public Event currentPlayerWinsEvent() {
                return Event.StartingPlayerWins;
            }

            @Override
            public Event nextPlayersTurn() {
                return Event.SwitchToOtherPlayer;
            }
            @Override
            public boolean isFinished() {
                return false;
            }
        },
        OtherPlayersTurn {
            @Override
            public Pair<Object, Grid.Mark> activePlayer(Context context) {
                return context.getOtherPlayer();
            }

            @Override
            public Event currentPlayerWinsEvent() {
                return Event.OtherPlayerWins;
            }

            @Override
            public Event nextPlayersTurn() {
                return Event.SwitchToStartingPlayer;
            }
            @Override
            public boolean isFinished() {
                return false;
            }
        },
        Evaluate {
            @Override
            public Pair<Object, Grid.Mark> activePlayer(Context context) {
                throw new IllegalStateException();
            }
            @Override
            public boolean isFinished() {
                return false;
            }
        },
        Tie {
            @Override
            public Pair<Object, Grid.Mark> activePlayer(Context context) {
                throw new GameIsFinishedException();
            }
            @Override
            public boolean isFinished() {
                return true;
            }
        },
        OtherPlayerWins {
            @Override
            public Pair<Object, Grid.Mark> activePlayer(Context context) {
                throw new GameIsFinishedException();
            }
            @Override
            public boolean isFinished() {
                return true;
            }
        },
        StartingPlayerWins {
            @Override
            public Pair<Object, Grid.Mark> activePlayer(Context context) {
                throw new GameIsFinishedException();
            }
            @Override
            public boolean isFinished() {
                return true;
            }
        };

        public void verifyNotStarted() {
            throw new GameIsAlreadyStartedException();
        }

        public abstract Pair<Object, Grid.Mark> activePlayer(Context context);

        public void verifyPlayersTurn(Context context, Object movingPlayer) {
            if (activePlayer(context).getFirst() != movingPlayer) {
                throw new NotThisPlayersTurnException();
            }
        }

        public Grid.Mark activePlayersMark(Context context) {
            return activePlayer(context).getSecond();
        }

        public Event currentPlayerWinsEvent() {
            throw new IllegalStateException();
        }

        public Event nextPlayersTurn() {
            throw new IllegalStateException();
        }

        public abstract boolean isFinished();
    }

    public enum Event {Start, Move, SwitchToStartingPlayer, SwitchToOtherPlayer, Tie, StartingPlayerWins, OtherPlayerWins}

    public interface Context {
        void starting();

        void eval();

        void switchTurnToStartingPlayer();

        void switchTurnToOtherPlayer();

        void tie();

        void startingPlayerWins();

        void otherPlayerWins();

        void startingPlayerMoved();

        void otherPlayerMoved();

        Pair<Object, Grid.Mark> getStartingPlayer();

        Pair<Object, Grid.Mark> getOtherPlayer();
    }

    private final Context context;


    {
        addTransition(State.NotStarted, Event.Start, NoOp, State.StartingPlayersTurn, starting());
        addTransition(State.StartingPlayersTurn, Event.Move, startingPlayerMoved(), State.Evaluate, evaluate());
        addTransition(State.OtherPlayersTurn, Event.Move, otherPlayerMoved(), State.Evaluate, evaluate());
        addTransition(State.Evaluate, Event.SwitchToOtherPlayer, NoOp, State.OtherPlayersTurn, switchTurnToOtherPlayer());
        addTransition(State.Evaluate, Event.SwitchToStartingPlayer, NoOp, State.StartingPlayersTurn, switchTurnToStartingPlayer());
        addTransition(State.Evaluate, Event.Tie, NoOp, State.Tie, tie());
        addTransition(State.Evaluate, Event.StartingPlayerWins, NoOp, State.StartingPlayerWins, startingPlayerWins());
        addTransition(State.Evaluate, Event.OtherPlayerWins, NoOp, State.OtherPlayerWins, otherPlayerWins());
    }

    private Runnable otherPlayerMoved() {
        return new Runnable() {
            @Override
            public void run() {
                context.otherPlayerMoved();
            }
        };
    }

    private Runnable startingPlayerMoved() {
        return new Runnable() {
            @Override
            public void run() {
                context.startingPlayerMoved();
            }
        };
    }

    private Runnable otherPlayerWins() {
        return new Runnable() {
            @Override
            public void run() {
                context.otherPlayerWins();
            }
        };
    }

    private Runnable startingPlayerWins() {
        return new Runnable() {
            @Override
            public void run() {
                context.startingPlayerWins();
            }
        };
    }

    private Runnable switchTurnToStartingPlayer() {
        return new Runnable() {
            @Override
            public void run() {
                context.switchTurnToStartingPlayer();
            }
        };
    }

    private Runnable tie() {
        return new Runnable() {
            @Override
            public void run() {
                context.tie();
            }
        };
    }

    private Runnable switchTurnToOtherPlayer() {
        return new Runnable() {
            @Override
            public void run() {
                context.switchTurnToOtherPlayer();
            }
        };
    }

    private Runnable evaluate() {
        return new Runnable() {
            @Override
            public void run() {
                context.eval();
            }
        };
    }

    private Runnable starting() {
        return new Runnable() {
            @Override
            public void run() {
                context.starting();
            }
        };
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
}
