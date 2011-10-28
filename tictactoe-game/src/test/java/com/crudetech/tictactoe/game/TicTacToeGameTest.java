package com.crudetech.tictactoe.game;

import org.junit.Before;
import org.junit.Test;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;


public class TicTacToeGameTest {

    private PlayerStub firstPlayer;
    private PlayerStub secondPlayer;
    private TicTacToeGame game;

    @Before
    public void setUp() throws Exception {
        firstPlayer = new PlayerStub();
        secondPlayer = new PlayerStub();
        game = new TicTacToeGame(firstPlayer, secondPlayer);
    }

    @Test
    public void startHasEmptyBoard() {

        game.startWithPlayer(secondPlayer, Grid.Mark.Cross);

        Grid emptyGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
        });

        assertThat(firstPlayer.getTurnCount(), is(0));
        assertThat(secondPlayer.getLastGrid(), is(emptyGrid));
    }

    @Test
    public void startWithNoMarkThrows() {
        Runnable startWithNull = new Runnable() {
            @Override
            public void run() {
                game.startWithPlayer(firstPlayer, Grid.Mark.None);
            }
        };

        assertThat(startWithNull, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void startWithNullThrows() {

        Runnable startWithNull = new Runnable() {
            @Override
            public void run() {
                game.startWithPlayer(null, Grid.Mark.Cross);
            }
        };

        assertThat(startWithNull, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void startCanOnlyBeCalledOnce() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        Runnable startASecondTime = new Runnable() {
            @Override
            public void run() {
                game.startWithPlayer(firstPlayer, Grid.Mark.Cross);
            }
        };

        assertThat(startASecondTime, doesThrow(TicTacToeGame.GameIsAlreadyStartedException.class));
    }

    @Test
    public void startWithDifferentPlayerThrows() {
        Runnable startWithDifferentPlayer = new Runnable() {
            @Override
            public void run() {
                game.startWithPlayer(mock(Player.class), Grid.Mark.Cross);
            }
        };

        assertThat(startWithDifferentPlayer, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void addMarkWithWrongPlayerThrows() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        Runnable startWithDifferentPlayer = new Runnable() {
            @Override
            public void run() {
                game.addMark(secondPlayer, Grid.Row.Second, Grid.Column.First);
            }
        };

        assertThat(startWithDifferentPlayer, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void addMarkAddsMarkAndLetsOtherPlayerPlay() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Nought);

        game.addMark(firstPlayer, Grid.Row.Second, Grid.Column.First);

        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
        });

        assertThat(secondPlayer.getLastGrid(), is(expectedGrid));
        assertThat(secondPlayer.getTurnCount(), is(1));
    }

    @Test
    public void addSameMarkTwiceThrows() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Nought);

        game.addMark(firstPlayer, Grid.Row.Second, Grid.Column.First);

        Runnable addSameMarkSecondTime = new Runnable() {
            @Override
            public void run() {
                game.addMark(secondPlayer, Grid.Row.Second, Grid.Column.First);
            }
        };

        assertThat(addSameMarkSecondTime, doesThrow(IllegalArgumentException.class));
    }


    @Test
    public void addingSomethingWithNullRowThrows() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);


        Runnable addWithNullRow = new Runnable() {
            @Override
            public void run() {
                game.addMark(firstPlayer, null, Grid.Column.First);
            }
        };

        assertThat(addWithNullRow, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void addingSomethingWithNullColumnThrows() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);


        Runnable addWithNullColumn = new Runnable() {
            @Override
            public void run() {
                game.addMark(firstPlayer, Grid.Row.Second, null);
            }
        };

        assertThat(addWithNullColumn, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void gameThatIsAlmostFinishedShowCorrectGrid() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
        });

        assertThat(firstPlayer.getTurnCount(), is(4));
        assertThat(secondPlayer.getTurnCount(), is(3));
        assertThat(firstPlayer.getLastGrid(), is(expectedGrid));
    }

    private static void setupAlmostFinishedGame(Player first, Player second, TicTacToeGame game) {
        //   |   | 2
        //---+---+---
        // 1 | 1 | 2
        //---+---+---
        // 1 | 2 |
        game.addMark(first, Grid.Row.Second, Grid.Column.First);
        game.addMark(second, Grid.Row.First, Grid.Column.Third);

        game.addMark(first, Grid.Row.Third, Grid.Column.First);
        game.addMark(second, Grid.Row.Second, Grid.Column.Third);

        game.addMark(first, Grid.Row.Second, Grid.Column.Second);
        game.addMark(second, Grid.Row.Third, Grid.Column.Second);
    }

    @Test
    public void winningPlayerIsInformedWhenWinning() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.First, Grid.Column.First);


        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
        });


        assertThat(firstPlayer.wasWinning(), is(true));
        assertThat(firstPlayer.getWinGrid(), is(expectedGrid));
    }

    @Test
    public void winningShowsWinningCellsToWinningPlayer() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.First, Grid.Column.First);


        Grid.Triple expected = Grid.Triple.of(Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.First),
                Grid.Location.of(Grid.Row.Third, Grid.Column.First)
        );
        assertThat(firstPlayer.getWinTriple(), is(expected));
    }

    @Test
    public void loosingPlayerIsInformedWhenLoosing() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.First, Grid.Column.First);


        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
        });

        assertThat(secondPlayer.wasLoosing(), is(true));
        assertThat(secondPlayer.getLoosingGrid(), is(expectedGrid));
    }

    @Test
    public void thereIsNoNewTurnAfterADecidedGame() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.First, Grid.Column.First);

        assertThat(firstPlayer.wasWinning(), is(true));
        assertThat(firstPlayer.getTurnCount(), is(4));
        assertThat(secondPlayer.getTurnCount(), is(3));
    }

    @Test
    public void addMarkThrowsAfterDecidedGame() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.First, Grid.Column.First);

        Runnable addMark = new Runnable() {
            @Override
            public void run() {
                game.addMark(secondPlayer, Grid.Row.First, Grid.Column.Second);
            }
        };

        assertThat(addMark, doesThrow(TicTacToeGame.GameIsFinishedException.class));
    }

    @Test
    public void bothPlayersAreInformedOnTie() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.Third, Grid.Column.Third);
        game.addMark(secondPlayer, Grid.Row.First, Grid.Column.First);


        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
        });


        assertThat(firstPlayer.wasTie(), is(true));
        assertThat(firstPlayer.getTieGrid(), is(expectedGrid));

        assertThat(secondPlayer.wasTie(), is(true));
        assertThat(secondPlayer.getTieGrid(), is(expectedGrid));
    }

    @Test
    public void noNewTurnAfterTie() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.Third, Grid.Column.Third);
        game.addMark(secondPlayer, Grid.Row.First, Grid.Column.First);


        assertThat(firstPlayer.getTurnCount(), is(4));
        assertThat(secondPlayer.getTurnCount(), is(4));
    }

    @Test
    public void addMarkThrowsAfterTieGame() {
        game.startWithPlayer(firstPlayer, Grid.Mark.Cross);

        setupAlmostFinishedGame(firstPlayer, secondPlayer, game);

        game.addMark(firstPlayer, Grid.Row.Third, Grid.Column.Third);
        game.addMark(secondPlayer, Grid.Row.First, Grid.Column.First);

        Runnable addMarkOnLastFreeCell = new Runnable() {
            @Override
            public void run() {
                game.addMark(firstPlayer, Grid.Row.First, Grid.Column.Second);
            }
        };

        assertThat(addMarkOnLastFreeCell, doesThrow(TicTacToeGame.GameIsFinishedException.class));
    }
}
