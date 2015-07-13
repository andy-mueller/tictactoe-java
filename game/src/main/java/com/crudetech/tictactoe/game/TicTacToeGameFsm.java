package com.crudetech.tictactoe.game;


import com.crudetech.collections.Pair;

class TicTacToeGameFsm extends TransitionTableFsm<TicTacToeGameFsm.Event, TicTacToeGameFsm.State> {
    public TicTacToeGameFsm(Context context) {
        this(context, State.NotStarted);
    }
    public TicTacToeGameFsm(Context context, State initial) {
        super(initial);
        this.context = context;
    }

    TicTacToeGameFsm transferInto(State state) {
        return new TicTacToeGameFsm(context, state);
    }

    enum State {
        NotStarted {
            @Override
            public void verifyNotStarted() {
            }

            @Override
            public Pair<Player, Grid.Mark> activePlayer(Context context) {
                throw new GameWasNotStartedException();
            }

        },
        StartingPlayersTurn {
            @Override
            public Pair<Player, Grid.Mark> activePlayer(Context context) {
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
        },
        OtherPlayersTurn {
            @Override
            public Pair<Player, Grid.Mark> activePlayer(Context context) {
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
        },
        Evaluate {
            @Override
            public Pair<Player, Grid.Mark> activePlayer(Context context) {
                throw new IllegalStateException();
            }

        },
        Tie {
            @Override
            public Pair<Player, Grid.Mark> activePlayer(Context context) {
                throw new GameIsFinishedException();
            }

        },
        OtherPlayerWins {
            @Override
            public Pair<Player, Grid.Mark> activePlayer(Context context) {
                throw new GameIsFinishedException();
            }

        },
        StartingPlayerWins {
            @Override
            public Pair<Player, Grid.Mark> activePlayer(Context context) {
                throw new GameIsFinishedException();
            }

        };

        public void verifyNotStarted() {
            throw new GameIsAlreadyStartedException();
        }

        public abstract Pair<Player, Grid.Mark> activePlayer(Context context);

        public void verifyPlayersTurn(Context context, Player movingPlayer) {
            if (activePlayer(context).getFirst() != movingPlayer) {
                throw new NotThisPlayersTurnException();
            }
        }

        public Grid.Mark activePlayersMark(Context context) {
            return activePlayer(context).getSecond();
        }

        public  Event currentPlayerWinsEvent(){
            throw new IllegalStateException();
        }

        public Event nextPlayersTurn() {
            throw new IllegalStateException();
        }
    }

    enum Event {Start, Move, SwitchToStartingPlayer, SwitchToOtherPlayer, Tie, StartingPlayerWins, OtherPlayerWins}

    interface Context {
        void starting();

        void eval();

        void switchTurnToStartingPlayer();

        void switchTurnToOtherPlayer();

        void tie();

        void startingPlayerWins();

        void otherPlayerWins();

        Pair<Player, Grid.Mark> getStartingPlayer();

        Pair<Player, Grid.Mark> getOtherPlayer();
    }

    private final Context context;


    {
        addTransition(State.NotStarted, Event.Start, State.StartingPlayersTurn, starting());
        addTransition(State.StartingPlayersTurn, Event.Move, State.Evaluate, eval());
        addTransition(State.OtherPlayersTurn, Event.Move, State.Evaluate, eval());
        addTransition(State.Evaluate, Event.SwitchToOtherPlayer, State.OtherPlayersTurn, switchTurnToOtherPlayer());
        addTransition(State.Evaluate, Event.SwitchToStartingPlayer, State.StartingPlayersTurn, switchTurnToStartingPlayer());
        addTransition(State.Evaluate, Event.Tie, State.Tie, tie());
        addTransition(State.Evaluate, Event.StartingPlayerWins, State.StartingPlayerWins, startingPlayerWins());
        addTransition(State.Evaluate, Event.OtherPlayerWins, State.OtherPlayerWins, otherPlayerWins());
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
    private Runnable eval() {
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
