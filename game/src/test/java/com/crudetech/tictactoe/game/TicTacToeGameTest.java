package com.crudetech.tictactoe.game;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(HierarchicalContextRunner.class)
public class TicTacToeGameTest {

    private PlayerStub firstPlayer;
    private PlayerStub secondPlayer;
    private TicTacToeGame game;

    @Before
    public void createGame() throws Exception {
        firstPlayer = new PlayerStub();
        secondPlayer = new PlayerStub();
        game = new TicTacToeGame(firstPlayer, secondPlayer);
    }

    @SuppressWarnings("unused")
    public class WithNonStartedGame {
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
        public void startWithNullPlayerThrows() {
            Runnable startWithNull = new Runnable() {
                @Override
                public void run() {
                    game.startWithPlayer(null, Grid.Mark.Cross);
                }
            };

            assertThat(startWithNull, doesThrow(IllegalArgumentException.class));
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
        public void noActionHappensBeforeStart() {
            Grid NoGrid = null;

            assertThat(firstPlayer.getTurnCount(), is(0));
            assertThat(secondPlayer.getLastGrid(), is(NoGrid));
        }
    }

    @SuppressWarnings("unused")
    public class WithStartedGame {
        @Before
        public void startGame() throws Exception {
            game.startWithPlayer(firstPlayer, Grid.Mark.Cross);
        }

        @Test
        public void boardIsEmptyAfterStart() {
            Grid emptyGrid = LinearRandomAccessGrid.empty();

            assertThat(firstPlayer.getTurnCount(), is(1));
            assertThat(secondPlayer.getTurnCount(), is(0));
            assertThat(firstPlayer.getLastGrid(), is(emptyGrid));
        }

        @Test
        public void firstPlayerIsNotifiedOAfterStart() {
            Grid emptyGrid = LinearRandomAccessGrid.empty();

            assertThat(firstPlayer.getTurnCount(), is(1));
            assertThat(secondPlayer.getTurnCount(), is(0));
        }

        @Test
        public void startCanOnlyBeCalledOnce() {
            Runnable startASecondTime = new Runnable() {
                @Override
                public void run() {
                    game.startWithPlayer(firstPlayer, Grid.Mark.Cross);
                }
            };

            assertThat(startASecondTime, doesThrow(TicTacToeGame.GameIsAlreadyStartedException.class));
        }

        @Test
        public void addMarkWithWrongPlayerThrows() {
            Runnable startWithDifferentPlayer = new Runnable() {
                @Override
                public void run() {
                    game.makeMove(secondPlayer, Grid.Row.Second, Grid.Column.First);
                }
            };

            assertThat(startWithDifferentPlayer, doesThrow(TicTacToeGame.NotThisPlayersTurnException.class));
        }

        @Test
        public void addMarkAddsMarkAndLetsOtherPlayerPlay() {
            game.makeMove(firstPlayer, Grid.Row.Second, Grid.Column.First);

            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                    Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                    Grid.Mark.None, Grid.Mark.None, Grid.Mark.None);

            assertThat(secondPlayer.getLastGrid(), is(expectedGrid));
            assertThat(secondPlayer.getTurnCount(), is(1));
        }

        @Test
        public void addSameMarkTwiceThrows() {
            game.makeMove(firstPlayer, Grid.Row.Second, Grid.Column.First);

            Runnable addSameMarkSecondTime = new Runnable() {
                @Override
                public void run() {
                    game.makeMove(secondPlayer, Grid.Row.Second, Grid.Column.First);
                }
            };

            assertThat(addSameMarkSecondTime, doesThrow(IllegalArgumentException.class));
        }


        @Test
        public void addingSomethingWithNullRowThrows() {
            Runnable addWithNullRow = new Runnable() {
                @Override
                public void run() {
                    game.makeMove(firstPlayer, null, Grid.Column.First);
                }
            };

            assertThat(addWithNullRow, doesThrow(IllegalArgumentException.class));
        }

        @Test
        public void addingSomethingWithNullColumnThrows() {
            Runnable addWithNullColumn = new Runnable() {
                @Override
                public void run() {
                    game.makeMove(firstPlayer, Grid.Row.Second, null);
                }
            };

            assertThat(addWithNullColumn, doesThrow(IllegalArgumentException.class));
        }
    }

    @SuppressWarnings("unused")
    public class WithAlmostFinishedGame {
        @Before
        public void setupAlmostFinishedGame() {
            game.startWithPlayer(firstPlayer, Grid.Mark.Cross);
            TicTacToeGameMother gameMother = new TicTacToeGameMother();
            gameMother.setupAlmostFinishedGame(firstPlayer, secondPlayer, game);
        }

        @Test
        public void gameThatIsAlmostFinishedShowCorrectGrid() {
            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None);

            assertThat(firstPlayer.getTurnCount(), is(4));
            assertThat(secondPlayer.getTurnCount(), is(3));
            assertThat(firstPlayer.getLastGrid(), is(expectedGrid));
        }


        @Test
        public void winningPlayerIsInformedWhenWinning() {
            game.makeMove(firstPlayer, Grid.Row.First, Grid.Column.First);

            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None);


            assertThat(firstPlayer.wasWinning(), is(true));
            assertThat(firstPlayer.getWinGrid(), is(expectedGrid));
        }

        @Test
        public void winningShowsWinningCellsToWinningPlayer() {
            game.makeMove(firstPlayer, Grid.Row.First, Grid.Column.First);

            Grid.ThreeInARow expected = Grid.ThreeInARow.of(Grid.Mark.Cross,
                    Grid.Location.of(Grid.Row.First, Grid.Column.First),
                    Grid.Location.of(Grid.Row.Second, Grid.Column.First),
                    Grid.Location.of(Grid.Row.Third, Grid.Column.First)
            );
            assertThat(firstPlayer.getWinTriple(), is(expected));
        }

        @Test
        public void loosingPlayerIsInformedWhenLoosing() {
            game.makeMove(firstPlayer, Grid.Row.First, Grid.Column.First);

            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None);

            assertThat(secondPlayer.wasLoosing(), is(true));
            assertThat(secondPlayer.getLoosingGrid(), is(expectedGrid));
        }

        @Test
        public void thereIsNoNewTurnAfterADecidedGame() {
            game.makeMove(firstPlayer, Grid.Row.First, Grid.Column.First);

            assertThat(firstPlayer.wasWinning(), is(true));
            assertThat(firstPlayer.getTurnCount(), is(4));
            assertThat(secondPlayer.getTurnCount(), is(3));
        }

        @Test
        public void addMarkThrowsAfterDecidedGame() {
            game.makeMove(firstPlayer, Grid.Row.First, Grid.Column.First);

            Runnable addMark = new Runnable() {
                @Override
                public void run() {
                    game.makeMove(secondPlayer, Grid.Row.First, Grid.Column.Second);
                }
            };

            assertThat(addMark, doesThrow(TicTacToeGame.GameIsFinishedException.class));
        }

        @Test
        public void bothPlayersAreInformedOnTie() {
            game.makeMove(firstPlayer, Grid.Row.Third, Grid.Column.Third);
            game.makeMove(secondPlayer, Grid.Row.First, Grid.Column.First);


            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross);


            assertPlayerWasInformedAboutTie(firstPlayer, expectedGrid);
            assertPlayerWasInformedAboutTie(secondPlayer, expectedGrid);
        }

        private void assertPlayerWasInformedAboutTie(PlayerStub firstPlayer, Grid expectedGrid) {
            assertThat(firstPlayer.wasTie(), is(true));
            assertThat(firstPlayer.getTieGrid(), is(expectedGrid));
        }

        @Test
        public void noNewTurnAfterTie() {
            game.makeMove(firstPlayer, Grid.Row.Third, Grid.Column.Third);
            game.makeMove(secondPlayer, Grid.Row.First, Grid.Column.First);


            assertThat(firstPlayer.getTurnCount(), is(4));
            assertThat(secondPlayer.getTurnCount(), is(4));
        }

        @Test
        public void addMarkThrowsAfterTieGame() {
            game.makeMove(firstPlayer, Grid.Row.Third, Grid.Column.Third);
            game.makeMove(secondPlayer, Grid.Row.First, Grid.Column.First);

            Runnable addMarkOnLastFreeCell = new Runnable() {
                @Override
                public void run() {
                    game.makeMove(firstPlayer, Grid.Row.First, Grid.Column.Second);
                }
            };

            assertThat(addMarkOnLastFreeCell, doesThrow(TicTacToeGame.GameIsFinishedException.class));
        }
    }

    @Test
    public void gameSetsBackReferencesOnPlayers() {
        assertThat(firstPlayer.game, is(game));
        assertThat(secondPlayer.game, is(game));
    }
}
